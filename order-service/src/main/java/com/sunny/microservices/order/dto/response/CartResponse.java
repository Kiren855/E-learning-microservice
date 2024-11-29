package com.sunny.microservices.order.dto.response;

import com.sunny.microservices.order.entity.Cart;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    List<ACartResponse> cart;
}
