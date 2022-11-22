package com.batch.test.mapper;

import com.batch.test.dto.UpdateUserGrade;
import com.batch.test.dto.UserGrade;
import com.batch.test.dto.UserTotalPrice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    List<UserTotalPrice> selectUserTotalPrice(Map<String,Object> map);

    void updateUserGrade();

    List<UserGrade> selectUserGrade();
}
