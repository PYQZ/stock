package com.itheima.stock.controller;

import com.itheima.stock.pojo.domain.*;
import com.itheima.stock.service.StockService;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quot")
public class StockController {

    @Autowired
    private StockService stockService;
    //查询大盘指数
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getInnerMarketInfo(){
        return stockService.getInnerMarketInfo();
    }
    //查询外盘指数
    @GetMapping("/external/index")
    public R<List<OuterMarketDomain>> getOuterMarketInfo(){
        return stockService.getOuterMarketInfo();
    }
    //股票搜索模糊查询
    @GetMapping("/stock/search")
    public R<List<Map<String, String>>> getFuzzyStockInfo(@RequestParam("searchStr") String searchStr){
        return stockService.getFuzzyStockInfo(searchStr);
    }
    //个股实时交易流水查询功能
    @GetMapping("/stock/screen/second")
    public R<List<Map<String,Object>>> getLiveTradingPipeline(@RequestParam("code") String code){
        return stockService.getLiveTradingPipeline(code);
    }

    //个股主营业务查询
    @GetMapping("/stock/describe")
    public R<StockDescriptionDomain> getStockDescription(@RequestParam("code") String code){
        return stockService.getStockDescription(code);
    }
    //个股周K线详情功能

    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> sectorAll(){
        return stockService.sectorAllLimit();
    }
    @GetMapping("/stock/all")
    public R<PageResult> getStockPageInfo(@RequestParam(name="page",required = false,defaultValue = "1") Integer page,@RequestParam(name = "pageSize",required = false,defaultValue = "20") Integer pageSize){
        return stockService.getStockPageInfo(page,pageSize);
    }

    @GetMapping("/stock/increase")
    public R<List<StockUpdownDomain>> getStockFourInfo(){
            return stockService.getStockFourInfo();
    }
    @GetMapping("/stock/updown/count")
    public R<Map> getStockUpdownCount(){
        return stockService.getStockUpdownCount();
    }
    @GetMapping("/stock/export")
    public void exportStockUpDownInfo(@RequestParam(name="page",required = false,defaultValue = "1") Integer page,
                                      @RequestParam(name = "pageSize",required = false,defaultValue = "20") Integer pageSize,
                                      HttpServletResponse response){
        stockService.exportStockUpDownInfo(page,pageSize,response);
    }
    @GetMapping("/stock/tradeAmt")
    public R<Map<String,List>> getComparedStockTradeAmt(){
        return stockService.getComparedStockTradeAmt();
    }
    @GetMapping("/stock/updown")
    public R<Map> getIncreaseRangeInfo(){
        return stockService.getIncreaseRangeInfo();
    }
    @GetMapping("/stock/screen/time-sharing")
    public R<List<Stock4MinuteDomain>> getStockScreenTimeSharing(@RequestParam(value = "code",required = true) String stockCode){
        return stockService.getStockScreenTimeSharing(stockCode);
    }
    @GetMapping("/stock/screen/dkline")
    public R<List<Stock4EvrDayDomain>> getStockScreenDkLine(@RequestParam(value = "code",required = true) String stockCode){
        return stockService.getStockScreenDkLine(stockCode);
    }
    @GetMapping("/stock/screen/weekkline")
    public R<List<Stock4EvrWeekDomain>> getStockWeekScreenDkLine(@RequestParam(value = "stockCode",required = true) String stockCode){
        return stockService.getStockWeekScreenDkLine(stockCode);
    }
    //个股最新分时行情功能
    @GetMapping("/stock/screen/second/detail")
    public R<StockLatestTSDomain> getStockLatestTSInfo(@RequestParam(value = "code",required = true) String code){
        return stockService.getStockLatestTSInfo(code);
    }


}
