package com.batch.test.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserTotalPrice {


    private long userId;

    private long totalPrice;


    public UpdateUserGrade updateGrade(long userId, long totalPrice){
        String grade = "";
        if(totalPrice<=100000){
            grade="BASIC";
        } else if (totalPrice<=200000) {
            grade="SILVER";
        } else if (totalPrice<=500000) {
            grade="GOLD";
        }else{
            grade="VIP";
        }
        return UpdateUserGrade.builder()
                .userId(userId)
                .grade(grade)
                .build();

    }
}
