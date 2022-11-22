package com.batch.test.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@NoArgsConstructor
@Getter
public class UserGrade {

    private long userId;
    private String grade;



    public InsertUserPoint insertUserPoint(long userId, String grade){
        int point=0;
        if(grade.equals("BASIC")){
            point= 1000;
        }else if(grade.equals("SILVER")){
            point=2000;
        } else if (grade.equals("GOLD")) {
            point=3000;
        }else{
            point=5000;
        }


        return InsertUserPoint.builder()
                .pointInfo("등급별 포인트지급")
                .userId(userId)
                .point(point)
                .build();
    }
}
