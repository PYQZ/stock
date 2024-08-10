package com.itheima.stock.service;

public interface StockTimerTaskService {
    //采集大盘数据
    void getInnerMarketInfo();
    //采集个股数据
    void getStockRtIndex();
    void getStockSectorRtIndex();
    void getOuterMarketInfo();
}
