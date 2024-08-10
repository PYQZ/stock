package com.itheima.stock.service;

import com.itheima.stock.pojo.domain.*;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface StockService {
    R<List<InnerMarketDomain>> getInnerMarketInfo();

    R<List<StockBlockDomain>> sectorAllLimit();

    R<PageResult> getStockPageInfo(Integer page, Integer pageSize);

//    R<PageResult> getStockFourInfo(Integer page, Integer pageSize);
    R<List<StockUpdownDomain>> getStockFourInfo();

    R<Map> getStockUpdownCount();

    void exportStockUpDownInfo(Integer page, Integer pageSize, HttpServletResponse response);


    R<Map<String, List>> getComparedStockTradeAmt();

    R<Map> getIncreaseRangeInfo();

    R<List<Stock4MinuteDomain>> getStockScreenTimeSharing(String stockCode);

    R<List<Stock4EvrDayDomain>> getStockScreenDkLine(String stockCode);

    R<List<OuterMarketDomain>> getOuterMarketInfo();

    R<List<Map<String, String>>> getFuzzyStockInfo(String searchStr);


    R<StockDescriptionDomain> getStockDescription(String code);

    R<List<Stock4EvrWeekDomain>> getStockWeekScreenDkLine(String stockCode);


    R<StockLatestTSDomain> getStockLatestTSInfo(String code);

    R<List<Map<String, Object>>> getLiveTradingPipeline(String code);
}
