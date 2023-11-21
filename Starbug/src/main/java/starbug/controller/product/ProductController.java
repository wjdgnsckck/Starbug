package starbug.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import starbug.model.dto.product.ProductDto;
import starbug.service.product.ProductService;
import starbug.service.resoures.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService; // 상품 서비스

    @Autowired
    private InventoryService inventoryService; // 재고 서비스

    // ----------------------------- Product --------------------------------

    // 1. 전체/특정메뉴 출력 기능
    @GetMapping("/get")
    public List<ProductDto> getCategoryProductDto(@RequestParam int pcno, @RequestParam String keyword, @RequestParam String searchSelect) {
        System.out.println("ProductController.getCategoryProductDto");
        return productService.getProduct(pcno, keyword, searchSelect);
    }

    // 2. 메뉴 등록 기능
    @PostMapping("/post")
    public boolean registerProduct(@RequestBody ProductDto productDto) {
        System.out.println("productDto = " + productDto);
        return productService.registerProduct(productDto);
    }

    // 3. 메뉴 수정 기능
    @PutMapping("/put")
    public boolean updateProduct(@RequestBody ProductDto productDto) {
        System.out.println("ProductController.updateProduct");
        System.out.println("productDto = " + productDto);
        return productService.updateProduct(productDto);
    }

    // 4. 메뉴 삭제 기능
    @DeleteMapping("/delete")
    public boolean deleteProduct(@RequestParam int pno) {
        System.out.println("pno = " + pno);
        return productService.deleteProduct(pno);
    }

    // 5. 이벤트 상품 설정 기능
    @PutMapping("/event")
    public boolean changeEventProduct(@RequestParam int pno, @RequestParam int pevent, @RequestParam boolean isEvent) {
        System.out.println("pno = " + pno);
        System.out.println("pevent = " + pevent);
        System.out.println("inEvent = " + isEvent);
        return productService.changeEventProduct(pno, pevent, isEvent);
    }

    // 6. 상품 품절 기능
    @PostMapping("/get/soldout")
    public List<Integer> selectSoldoutProduct(@RequestBody List<ProductDto> productDtoList) {
        return productService.selectSoldoutProduct(productDtoList);
    }

    // ------------------------------- ProductCategory -----------------------------
    // 1. 전체 상품 카테고리 출력
    @GetMapping("/category/get")
    public List<ProductDto> getCategoryAll(){
        return productService.getCategory();
    }
    // 2. 상품 카테고리 추가
    @PostMapping("/category/post")
    public boolean registerCategory(@RequestBody ProductDto productDto) { return productService.registerCategory(productDto);}
    // 3. 상품 카테고리 수정
    @PutMapping("/category/put")
    public boolean updateCategory(@RequestBody ProductDto productDto) { return productService.updateCategory(productDto);}
    // 4. 상품 카테고리 삭제
    @DeleteMapping("/category/delete")
    public boolean deleteCategory(@RequestParam int pcno) { return productService.deleteCategory(pcno);}

}
