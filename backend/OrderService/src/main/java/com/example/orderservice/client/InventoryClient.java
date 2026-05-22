package com.example.orderservice.client;

import com.example.orderservice.dto.request.ReserveItemRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(name = "Gateway")
public interface InventoryClient {

    @PostMapping("/internal/inventory/reserve")
    void reserveInventory(@RequestBody Set<ReserveItemRequest> items);
}
