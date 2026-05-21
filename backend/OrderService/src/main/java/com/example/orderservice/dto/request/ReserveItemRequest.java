package com.example.orderservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveItemRequest {

    private Long productId;
    private int quantity;
}
