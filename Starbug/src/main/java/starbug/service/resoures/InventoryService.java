package starbug.service.resoures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import starbug.model.dto.page.PageDto;
import starbug.model.dto.resoures.ResouresDto;
import starbug.model.entity.inventory.InventoryLogEntity;
import starbug.model.entity.parcel.ProducerEntity;
import starbug.model.entity.product.ProductEntity;
import starbug.model.entity.resoures.RecipeEntity;
import starbug.model.entity.resoures.RescategoryEntity;
import starbug.model.entity.resoures.ResouresEntity;
import starbug.model.repository.product.ProductEntityRepository;
import starbug.model.repository.resoures.Inventory_LogEntityRepository;
import starbug.model.repository.resoures.RecipeEntityRepository;
import starbug.model.repository.resoures.RescategoryEntityRepository;
import starbug.model.repository.resoures.ResouresEntityRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private Inventory_LogEntityRepository   inventory; //재고 로그 엔티티 repository
    @Autowired
    private ResouresEntityRepository resoures;  //재료 엔티티 repository
    @Autowired
    private RecipeEntityRepository recipe ; // 레시피 엔티티
    @Autowired
    private ProductEntityRepository productEntityRepository; // 제품 엔티티
    @Autowired
    private RescategoryEntityRepository rescategoryEntityRepository; //레시피 카테고리를 얻기위한 엔티티


    // [C] : 입력받은 pno를 이용해 재고 로그에 값을 넣어주는 메소드 [ 제품페이지에서 결제시 사용]
    @Transactional
    public  boolean insertCount(int pno){
        ProductEntity productEntity = productEntityRepository.getById(pno);          //pno에 맞는 레시피 찾기

        List<RecipeEntity> recipeList =productEntity.getRecipeEntities();            //레시피 에서 찾은 재료를 list에 담기
        boolean result =false;
        for (RecipeEntity recipeEntity : recipeList) {                               //레시피에 있는 재료 번호와 레시피에 맞는 재료개수 찾기
            int listSize = recipeList.size();                                        // 레시피 크기에 맞게 돌기 위한 변수 선언
            int resno = recipeEntity.getResouresEntity().getResno();                 //레시피 안에 재료엔티티의 재료번호 담기
            Double rquantity = recipeEntity.getRquantity();                          //레시피안에 필요한 재료개수 담기
            if (insertinventory(resno,rquantity,listSize)                            //재료 번호와 재료 개수를 찾는 메소드 호출하기
            ){result =true;}
        };

        return result;
    }
    int listCount = 0;                                                               //리스트 크기와 비교하기 위한 변수 선언
    // [C] : pno에 맞는 rno를 받아 resno를 확인하는 메소드
    @Transactional
    public  boolean insertinventory(int  resno,Double count,int listSize){           // 재고 관리 페이지 출력시 사용
        LocalDateTime date = LocalDateTime.now();                                    //현재 시간 출력해주는 메소드
        InventoryLogEntity inventoryLogEntity                                        // db타입에 맞게 커스텀 해서 save 해주기
                = inventory.save(InventoryLogEntity.builder()
                .inloghistory(-count).inlogdate(date)
                .resouresEntity(ResouresEntity.builder().resno(resno).build())
                .build());                                                           // inventorylog에 값을 저장하기!

        Optional<ResouresEntity> resouresEntity = resoures.findById(resno);          //단방향넣기 위해 resno에 맞는 데이터 가져오기
        if (resouresEntity.isPresent()) {
            if (inventoryLogEntity.getInlogno() > 1) {
                //맞을시 단방향 저장하기 !
                inventoryLogEntity.setResouresEntity(resouresEntity.get());
                listCount ++;                                                        //리스트의 크기를 증가한다!(크기와 맞을때 break하기위해)
                if(listSize==listCount) {                                            //리스트 사이즈 만큼 반복했을때 !
                    listCount=0;                                                     //리스트 전역변수 초기화
                    return true;                                                     //true 를 반환해준다.
                }
            } else {
                return false;
            }
        }

        return false;
    }
    //  [C] : 입력받은값을 저장해주는 메소드 [ 재고 입력 페이지 ]
    @Transactional
    public boolean insertResoures(ResouresDto dto){                                 //재고 입력 페이지
        ResouresEntity resouresEntity = resoures.findByResname(dto.getResname()); //3번 사용하니 메소드 안에 전역변수로 선언
        if(resouresEntity!=null) {                                                  // 맞는 재료 이름이 있을때
            InventoryLogEntity inventoryLogEntity = inventory.save(InventoryLogEntity.builder()
                    .inlogdate(LocalDateTime.now()).inloghistory(dto.getInloghistory()).
                    resouresEntity(resouresEntity).build()); //값을 save 해줘야함! resouresEntity는 매개변수dto에 resname이 있어 그걸 찾은걸 넣어준다.
            if (inventoryLogEntity.getInlogno() > 1) {
                inventoryLogEntity.setResouresEntity(resouresEntity); // 넣을시 단방향 저장
                List<InventoryLogEntity> inventoryLogEntityList = resouresEntity.getInventoryLogEntities();  //양방향 저장을위해 찾은 resouresEntity의 인벤토리로그 엔팉꺼내주기
                inventoryLogEntityList.add(inventoryLogEntity);                                             //꺼낸값에 현재 로그 넣어주기
                resouresEntity.setInventoryLogEntities(inventoryLogEntityList);                             //그뒤 그 리스트를 저장하기
                return true;
            }
        }
        return false;                                       // 맞는 재료 이름이없을때는 false 반환

    }

    //[R] : 재고의 이름을 입력받아 존재 여부 확인해주는 메소드 [재고수정 페이지]
    @Transactional
    public  boolean exist(String resname){
        ResouresEntity resouresEntity =  resoures.findByResname(resname);           // 재고이름으로 재료찾기
        if(resouresEntity!=null){                                                   // 값이 있으면
            return true;
        }
        else {
            return false;
        }
    }

    //[R] : 엑셀파일에 맞게 리스트를 반환해주는 메소드
    @Transactional
    public List<ResouresDto> ResouresExcel () {
        List<ResouresEntity> resouresList =  resoures.findAll();                    // 재고 찾기
        List<ResouresDto> resouresDtoList = new ArrayList<>();                      // 재고를 담을 list만들기 dto형으로 반환해야함
        //rescount 재료개수
        resouresList.forEach((r)->{                                                 //맞게 넣어주기
            Double rescount= resouresCount(r.getResno());
            ResouresDto resouresDto = ResouresDto.builder()
                    .resno(r.getResno()).rescname(r.getRescategoryEntity().getRescname())
                    .resname(r.getResname()).resprice(r.getResprice()).rescount(rescount)
                    .build();
            resouresDtoList.add(resouresDto);
        });
        System.out.println("resouresDtoList = " + resouresDtoList);
        return resouresDtoList;

    }



    //[R] : 처음에 재료(재고)를 전체 출력해주는 메소드
    @Transactional
    public PageDto getResoures(int page,int view, String keyword){
        System.out.println("page = " + page);
        Pageable pageable = PageRequest.of(page-1,view);                      //page인터페이스 사용 구현체 만들기 page=>현재클릭페이지-1,view는보여주는개수
        Page<ResouresEntity> resouresEntity=resoures.findBySearch(keyword,pageable);       //keyword를 가지고 검색
        List<ResouresDto> ResoureCount = new ArrayList<>();                        // DB에 없는 재료 개수를 담아두는 리스트
        resouresEntity.forEach( (r)->{
                    if(r!=null) {                                                           //가져온 값이 null 이 아닐때
                        int resno = r.getResno();                                           //그 값의 resno를 가져온다.
                        Double Count=resouresCount(resno);                                  //그 값을 resouresCount함수의 매개변수로 넣고 double형으로 반환
                        // 판매개수
                        if(Count != null) {                                               // 그 값이 null이 아닐때
                            ResouresDto resouresDto = ResouresDto.builder().resno(r.getResno()).//요구사항에 맞게 출력하기위해 dto 담기
                                    resname(r.getResname()).resprice(r.getResprice())
                                    .rescname(r.getRescategoryEntity().getRescname()).
                                    rescount(Count).build();
                            //값을 리스트에 넣어서 전송한다.
                            ResoureCount.add(resouresDto);
                        } // if end
                    } //if end
                }//for end
        );
        int totalPages = resouresEntity.getTotalPages();                             // 총 페이지수
        Long totalCount=resouresEntity.getTotalElements();                           // 총 게시물수
        PageDto pageDto = PageDto.builder()                                          // PageDto에 담기
                .ResouresCount(ResoureCount)
                .totalPage(totalPages)
                .totalCount(totalCount)
                .build();
        return pageDto;

    }

    //[R] : 재고 로그를 출력해주는 메소드
    @Transactional
    public  List<ResouresDto> getInventoryLog(String date){
        //1. 재고로그 전체를 찾아서 List에 담기(날짜에 맞는 데이터 담아주기)
        List<InventoryLogEntity> inventoryLogEntitie = findDate(date);
        System.out.println("inventoryLogEntitie = " + inventoryLogEntitie);
        // 1-1 재고 로그에 판매 개수를 구하기 위한 로직 작성하기
        //2. 재고 로그를 담을 resouresdto형 리스트 만들기
        List<ResouresDto> inventoryList= new ArrayList<>();                                     // resouresdto형으로 반환하기 위한 리스트 생성
        if (!inventoryLogEntitie.isEmpty()){
            inventoryLogEntitie.forEach((r) -> {                                                // 로그엔티티로 가져온 정보를 꺼내오기
                        int resno = r.getResouresEntity().getResno();                           // 로그엔티티의 fk인 resouresentity의 resno를 가져오기
                        Double sale = saleResouresCount(date, resno);                           // 날짜에 맞는 재료 판매 개수를 구하기위한 메소드 호출
                        System.out.println("sale = " + sale);
                        if (sale != 0.0) {                                                      // 반환값이 있을때(판매개수가 없을때!)
                            Double saleCount = Math.round(sale * 100) / 100.0;                  // 소수점 둘째 자리 까지 반환하기위한 코드
                            Double Count = resouresCount(resno);                                // 함수에서 resno개수를 리턴받아 rounds에 저장하기
                            Double Rounds = Math.round(Count * 100) / 100.0;                    // 값에 소수점이 있어서 소수점을 2번째 자리 까지 제거하기
                            inventoryList.add(ResouresDto.builder()                             // 반환에 필요한 타입으로 반환하기 !
                                    .resno(r.getResouresEntity().getResno())                    // 재료의 번호
                                    .resname(r.getResouresEntity().getResname())                // 재료의 이름
                                    .inlogdate(r.getInlogdate())                                // 재고로그날짜
                                    .inloghistory(r.getInloghistory())                          // 재고 로그 입출내역
                                    .rescount(Rounds)                                           // 재료개수
                                    .salecount(saleCount)                                       // 날짜의 맞는 판매재료 개수
                                    .resprice(r.getResouresEntity().getResprice())              // 재료금액
                                    .build());

                        }
                    }
            ); // for end
            System.out.println("inventoryList : " + inventoryList);
            return inventoryList;
        }   //if end
        return inventoryList;
    }// p end
    //재고 로그 엑셀 메소드 출력 1. 재고 로그 번호 2. 재고 로그 날짜 3. 재고 로그 개수 4. 재료 번호 5. 재료 이름
    public List<ResouresDto> logExcel(String date){
        System.out.println("InventoryService.logExcel");
        //1. 재고로그 전체를 찾아서 List에 담기(날짜에 맞는 데이터 담아주기)
        List<InventoryLogEntity> inventoryLogEntitie = inventory.findByInlogdate(date);
        System.out.println("inventoryLogEntitie = " + inventoryLogEntitie);
        List<ResouresDto> inventoryList= new ArrayList<>();                         // resouresdto형으로 반환하기 위한 리스트 생성
        if (!inventoryLogEntitie.isEmpty()){
            inventoryLogEntitie.forEach((r)->{
                inventoryList.add(ResouresDto.builder()
                        .inlogno(r.getInlogno())
                        .inlogdate(r.getInlogdate())
                        .inloghistory(r.getInloghistory())
                        .resno(r.getResouresEntity().getResno())
                        .resname(r.getResouresEntity().getResname())
                        .resprice(r.getResouresEntity().getResprice())
                        .build());
            });     //for end
            System.out.println("inventoryList = " + inventoryList);
            return inventoryList;

        }

        else {return null;}
    }
    //날짜에 맞는 데이터 찾기 ! 중복이라서 함수로 만듬
    @Transactional
    public  List<InventoryLogEntity> findDate(String date){ return inventory.findByInlogdateAfter(date);}

    //날짜에 맞는 데이터 존재여부!
    @Transactional
    public boolean existDate (String date){
        List<InventoryLogEntity> exist= findDate(date);
        System.out.println("exist = " + exist);
        if(exist.isEmpty()){return false;}
        else {return true;}

    }
    //재고 로그 판매 개수를 구하기 위한 메소드
    @Transactional
    public Double saleResouresCount(String date,int resno){
        Object Count =  inventory.findBysaleCount(date,resno);
        System.out.println("saleResouresCount");
        if(Count!=null) {
            Double sale = Math.abs((Double)Count); // 날짜와 재료번호를 매개변수로 주고 날짜에 맞는 음수 재료개수를 합산해서 리턴하는 코드
            return sale;
        }
        return 0.0;
    }


    //재료 개수 구하는 메소드
    @Transactional
    public Double resouresCount(int resno){
        Double Count = inventory.findByCount(resno);                            //db에 inventorylog에 resno를 매개변수로 찾기
        Double negative = -1.0;                                                 //만약에 저장된 값이 음수일때(실제는x가상이라넣는거)
        if(Count!=null) {                                                       //만약 count값이 널이 아니면
            if(Count>negative) {                                                //만약 count값이 양수이면
                Count=Math.round(Count*100)/100.0;
                return Count;                                                   //count를 반환
            }
            else{
                Count=0.0;                                                      //음수일때는 0.0 반환 (실제는 음수가없음)
            }
        }
        else {  Count =0.0;}                                                    //null일때도 0.0개를 반환
        return Count;
    }

    //[R] : 재료 검색해주는 메소드
    public  ResouresDto onSerach (String resname) {
        ResouresEntity resouresEntity = resoures.findByResname(resname);            //맞는 가격 찾기
        if(resouresEntity!=null) {
            int rescno = resouresEntity.getRescategoryEntity().getRescno();             //레시피 카테고리 출력을위해 rescno찾기
            RescategoryEntity rescategoryEntity = rescategoryEntityRepository.findById(rescno).orElse(null); //찾은 rescno에서 optional꺼내기
            System.out.println("rescategoryEntity = " + rescategoryEntity);
            if (rescategoryEntity != null) {                                                            //값이 null이 아닐때
                ResouresDto resouresDto = ResouresDto.builder()                             //resouresDto 생성
                        .resno(resouresEntity.getResno())
                        .resname(resouresEntity.getResname())
                        .resprice(resouresEntity.getResprice())
                        .rescname(rescategoryEntity.getRescname())
                        .build();

                return resouresDto;
            }
            else{return null;}
        }
        else{return null;}
    }


    //[U] : 재료를 수정해주는 메소드
    @Transactional
    public boolean updateResoures(ResouresDto dto){
        System.out.println("dto.getresname : " + dto.getResname());
        Optional<ResouresEntity> resouresEntity = resoures.findById(dto.getResno());
        if(resouresEntity.isPresent()){
            ResouresEntity updateResoures = resouresEntity.get();
            //수정!!! 어떤거 할지 정하기
            updateResoures.setResprice(dto.getResprice()); //가격
            updateResoures.setResname(dto.getResname());
            //나머지 넣기
            return true;

        }
        else{return false;}
    }

    //[D] : 재료를 삭제해주는 메소드
    @Transactional
    public boolean  deleteLog(int inlogno){
        System.out.println("Inventory_LogService.deleteResoures");
        System.out.println("inlogno = " + inlogno);
        Optional<InventoryLogEntity>inventoryLogEntity =  inventory.findById(inlogno);

        if(inventoryLogEntity.isPresent()){
            inventory.deleteById(inlogno);
            return true;
        }
        return false;
    }

    //[R] : 모든 재고 로그를 출력해주는 메소드
    @Transactional
    public PageDto getInventory (int page,String keyword ) {
        // System.out.println("keyword = " + keyword.getClass().getName()); //매개변수의 타입을 확인하는법!!! 변수명.getClass().getName()
        Pageable pageable = PageRequest.of(page-1,5);
        // 먼저 키워드에 맞는 resno 찾기! (이유는 inventoryLog에 resname은없다.)
        Page<InventoryLogEntity>  inventoryLogEntity = inventory.findBySearch(keyword, pageable);

        List<ResouresDto> inventoryCount = new ArrayList<>();              //pageDto를 위해 리스트 만들기
        if(inventoryLogEntity!=null){
            inventoryLogEntity.forEach( (Entity) -> {
                ResouresDto resouresDto = ResouresDto.builder().
                        inlogno(Entity.getInlogno())
                        .inlogdate(Entity.getInlogdate())
                        .inloghistory(Entity.getInloghistory())
                        .resno(Entity.getResouresEntity().getResno()).
                        resname(Entity.getResouresEntity().getResname()).
                        build();
                inventoryCount.add(resouresDto);
            });
        }
        int totalPages = inventoryLogEntity.getTotalPages();                             // 총 페이지수
        Long totalCount=inventoryLogEntity.getTotalElements();                           // 총 게시물수
        PageDto pageDto = PageDto.builder()                                          // PageDto에 담기
                .inventoryCount(inventoryCount)
                .totalPage(totalPages)
                .totalCount(totalCount)
                .build();
        System.out.println("pageDto = " + pageDto);
        return pageDto;

    }




    // [R] : 모든 재료를 가져와주는 메소드
    @Transactional
    public List<ResouresDto> getAllResoures() {
        List<ResouresEntity> list = resoures.findAll();
        List<ResouresDto> result = new ArrayList<>();

        for(ResouresEntity resouresEntity : list) {
            result.add(resouresEntity.toResouresDto());
        }

        return result;
    }




}
