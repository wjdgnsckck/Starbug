package starbug.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import starbug.model.dto.order.OrdersDto;
import starbug.model.entity.member.CouponHistoryEntity;
import starbug.model.entity.member.StarBugMemberEntity;
import starbug.model.entity.order.OrderDetailsEntity;
import starbug.model.entity.order.OrdersEntity;
import starbug.model.entity.product.ProductEntity;
import starbug.model.repository.member.CouponHistoryEntityRepository;
import starbug.model.repository.member.StarBugMemberEntityRepository;
import starbug.model.repository.order.OrderDetailsEntityRepository;
import starbug.model.repository.order.OrdersEntityRepository;
import starbug.model.repository.order.PosEntityRepository;
import starbug.model.repository.product.ProductEntityRepository;
import starbug.service.resoures.InventoryService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class PosService {

    @Autowired
    private PosEntityRepository posEntityRepository;
    @Autowired
    private CouponHistoryEntityRepository couponEntityRepository;
    @Autowired
    private OrdersEntityRepository ordersEntityRepository;
    @Autowired
    private OrderDetailsEntityRepository orderDetailsEntityRepository;
    @Autowired
    private StarBugMemberEntityRepository memberEntityRepository;
    @Autowired
    private ProductEntityRepository productEntityRepository;

    @Autowired
    private InventoryService inventoryService;


    // 1. 주문하기 ============================================================================================
    @Transactional
    public boolean postOrders (Map<String, Object> objectMap ) {
        System.out.println("PosService.postOrders");
        System.out.println("objectMap = " + objectMap);

        // cartItem과 cartMember 객체를 가져오기 ----------------------------------------------
        Map<String, Object> cartItem = (Map<String, Object>) objectMap.get("cartItem");
        Map<String, Object> cartMember = (Map<String, Object>) objectMap.get("cartMember");
        //System.out.println("cartItem -> "+cartItem);
        //System.out.println("cartMember -> "+cartMember);

        // cartMember 내의 값을 가져오기 -------------------------------------------
        int mno = Integer.parseInt(String.valueOf(cartMember.get("mno")));
        int chisno = Integer.parseInt(String.valueOf(cartMember.get("chisno")));
        //System.out.println("mno -> "+mno);
        //System.out.println("chisno -> "+chisno);

        // 총 주문금액 구하기 -------------------------------------------
        int totalPrice = 0;
        for (Map.Entry<String, Object> entry : cartItem.entrySet()) {
            Map<String, Object> item = (Map<String, Object>) entry.getValue();
            int pprice = (int) item.get("pprice");
            int pevnetprice = (int) item.get("pevent");
            totalPrice += pprice;
            totalPrice -= pevnetprice; // 이벤트 할인
        }

        // 쿠폰 사용여부 판단후 실 결제 금액 구하기 --------------------------------------------------------
        int paid = totalPrice;
        // 주문자가 쿠폰을 사용했으면 0이 아님
        if( chisno != 0 ) {
            // 쿠폰 카테고리 테이블에서 ccpercent 값 구해오기
            Map<String, Object> couponHistory = couponEntityRepository.findByChisno(chisno);
            if (couponHistory != null) {
                int ccpercent = (int) couponHistory.get("ccpercent");
                //System.out.println("ccpercent : " + ccpercent);
                // 할인 퍼센트 계산된 금액 계산
                paid = totalPrice - (totalPrice / ccpercent);
                //System.out.println(" 결제 할 금액 " + paid);
            }
        } // 해당 로직을 거치지 않는다면 쿠폰을 사용하지 않은 것이라 총 주문 금액과 실결제 금액이 같다.

        // DTO 생성하기 -----------------------------------------------------------------------------
        OrdersDto dto = OrdersDto.builder()
                .mno(mno)               // 주문한 회원번호
                .paid(paid)             // 실 결졔금액
                .totalprice(totalPrice) // 총 주문금액
                .build();
        //System.out.println(" 저장될 OrdersDto -> "+dto);

        // ☆☆☆ 단방향 저장
        // 주문 테이블에 넣고 해당 주문 번호 가져오기  -------------------------------------------------
        OrdersEntity ordersEntity = ordersEntityRepository.save( dto.toOrders() );
        int ono = ordersEntity.getOno();
        //System.out.println("방금 저장한 ono -> "+ono);

        // 그리고 가져온 주문에서 쿠폰을 사용했을 경우 쿠폰테이블에 주문번호 넣어 상태변경하기 ------------------------
        if( chisno != 0 ) {
            Optional<CouponHistoryEntity> useCoupon = couponEntityRepository.findByMnoAndCoupon(mno, chisno);
            if (useCoupon.isPresent()) {
                useCoupon.get().setChstate(ono);
            }
        }

        // ☆☆☆ 단방향 저장
        // 방금 만든 주문 번호를 사용해 주문 상세 테이블에 넣고 주문디테일번호들 받아오기  --------------------
            // 담아둘곳
        List<Integer> odnoList = new ArrayList<>();
        cartItem.forEach((key, value) -> {
            Map<String, Object> od = (Map<String, Object>) value;
            int pno = (int) od.get("pno");
            System.out.println("pno -> "+pno);


            OrdersDto detailsDto = OrdersDto.builder()
                    .ono(ono)
                    .pno(pno)
                    .build();
            //System.out.println("forEach key detailsDto= "+detailsDto);
            //System.out.println("forEach key detailsEntity= "+detailsDto.toOrderDetails());
            OrderDetailsEntity detailsEntity = orderDetailsEntityRepository.save( detailsDto.toOrderDetails());
            odnoList.add(detailsEntity.getOdtno());

            // --------------------- ★★★ 양방향 저장하기 -----------------------------------------------
            // 1. 주문상세에 주문 넣어주기 ---------------
            detailsEntity.setOrdersEntity( ordersEntity );

            // 2. 주문에 주문상세 넣어주기 --------------- LIST에 담는 것이 나중에
            ordersEntity.getOrderDetailsEntities().add( detailsEntity );

            // 3. 주문상세에 상품 넣어주기 ---------------
            Optional<ProductEntity> productEntity = productEntityRepository.findById(pno);
            if(productEntity.isPresent()){
                detailsEntity.setProductEntity( productEntity.get() );
            }

            // 4. 상품에 주문상세 넣어주기 --------------- LIST에 담는 것이 나중에
            productEntity.get().getOrderDetailsEntities().add( detailsEntity );

        }); // forEach end

            // 5. 주문에 회원 넣어주기 ---------------
            Optional<StarBugMemberEntity> orderMem = memberEntityRepository.findById(mno);
            if(orderMem.isPresent()){
                ordersEntity.setStarBugMemberEntity( orderMem.get() );
            }
            // 6. 회원에 주문 넣어주기 -------------- LIST에 담는 것이 나중에
            orderMem.get().getOrdersEntities().add( ordersEntity );

        // --------------------- ★★★ 양방향 저장하기 end -----------------------------------------------

        // 모든 상세 주문 정보가 저장 되었는지 판단하기 -------------------------------------------
        boolean allPositive = true;
        System.out.println("odnoList == " + odnoList);
        for (int i = 0; i < odnoList.size(); i++) {
            if (odnoList.get(i) <= 0) {
                allPositive = false;
                break;
            } else {
                cartItem.forEach((key, value) -> {
                    Map<String, Object> od = (Map<String, Object>) value;
                    int pno = (int) od.get("pno");
                    inventoryService.insertCount(pno); // 정훈이 함수 호출
                }); // forEach end
            }
        }
        // 모든 주문 디테일번호가 0보다 큰 경우 성공
        if (allPositive) { return true; }
        // 하나라도 0 이하의 숫자가 있는 경우 실패
        else { return false; }



    }


    // 2. 쿠폰 정보 호출하기 ============================================================================================
    @Transactional
    public List<Map<String , Object>> getMemberCoupon( int mno ) {
        System.out.println("PosService.getMemberCoupon");

        // 회원이 소지한 모든 쿠폰 호출하기 --------------------------------------------------------
        List<Map<String , Object>> coupons  = couponEntityRepository.findByMemberCoupon(mno);

        if ( coupons.size() != 0 ) {
            System.out.println("coupons = " + coupons.get(0).get("cccontent"));

            // 반환하려고 만들어둔 리스트 ---------------------------------------------------------------
            List<Map<String , Object>> positiveCoupons = new ArrayList<>();

            // 사용 가능한 쿠폰인지 판단하고 만들어둔 리스트에 담기 -------------------------------------------
            if( !coupons.isEmpty() ){
                for (Map<String, Object> c : coupons) {
                    int chstate = (int) c.get("chstate");
                    if (chstate == 0) {
                        positiveCoupons.add(c);
                    }
                }
            }
            return positiveCoupons;

        }
            return null;
    }


    // 3. 환불하기 ============================================================================================
    @Transactional
    public boolean cancelOrder( int ono ) {
        System.out.println("PosService.cancelOrder");

        // 주문번호에 맞는 엔티티 찾아서 주문 상태 변경하기 -----------------------------------------------------
            // 0: 판매 ( 기본값 ) , 1: 환불
        Optional<OrdersEntity> ordersEntity = ordersEntityRepository.findById(ono);
        if(ordersEntity.isPresent()) {
            System.out.println("PosService.cancelOrder 주문테이블변경");
            OrdersEntity order = ordersEntity.get();
            order.setOstate(1);

            // 사용했던 쿠폰이 있다면 쿠폰 반환하기 -----------------------------------------------------------
            Optional<CouponHistoryEntity> couponHistory = couponEntityRepository.findByOrder(ono);
            System.out.println(couponHistory);
            if(couponHistory.isPresent()) {
                System.out.println("PosService.cancelOrder 쿠폰내역테이블변경");
                CouponHistoryEntity history = couponHistory.get();
                history.setChstate(0);
            } return true;
        } else { return false; }

    }


}
