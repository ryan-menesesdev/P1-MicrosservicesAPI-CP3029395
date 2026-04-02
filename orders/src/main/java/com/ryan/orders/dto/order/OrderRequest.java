package com.ryan.orders.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderRequest(
    @NotNull(message = "O ID do usuário é obrigatório")
    Long usuarioId,

    @NotEmpty(message = "O pedido deve conter pelo menos um item")
    List<@Valid OrderItemRequest> itens
) {}