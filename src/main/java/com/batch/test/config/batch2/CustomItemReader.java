package com.batch.test.config.batch2;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;

//list처리

public class CustomItemReader<T> implements ItemReader {

    private  List<T> itemList;

    public CustomItemReader(List<T> itemList) {
        this.itemList = new ArrayList<>();
    }

    @Override
    public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if(!itemList.isEmpty()){
            return itemList.remove(0);
        }

        return null; //null return 하면 중지 왜냐 더 이상 값이 없기에
    }
}
