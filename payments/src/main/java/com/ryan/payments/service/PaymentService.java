package com.ryan.payments.service;

import com.ryan.payments.dto.PaymentRequest;
import com.ryan.payments.dto.PaymentResponse;
import com.ryan.payments.model.Payment;
import com.ryan.payments.model.PaymentStatus;
import com.ryan.payments.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public PaymentResponse process(PaymentRequest request) {

        PaymentStatus evaluatedStatus = request.valorPago().compareTo(request.valorPedido()) >= 0
                ? PaymentStatus.APROVADO
                : PaymentStatus.RECUSADO;

        Payment payment = Payment.builder()
                .pedidoId(request.pedidoId())
                .valor(request.valorPago())
                .status(evaluatedStatus)
                .build();

        Payment savedPayment = repository.save(payment);

        return new PaymentResponse(
                savedPayment.getId(),
                savedPayment.getStatus().name()
        );
    }
}