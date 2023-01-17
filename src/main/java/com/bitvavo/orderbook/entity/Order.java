package com.bitvavo.orderbook.entity;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @NonNull
    private String orderId;
    @NonNull
    private OrderAction side;
    @NonNull
    private Integer price;
    @NonNull
    private int volume;
    private Instant timestamp;


}
