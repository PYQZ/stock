package com.itheima.stock.job;

import com.itheima.stock.service.StockTimerTaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class StockJob {
    @Autowired
    private StockTimerTaskService stockTimerTaskService;
    @XxlJob("stockJobHandler")
    public void demoJobHandler() throws Exception {
        System.out.println("当前时间点："+ DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
    }
    @XxlJob("getInnerMarketInfo")
    public void getStockInfos(){
        stockTimerTaskService.getInnerMarketInfo();
    }
    @XxlJob("getStockRtIndex")
    public void getStockRtIndex(){
        stockTimerTaskService.getStockRtIndex();
    }
    @XxlJob("getStockSectorRtIndex")
    public void getStockSectorRtIndex(){
        stockTimerTaskService.getStockSectorRtIndex();
    }

}
