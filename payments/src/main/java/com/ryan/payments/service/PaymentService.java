package com.ryan.payments.service;

import com.ryan.payments.controller.handler.exception.PaymentNotFoundException;
import com.ryan.payments.dto.PaymentRequest;
import com.ryan.payments.dto.PaymentResponse;
import com.ryan.payments.model.Payment;
import com.ryan.payments.model.PaymentStatus;
import com.ryan.payments.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    @Transactional()
    public List<PaymentResponse> findAll() {
        return repository.findAll().stream()
            .map(p -> new PaymentResponse(p.getId(), p.getPedidoId(), p.getStatus().name(), p.getValor()))
            .toList();
    }

    @Transactional()
    public PaymentResponse findById(Long id) {
        Payment payment = repository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Pagamento não encontrado com id: " + id));
        return new PaymentResponse(payment.getId(), payment.getPedidoId(), payment.getStatus().name(), payment.getValor());
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
                savedPayment.getPedidoId(),
                savedPayment.getStatus().name(),
                savedPayment.getValor()
        );
    }
}