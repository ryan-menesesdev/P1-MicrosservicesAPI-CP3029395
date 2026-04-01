package com.ryan.orders.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service", url = "${inventory.service.url}")
public interface InventoryFeignClient {

    @GetMapping("/inventory/{productId}/check")
    boolean checkAvailability(@PathVariable("productId") Long productId, @RequestParam("quantity") Integer quantity);
}
