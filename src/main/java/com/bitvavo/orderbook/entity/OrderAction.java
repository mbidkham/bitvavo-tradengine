package com.bitvavo.orderbook.entity;

public enum OrderAction {
    BID(),
    ASK();

    public static OrderAction actionFactory(String inputValue) {
        switch (inputValue){
            case "B":
                return BID;
            case "S":
                return ASK;
            default:
                throw new IllegalArgumentException("Incompatible order type");
        }
    }
}
