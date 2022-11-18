package com.batch.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class Shoes {
    private int id;
    private String shoesBrand;
    private String shoesName;
    private int shoesSize;

    public Shoes(int id, String shoesBrand, String shoesName, int shoesSize) {
        this.id = id;
        this.shoesBrand = shoesBrand;
        this.shoesName = shoesName;
        this.shoesSize = shoesSize;
    }
}
