package starbug.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import starbug.model.dto.product.ProductDto;
import starbug.model.dto.resoures.ResouresDto;
import starbug.model.entity.product.ProductCategoryEntity;
import starbug.model.entity.product.ProductEntity;
import starbug.model.entity.resoures.RecipeEntity;
import starbug.model.repository.product.ProductCategoryEntityRepository;
import starbug.model.repository.product.ProductEntityRepository;
import starbug.model.repository.resoures.RecipeEntityRepository;
import starbug.model.repository.resoures.ResouresEntityRepository;
import starbug.service.resoures.InventoryService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired // 상품 레포지토리
    private ProductEntityRepository productEntityRepository;

    @Autowired // 상품 카테고리 레포지토리
    private ProductCategoryEntityRepository productCategoryEntityRepository;

    @Autowired // 레시피 레포지토리
    private RecipeEntityRepository recipeEntityRepository;

    @Autowired // 재료 레포지토리
    private ResouresEntityRepository resouresEntityRepository;

    @Autowired // 재고 서비스
    private InventoryService inventoryService;

    // -------------------------------- Product --------------------------------
    @Transactional // 1. 상품 등록
    public boolean registerProduct(ProductDto productDto) {
        // 엔티티로 변환
        ProductEntity productEntity = productDto.toProductEntity();
        // 카테고리 엔티티 삽입
        productEntity.setProductCategoryEntity(productCategoryEntityRepository.findById(productDto.getPcno()).get());
        // 제품 엔티티 DB에 저장
        ProductEntity result = productEntityRepository.save(productEntity);
        System.out.println("result = " + result);
        for(int i = 0; i < productDto.getResnos().size(); i++) {
            // 레시피 i번째 가져오기
            RecipeEntity recipeEntity = productDto.getResnos().get(i).toRecipeEntity();
            // 재료 엔티티 삽입 (resno를 위해)
            recipeEntity.setResouresEntity(resouresEntityRepository.findById(productDto.getResnos().get(i).getResno()).get());
            // 제품 엔티티 삽입 (pno를 위해)
            recipeEntity.setProductEntity(productEntityRepository.findByPname(productDto.getPname()));
            // 레시피 엔티티 DB에 저장
            RecipeEntity result2 = recipeEntityRepository.save(recipeEntity);
            System.out.println("result2 :" + result2.getRno());
        }

        return result.getPno() >= 1;
    }

    @Transactional // 2. 전체/특정메뉴 출력 - 검색 or 특정카테고리별 or 이벤트메뉴
    public List<ProductDto> getProduct(int pcno, String keyword, String searchSelect) { // 카테고리 번호, 키워드(검색), 정렬기준
        List<ProductDto> result = new ArrayList<>();
        System.out.println("pcno = " + pcno + ", keyword = " + keyword + ", searchSelect = " + searchSelect);
        List<ProductEntity> list = productEntityRepository.findByProducts(pcno, keyword);
        System.out.println("list : " + list);



        for(ProductEntity productEntity : list) { // Entity -> Dto로 변환
            System.out.println("test productEntity :" + productEntity);
            result.add(productEntity.toProductDto());
        }

        switch (searchSelect) { // 정렬기준에 따라 정렬
            case "pno":
                result.sort(Comparator.comparing(ProductDto::getPno));
                break;
            case "pname":
                result.sort(Comparator.comparing(ProductDto::getPname));
                break;
            case "pprice":
                result.sort(Comparator.comparing(ProductDto::getPprice));
                break;
        }

        return result;
    }

    @Transactional // 3. 상품 수정하기
    public boolean updateProduct(ProductDto productDto) {
        System.out.println("ProductService.updateProduct");
        Optional<ProductEntity> optionalProductEntity = productEntityRepository.findById(productDto.getPno());
        if(optionalProductEntity.isPresent()) {
            ProductEntity productEntity = optionalProductEntity.get();
            productEntity.setPname(productDto.getPname());
            productEntity.setPprice(productDto.getPprice());
            return true;
        }
        return false;
    }

    @Transactional // 4. 상품 삭제하기
    public boolean deleteProduct(int pno) {
        Optional<ProductEntity> optionalProductEntity = productEntityRepository.findById(pno);
        if(optionalProductEntity.isPresent()){
            productEntityRepository.deleteById(pno);
            return true;
        }
        return false;
    }

    @Transactional // 5. 이벤트 상품 설정하기
    public boolean changeEventProduct(int pno, int pevent, boolean isEvent) {
        Optional<ProductEntity> optionalProductEntity = productEntityRepository.findById(pno);
        if(optionalProductEntity.isPresent()) {
            ProductEntity productEntity = optionalProductEntity.get();
            if(isEvent) { productEntity.setPevent(pevent); }
            else productEntity.setPevent(0);
            return true;
        }
        return false;
    }

    @Transactional // 6. 상품 품절 기능 - 품절된 상품의 식별번호 담아주기
    public List<Integer> selectSoldoutProduct(List<ProductDto> productDtoList) {

        if(productDtoList.get(0).getPno()== 0) return null;
        List<Integer> result = new ArrayList<>(); // 결과 넣어주기

        for(ProductDto productDto : productDtoList) {
            System.out.println("productDto = " + productDto);
            boolean isSoldout = false; // 품절 여부

            ProductEntity productEntity = productEntityRepository.findById(productDto.getPno()).get(); // pno에 맞는 상품 가져오기
            productEntity.setRecipeEntities(recipeEntityRepository.findByPno(productDto.getPno())); // 상품에 맞는 레시피리스트 넣어주기
            System.out.println("productEntity = " + productEntity);
            List<RecipeEntity> recipeEntities = productEntity.getRecipeEntities(); // 레시피리스트 가져오기

            List<ResouresDto> resouresDtos = new ArrayList<>();
            for(RecipeEntity recipeEntity : recipeEntities) { // 재료Dto에 넣어주기
                resouresDtos.add(recipeEntity.toRecipeDto());
            }

            for(ResouresDto resouresDto : resouresDtos) {
                System.out.println("resouresDto = " + resouresDto);
                Double resouresCount = inventoryService.resouresCount(resouresDto.getResno()); // 재료의 남은 수랑 가져오기
                if(resouresCount - resouresDto.getRquantity()<0) { // 남은 수량 - 수정 수량 < 0이라면
                    System.out.println("resouresCount = " + resouresCount);
                    System.out.println("resouresDto = " + resouresDto.getRquantity());
                    isSoldout = true; break; // 품절로 만들기
                }
            }
            if(isSoldout) result.add(productDto.getPno());
        }

        System.out.println("result = " + result );

        return result;
    }

    // -------------------------------- ProductCategory --------------------------------
    @Transactional // 1. 상품 카테고리 가져오기
    public List<ProductDto> getCategory() {
        List<ProductCategoryEntity> optionalProductEntity = productCategoryEntityRepository.findAllCategory();
        List<ProductDto> result = new ArrayList<>();
        for(ProductCategoryEntity productCategory : optionalProductEntity) {
            result.add(ProductDto.builder().pcno(productCategory.getPcno()).pcname(productCategory.getPcname()).build());
        }
        return result;
    }

    @Transactional // 2. 상품 카테고리 등록
    public boolean registerCategory(ProductDto productDto){
        return productCategoryEntityRepository.save(productDto.toProductCategoryEntity()).getPcno()>=1;
    }

    @Transactional // 3. 상품 카테고리 수정
    public boolean updateCategory(ProductDto productDto) {
        Optional<ProductCategoryEntity> productCategoryEntityOptional = productCategoryEntityRepository.findById(productDto.getPcno());
        if(productCategoryEntityOptional.isPresent()) {
            ProductCategoryEntity productCategoryEntity = productCategoryEntityOptional.get();
            productCategoryEntity.setPcname(productDto.getPcname());
            return true;
        }
        return false;
    }

    @Transactional // 4. 상품 카테고리 삭제
    public boolean deleteCategory(int pcno) {
        Optional<ProductCategoryEntity> productCategoryEntityOptional = productCategoryEntityRepository.findById(pcno);
        if(productCategoryEntityOptional.isPresent()) {
            productCategoryEntityRepository.deleteById(pcno);
            return true;
        }
        return false;
    }
}
