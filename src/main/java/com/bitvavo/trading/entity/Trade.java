package com.bitvavo.trading.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * If 2 orders can match to each other Based on @see com.bitvavo.processors.MatchingEngine logic,
 * Trade will happen.
 */
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class Trade {
    private String takerId;
    private String makerId;
    private int price;
    private int volume;
    @Override
    public String toString() {
        return String.format("trade %s %s %s %s", this.takerId, this.makerId, this.price, this.volume);
    }
}
