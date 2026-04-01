package com.ryan.orders.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos_tb")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PedidoStatus status;

    @Column(name = "valor_total")
    private Double valorTotal;

    @Column(name = "user_id")
    private Long usuarioId;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> itens = new ArrayList<>();

    public void addItem(OrderItem item) {
        itens.add(item);
        item.setPedido(this);
    }
}

