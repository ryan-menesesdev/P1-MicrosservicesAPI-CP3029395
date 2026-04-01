package com.ryan.orders.service;

import com.ryan.orders.dto.*;
import com.ryan.orders.feign.CatalogFeignClient;
import com.ryan.orders.feign.InventoryFeignClient;
import com.ryan.orders.feign.PaymentFeignClient;
import com.ryan.orders.model.Order;
import com.ryan.orders.model.OrderItem;
import com.ryan.orders.model.PedidoStatus;
import com.ryan.orders.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
    public OrderResponse create(OrderRequest request) {
        Order order = Order.builder()
            .usuarioId(request.usuarioId())
            .status(PedidoStatus.CRIADO)
            .build();

        double totalValue = 0.0;

        for (var itemRequest : request.itens()) {
            ProductResponse product = catalogClient.getProductById(itemRequest.produtoId());

            boolean isAvailable = inventoryClient.checkAvailability(itemRequest.produtoId(), itemRequest.quantidade());
            if (!isAvailable) {
                throw new RuntimeException("Product out of stock: " + itemRequest.produtoId());
            }

            OrderItem orderItem = OrderItem.builder()
                    .produtoId(product.id())
                    .quantidade(itemRequest.quantidade())
                    .precoCompra(product.price())
                    .build();

            order.addItem(orderItem);

            double itemTotal = product.price() * itemRequest.quantidade();
            totalValue += itemTotal;
        }

        order.setValorTotal(totalValue);
        Order savedOrder = repository.save(order);

        PaymentRequest paymentRequest = new PaymentRequest(savedOrder.getId(), savedOrder.getValorTotal(), request.valorPago());

        try {
            PaymentResponse paymentResponse = paymentClient.processPayment(paymentRequest);
            if ("APROVADO".equalsIgnoreCase(paymentResponse.status())) {
                savedOrder.setStatus(PedidoStatus.CONCLUIDO);
            } else {
                savedOrder.setStatus(PedidoStatus.FALHA);
            }
        } catch (Exception e) {
            savedOrder.setStatus(PedidoStatus.FALHA);
        }

        return mapToResponse(repository.save(savedOrder));
    }

    @Transactional()
    public OrderResponse findById(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToResponse(order);
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