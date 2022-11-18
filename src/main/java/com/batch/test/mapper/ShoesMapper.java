package com.batch.test.mapper;


import com.batch.test.dto.Shoes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoesMapper {

    List<Shoes> selectShoesList();

    void insertShoes();
}
