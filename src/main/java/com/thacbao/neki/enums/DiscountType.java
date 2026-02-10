package com.thacbao.neki.enums;

import lombok.Getter;

@Getter
public enum DiscountType {
    AMOUNT("amount"),
    SHIP("ship");

    private final String value;

    public static DiscountType fromValue(String value) {
        for(DiscountType item : values()) {
            if (item.value.equals(value.toUpperCase())) {
                return item;
            }
        }

        throw new IllegalArgumentException("Invalid value: " + value);
    }

    DiscountType(String value) {
        this.value = value;
    }
}
