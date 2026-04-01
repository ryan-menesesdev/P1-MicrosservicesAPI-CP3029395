package com.ryan.payments.controller;

import com.ryan.payments.dto.InventoryRequest;
import com.ryan.payments.dto.InventoryResponse;
import com.ryan.payments.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable Long productId) {
        return ResponseEntity.ok(service.findByProductId(productId));
    }

    @GetMapping("/{productId}/check")
    public ResponseEntity<Boolean> checkAvailability(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(service.checkAvailability(productId, quantity));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<InventoryResponse> updateInventory(
            @PathVariable Long productId,
            @RequestBody @Valid InventoryRequest request) {
        return ResponseEntity.ok(service.deductInventory(productId, request.quantidade()));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<InventoryResponse> initializeStock(
            @PathVariable Long productId,
            @RequestBody @Valid InventoryRequest request) {

        InventoryResponse response = service.initializeStock(productId, request.quantidade());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
