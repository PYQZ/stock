package com.itheima.stock;

import com.google.common.collect.Lists;
import com.itheima.stock.mapper.StockBusinessMapper;
import com.itheima.stock.service.StockTimerTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class TestMapper {
    @Autowired
    private StockBusinessMapper stockBusinessMapper;
    @Autowired
    private StockTimerTaskService stockTimerTaskService;
    @Test
    public void test01(){
        List<String> allCodes = stockBusinessMapper.getAllStockIds();
        System.out.println(allCodes);
        allCodes = allCodes.stream().map(code->code.startsWith("6")?"sh"+code:"sz"+code).collect(Collectors.toList());
        System.out.println("newCodes:"+allCodes);
        Lists.partition(allCodes, 15).forEach(codes->{
            System.out.println(codes);
        });

    }
    @Test
    public void test02(){
        stockTimerTaskService.getStockRtIndex();
    }
    @Test
    public void test03(){
        stockTimerTaskService.getStockSectorRtIndex();
    }
    @Test
    public void test04(){
        stockTimerTaskService.getOuterMarketInfo();
    }
}
