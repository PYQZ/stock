package com.itheima.stock.service.impl;
import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.stock.mapper.*;
import com.itheima.stock.pojo.domain.*;
import com.itheima.stock.pojo.vo.StockInfoConfig;
import com.itheima.stock.service.StockService;
import com.itheima.stock.utils.DateTimeUtil;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author by itheima
 * @Date 2021/12/19
 * @Description
 */
@Service("stockService")
@Slf4j
public class StockServiceImpl implements StockService {

    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private Cache<String,Object> caffeineCache;
    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    @Override
    public R<List<InnerMarketDomain>> getInnerMarketInfo() {
        R<List<InnerMarketDomain>> result = (R<List<InnerMarketDomain>>)caffeineCache.get("innerMarketKey",key->{
            List<String> inners = stockInfoConfig.getInner();
            //2.获取最近股票交易日期
            Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
            //TODO mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
            lastDate=DateTime.parse("2022-01-02 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            //3.将获取的java Date传入接口
            List<InnerMarketDomain> list= stockMarketIndexInfoMapper.getInnerMarketInfo(inners,lastDate);
            //4.返回查询结果
            return R.ok(list);
        });
        return result;
    }
    @Override
    public R<List<OuterMarketDomain>> getOuterMarketInfo() {
        R<List<OuterMarketDomain>> result = (R<List<OuterMarketDomain>>) caffeineCache.get("outerMarketKey", key -> {
            List<String> outers = stockInfoConfig.getOuter();
            Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
            //TODO mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
            lastDate = DateTime.parse("2021-12-01 10:57:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            List<OuterMarketDomain> list = stockOuterMarketIndexInfoMapper.getOuterMarketInfo(outers, lastDate);
            return R.ok(list);
        });
        return result;
    }

    @Override
    public R<List<Map<String, String>>> getFuzzyStockInfo(String searchStr) {
        List<Map<String, String>> stockFuzzyInfoList = stockBusinessMapper.getFuzzyInfoByCode(searchStr);
        return R.ok(stockFuzzyInfoList);
    }
    @Override
    public R<List<Map<String, Object>>> getLiveTradingPipeline(String code) {
        List<Map<String, Object>> data = stockRtInfoMapper.getLiveTradingPipelineByCode(code);
        return R.ok(data);
    }

    @Override
    public R<StockDescriptionDomain> getStockDescription(String code) {
        StockDescriptionDomain stockDescriptionInfoList = stockBusinessMapper.getStockDescriptionInfoByCode(code);
        return R.ok(stockDescriptionInfoList);
    }

    @Override
    public R<List<Stock4EvrWeekDomain>> getStockWeekScreenDkLine(String stockCode) {
        DateTime lastDate4Stock = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date curDate = lastDate4Stock.toDate();
        curDate=DateTime.parse("2021-12-30 14:47:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Stock4EvrWeekDomain> data = stockRtInfoMapper.getStockWeekInfoByCode(curDate,stockCode);
        return R.ok(data);
    }

    @Override
    public R<StockLatestTSDomain> getStockLatestTSInfo(String code) {
        StockLatestTSDomain data = stockRtInfoMapper.getStockLatestTS(code);
        return R.ok(data);
    }


    @Override
    public R<List<StockBlockDomain>> sectorAllLimit() {
        Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        lastDate=DateTime.parse("2021-12-21 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<StockBlockDomain> infos = stockBlockRtInfoMapper.sectorAllLimit(lastDate);
        if(CollectionUtils.isEmpty(infos)){
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());

        }
        return R.ok(infos);
    }

    @Override
    public R<PageResult> getStockPageInfo(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate= DateTime.parse("2022-06-07 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<StockUpdownDomain> infos= stockRtInfoMapper.getNewestStockInfo(curDate);
        if(CollectionUtils.isEmpty(infos)){
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        PageResult<StockUpdownDomain> stockUpdownDomainPageResult = new PageResult<>(new PageInfo<>(infos));
        return R.ok(stockUpdownDomainPageResult);
    }

    @Override
    public R<List<StockUpdownDomain>> getStockFourInfo() {


        PageHelper.startPage(1,4);
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate= DateTime.parse("2022-06-07 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<StockUpdownDomain> infos= stockRtInfoMapper.getNewestStockInfo(curDate);
        if(CollectionUtils.isEmpty(infos)){
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }

        return R.ok(infos);
    }

    @Override
    public R<Map> getStockUpdownCount() {
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date curTime = curDateTime.toDate();
        //TODO
        curTime= DateTime.parse("2022-01-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        DateTime openDate = DateTimeUtil.getOpenDate(curDateTime);
        Date openTime = openDate.toDate();
        openTime= DateTime.parse("2022-01-06 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Map> upCounts = stockRtInfoMapper.getStockUpdownCount(openTime,curTime,1);
        List<Map> dwCounts = stockRtInfoMapper.getStockUpdownCount(openTime,curTime,0);
        HashMap<String, List> mapInfo = new HashMap<>();
        mapInfo.put("upList",upCounts);
        mapInfo.put("dwList",dwCounts);
        return R.ok(mapInfo);
    }

    @Override
    public void exportStockUpDownInfo(Integer page, Integer pageSize, HttpServletResponse response) {
        R<PageResult> r = this.getStockPageInfo(page, pageSize);
        List rows = r.getData().getRows();
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ns-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            String fileName = URLEncoder.encode("股票信息表", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), StockUpdownDomain.class).sheet("股票涨跌信息").doWrite(rows);
        } catch (IOException e) {
            log.error("当前页码：{}，每页大小：{},当前时间：{}，异常信息：{}",page,pageSize,DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),e.getMessage());
            response.setContentType("application/json");
            R<Object> error = R.error(ResponseCode.ERROR);
            try {
                String jsonData = new ObjectMapper().writeValueAsString(error);
                response.getWriter().write(jsonData);
            } catch (IOException ex) {
                log.error("exportStockUpDownInfo:响应错误信息失败");
            }


        }
    }

    @Override
    public R<Map<String, List>> getComparedStockTradeAmt() {
        DateTime tEndDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        tEndDateTime=DateTime.parse("2022-01-03 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date tEndDate = tEndDateTime.toDate();
        Date tStartDate = DateTimeUtil.getOpenDate(tEndDateTime).toDate();
        DateTime preTEndDateTime = DateTimeUtil.getPreviousTradingDay(tEndDateTime);
        preTEndDateTime=DateTime.parse("2022-01-02 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date preTEndDate = preTEndDateTime.toDate();
        Date tPreStartDate = DateTimeUtil.getOpenDate(preTEndDateTime).toDate();
        List<Map> tData = stockMarketIndexInfoMapper.gerSumAmtInfo(tStartDate,tEndDate,stockInfoConfig.getInner());
        List<Map> preTData = stockMarketIndexInfoMapper.gerSumAmtInfo(tPreStartDate,preTEndDate,stockInfoConfig.getInner());
        HashMap<String, List> mapInfo = new HashMap<>();
        mapInfo.put("amtList",tData);
        mapInfo.put("yesAmtList",preTData);
        return R.ok(mapInfo);
    }

    @Override
    public R<Map> getIncreaseRangeInfo() {
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        curDateTime=DateTime.parse("2022-01-06 09:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date curDate = curDateTime.toDate();
        List<Map> infos = stockRtInfoMapper.getIncreaseRangeInfoByDate(curDate);
        List<String> upDownRange = stockInfoConfig.getUpDownRange();
        List<Map> allInfos = new ArrayList<>();
        for (String title : upDownRange) {
            Map tmp = null;
            for (Map info : infos) {
                if(info.containsValue(title)){
                    tmp = info;
                    break;
                }
            }
            if(tmp == null){
                tmp = new HashMap<>();
                tmp.put("title",title);
                tmp.put("count",0);
                infos.add(tmp);
            }
            allInfos.add(tmp);
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("time",curDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        data.put("infos",allInfos);
        return R.ok(data);
    }

    @Override
    public R<List<Stock4MinuteDomain>> getStockScreenTimeSharing(String stockCode) {
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date endDate = endDateTime.toDate();
        endDate=DateTime.parse("2021-12-30 14:47:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date openDate = DateTimeUtil.getOpenDate(endDateTime).toDate();
        openDate=DateTime.parse("2021-12-30 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Stock4MinuteDomain> data=stockRtInfoMapper.getStock4MinuteInfo(openDate,endDate,stockCode);
        //判断非空处理
        if (CollectionUtils.isEmpty(data)) {
            data=new ArrayList<>();
        }
        //3.返回响应数据
        return R.ok(data);
    }

    @Override
    public R<List<Stock4EvrDayDomain>> getStockScreenDkLine(String stockCode) {
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        endDateTime=DateTime.parse("2022-07-22 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = endDateTime.toDate();
        DateTime startDateTime = endDateTime.minusMonths (3);
        startDateTime=DateTime.parse("2022-01-01 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date startDate = startDateTime.toDate();
//        List<Stock4EvrDayDomain> dkLineData = stockRtInfoMapper.getStock4DkLine(startDate,endDate,stockCode);
        List<Date> closeDates = stockRtInfoMapper.getCloseDates(stockCode, startDate, endDate);
        List<Stock4EvrDayDomain> dkLineData = stockRtInfoMapper.getStockCreenDkLineData(stockCode, closeDates);
        return R.ok(dkLineData);
    }



}