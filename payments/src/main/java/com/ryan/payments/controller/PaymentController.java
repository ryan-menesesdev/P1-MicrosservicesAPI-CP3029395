package com.ryan.payments.controller;

import com.ryan.payments.dto.PaymentRequest;
import com.ryan.payments.dto.PaymentResponse;
import com.ryan.payments.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody @Valid PaymentRequest request) {
        PaymentResponse response = service.process(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
