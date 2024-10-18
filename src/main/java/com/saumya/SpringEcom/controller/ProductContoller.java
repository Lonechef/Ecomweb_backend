package com.saumya.SpringEcom.controller;

import com.saumya.SpringEcom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.saumya.SpringEcom.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductContoller {

    @Autowired
    private ProductService productService;
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(){
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        Product product = productService.getProductById(id);
        if(product!=null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
       Product product = productService.getProductById(productId);
       if(product!=null) {
           return new ResponseEntity<>(product.getImageData(), HttpStatus.OK);
       }
       else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
    }
    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(
            @RequestPart("product") Product product,
            @RequestPart("imageFile") MultipartFile imageFile){
        Product savedProduct = null;
        try {
            savedProduct = productService.addProduct(product,imageFile);
            return new ResponseEntity<>(savedProduct,HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }
        @PutMapping("/product/{id}")
        public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product,@RequestPart MultipartFile imageFile){
            Product updatedProduct = null;
            try{
                updatedProduct=productService.updateProduct(product,imageFile);
                return new ResponseEntity<>("Updated",HttpStatus.OK);
            }
            catch (IOException e){
                return  new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
            }
        }
        @DeleteMapping("/product/{id}")
        public ResponseEntity<String> deleteProduct(@PathVariable int id){
            //So before deleting the product we will get that product to know if the product exisists or not
            Product product = productService.getProductById(id);
            if(product!=null) {
                productService.deleteProduct(id);
                //And if nothing goes wrong we will pass the Response Entity
                return new ResponseEntity<>("Deleted", HttpStatus.OK);
            }
            else{
                return  new ResponseEntity<>("Product Not Found",HttpStatus.NOT_FOUND);
            }
        }
        @GetMapping("/product/search")
        //So now in this we will be searching on the basis of the Keyword
        public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        List<Product> products = productService.searchProducts(keyword);
        //And now whatever products we get on the basis of the keywords
            return new ResponseEntity<>(products,HttpStatus.OK);
        }


}
