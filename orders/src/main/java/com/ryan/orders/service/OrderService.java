package com.ryan.orders.service;

import com.ryan.orders.controller.handler.exception.InvalidOrderStatusException;
import com.ryan.orders.controller.handler.exception.OrderNotFoundException;
import com.ryan.orders.controller.handler.exception.OutOfStockException;
import com.ryan.orders.controller.handler.exception.PaymentProcessingException;
import com.ryan.orders.dto.*;
import com.ryan.orders.dto.inventory.InventoryRequest;
import com.ryan.orders.dto.order.OrderItemRequest;
import com.ryan.orders.dto.order.OrderItemResponse;
import com.ryan.orders.dto.order.OrderRequest;
import com.ryan.orders.dto.order.OrderResponse;
import com.ryan.orders.dto.payment.PaymentRequest;
import com.ryan.orders.dto.payment.PaymentResponse;
import com.ryan.orders.feign.CatalogFeignClient;
import com.ryan.orders.feign.InventoryFeignClient;
import com.ryan.orders.feign.PaymentFeignClient;
import com.ryan.orders.model.Order;
import com.ryan.orders.model.OrderItem;
import com.ryan.orders.model.PedidoStatus;
import com.ryan.orders.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final CatalogFeignClient catalogClient;
    private final InventoryFeignClient inventoryClient;
    private final PaymentFeignClient paymentClient;

    public OrderService(
            OrderRepository repository,
            CatalogFeignClient catalogClient,
            InventoryFeignClient inventoryClient,
            PaymentFeignClient paymentClient
    ) {
        this.repository = repository;
        this.catalogClient = catalogClient;
        this.inventoryClient = inventoryClient;
        this.paymentClient = paymentClient;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Order order = Order.builder()
                .usuarioId(request.usuarioId())
                .status(PedidoStatus.CRIADO)
                .build();

        double totalValue = 0.0;

        for (OrderItemRequest itemRequest : request.itens()) {
            ProductResponse product = catalogClient.getProductById(itemRequest.produtoId());

            boolean isAvailable = inventoryClient.checkAvailability(itemRequest.produtoId(), itemRequest.quantidade());
            if (!isAvailable) {
                throw new OutOfStockException("Produto fora de estoque: " + itemRequest.produtoId());
            }

            OrderItem orderItem = OrderItem.builder()
                    .produtoId(product.id())
                    .quantidade(itemRequest.quantidade())
                    .precoCompra(product.valorIndividual())
                    .build();

            order.addItem(orderItem);
            double itemTotal = product.valorIndividual() * itemRequest.quantidade();
            totalValue += itemTotal;
        }

        order.setValorTotal(totalValue);
        Order savedOrder = repository.save(order);

        return mapToResponse(savedOrder);
    }

    @Transactional
    public OrderResponse confirmPayment(Long orderId, Double valorPago) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com id: " + orderId));

        if (!order.getStatus().equals(PedidoStatus.CRIADO)) {
            throw new InvalidOrderStatusException("Pedido não está em estado CRIADO. Status atual: " + order.getStatus());
        }

        PaymentRequest paymentRequest = new PaymentRequest(
                order.getId(),
                order.getValorTotal(),
                valorPago
        );

        try {
            PaymentResponse paymentResponse = paymentClient.processPayment(paymentRequest);

            if ("APROVADO".equalsIgnoreCase(paymentResponse.status())) {
                order.setStatus(PedidoStatus.PAGO);

                for (OrderItem item : order.getItens()) {
                    InventoryRequest invRequest = new InventoryRequest(item.getQuantidade());
                    inventoryClient.deductInventory(item.getProdutoId(), invRequest);
                }
            } else {
                order.setStatus(PedidoStatus.CANCELADO);
            }
        } catch (Exception e) {
            order.setStatus(PedidoStatus.CANCELADO);
            repository.save(order);
            throw new PaymentProcessingException("Erro ao processar pagamento");
        }

        return mapToResponse(repository.save(order));
    }

    @Transactional
    public OrderResponse findById(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com id: " + id));
        return mapToResponse(order);
    }

    @Transactional
    public List<OrderResponse> findOrdersByUserId(Long userId) {
        List<Order> orders = repository.findByUsuarioId(userId);
        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public List<OrderResponse> findAll() {
        List<Order> orders = repository.findAll();
        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private OrderResponse mapToResponse(Order order) {
        var itemsResponse = order.getItens().stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getProdutoId(),
                        item.getQuantidade(),
                        item.getPrecoCompra()
                )).toList();

        return new OrderResponse(
                order.getId(),
                order.getStatus().name(),
                order.getValorTotal(),
                order.getUsuarioId(),
                itemsResponse
        );
    }
}