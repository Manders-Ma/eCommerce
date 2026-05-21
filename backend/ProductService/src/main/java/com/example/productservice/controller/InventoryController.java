package com.example.productservice.controller;

import com.example.productservice.dto.request.ProductCreation;
import com.example.productservice.dto.request.ReserveItemRequest;
import com.example.productservice.dto.response.MessageResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
  
  @Autowired
  private InventoryService inventoryService;

  @PatchMapping
  public ResponseEntity<MessageResponse> updateProduct(@RequestBody Product product) {
    ResponseEntity<MessageResponse> response = null;

    try {
      inventoryService.updateProduct(product);
      response = ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("商品更改模式: 已成功更改商品資訊。"));
    } catch (Exception e) {
      response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("商品更改模式: 更改商品資訊失敗。"));
      return response;
    }


    return response;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<MessageResponse> deleteProductById(@PathVariable(name = "id") Long id) {
    ResponseEntity<MessageResponse> response = null;

    try {
      inventoryService.deleteProductById(id);
      response = ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("成功刪除商品。"));
    } catch (Exception e) {
      response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("刪除商品失敗。"));
      return response;
    }

    return response;
  }

  @PostMapping("/reserve")
  public ResponseEntity<MessageResponse> reserveInventory(@RequestBody Set<ReserveItemRequest> items) {
    try {
      inventoryService.reserveInventory(items);
      return ResponseEntity.ok().build();
    } catch (com.example.productservice.exception.InsufficientInventoryException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(e.getMessage()));
    }
  }

  @PostMapping
  public ResponseEntity<MessageResponse> saveProduct(@RequestBody ProductCreation productCreation) {
    ResponseEntity<MessageResponse> response = null;
    try {
      inventoryService.saveProduct(productCreation);
      response = ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("商品新增模式: 已成功新增商品。"));
    } catch (Exception e) {
      response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("商品新增模式: 請確認輸入格式是否正確。"));
      return response;
    }
    
    return response;
  }
}
