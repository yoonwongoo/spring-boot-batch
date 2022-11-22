package com.batch.test.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Getter
public class InsertUserPoint {

    public InsertUserPoint(long userId, String pointInfo, int point) {
          this.userId = userId;
          this.pointInfo = pointInfo;
          this.point = point;
      }

    private long userId;
    private String pointInfo;
    private int point;


}
