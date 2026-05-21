package com.example.productservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveInventoryRequest {

    private Long productId;
    private int quantity;
}
