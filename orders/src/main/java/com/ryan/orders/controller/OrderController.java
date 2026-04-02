package com.ryan.orders.controller;

import com.ryan.orders.dto.order.ConfirmPaymentRequest;
import com.ryan.orders.dto.order.OrderRequest;
import com.ryan.orders.dto.order.OrderResponse;
import com.ryan.orders.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.findOrdersByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createPending(@RequestBody @Valid OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @PostMapping("/{orderId}/confirm-payment")
    public ResponseEntity<OrderResponse> confirmPayment(
            @PathVariable Long orderId,
            @RequestBody @Valid ConfirmPaymentRequest request
    ) {
        return ResponseEntity.ok(orderService.confirmPayment(orderId, request.valorPago()));
    }
}
