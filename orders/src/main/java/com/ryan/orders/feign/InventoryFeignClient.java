package com.ryan.orders.feign;

import com.ryan.orders.dto.InventoryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service", url = "${inventory.service.url}")
public interface InventoryFeignClient {

    @GetMapping("/inventory/{productId}/check")
    boolean checkAvailability(@PathVariable("productId") Long productId, @RequestParam("quantity") Integer quantity);

    @PutMapping("/inventory/{productId}")
    void deductInventory(@PathVariable("productId") Long productId, @RequestBody InventoryRequest request);
}
