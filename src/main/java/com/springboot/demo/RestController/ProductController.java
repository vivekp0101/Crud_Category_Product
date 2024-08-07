package com.springboot.demo.RestController;
import org.apache.catalina.startup.Catalina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.springboot.demo.model.Category;
import com.springboot.demo.model.Product;
import com.springboot.demo.repository.CategoryRepository;
import com.springboot.demo.repository.ProductRepository;

import java.util.Optional;




 

@Controller
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(productRepository.findAll(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        Optional<Category> categoryOpt = categoryRepository.findById(product.getCategory().getId());
        if (categoryOpt.isPresent()) {
            product.setCategory(categoryOpt.get());
            return new ResponseEntity<>(productRepository.save(product), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Category not found", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            return new ResponseEntity<>(productOpt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            Optional<Category> categoryOpt = categoryRepository.findById(productDetails.getCategory().getId());
            if (categoryOpt.isPresent()) {
                product.setCategory(categoryOpt.get());
                return new ResponseEntity<>(productRepository.save(product), HttpStatus.OK);
            }
            return new ResponseEntity<>("Category not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
    }
}