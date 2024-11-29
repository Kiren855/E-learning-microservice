package com.sunny.microservices.order.dto.response;

import com.sunny.microservices.order.entity.WishList;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishListResponse {
        List<AWishResponse> wishList;
}
