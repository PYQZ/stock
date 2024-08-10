package com.itheima.stock.service.impl;

import com.google.common.collect.Lists;
import com.itheima.stock.constant.ParseType;
import com.itheima.stock.mapper.*;
import com.itheima.stock.pojo.entity.StockBlockRtInfo;
import com.itheima.stock.pojo.entity.StockMarketIndexInfo;
import com.itheima.stock.pojo.entity.StockRtInfo;
import com.itheima.stock.pojo.vo.StockInfoConfig;
import com.itheima.stock.service.StockTimerTaskService;
import com.itheima.stock.utils.DateTimeUtil;
import com.itheima.stock.utils.IdWorker;
import com.itheima.stock.utils.ParserStockInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Service("stockTimerTaskService")
@Slf4j

public class StockTimerTaskServiceImpl implements StockTimerTaskService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private ParserStockInfoUtil parserStockInfoUtil;
    @Autowired
    private StockBusinessMapper stockBusinessMapper;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;
    private HttpEntity<Object> httpEntity;
    @Override
    public void getInnerMarketInfo() {
        String url = stockInfoConfig.getMarketUrl()+String.join(",",stockInfoConfig.getInner());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        if(statusCodeValue!=200){
            log.error("当前时间：{},采集数据失败，http状态码：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),statusCodeValue);
            return;
        }
        String jsData = responseEntity.getBody();
        log.info("当前时间：{},采集数据成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        String reg = "var hq_str_(.+)=\"(.+)\";";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(jsData);
        List<StockMarketIndexInfo> entities = new ArrayList<>();
        while (matcher.find()){
            String marketCode = matcher.group(1);
            String otherInfo = matcher.group(2);
            String[] splitArr = otherInfo.split(",");
            String marketName=splitArr[0];
            //获取当前大盘的开盘点数
            BigDecimal openPoint=new BigDecimal(splitArr[1]);
            //前收盘点
            BigDecimal preClosePoint=new BigDecimal(splitArr[2]);
            //获取大盘的当前点数
            BigDecimal curPoint=new BigDecimal(splitArr[3]);
            //获取大盘最高点
            BigDecimal maxPoint=new BigDecimal(splitArr[4]);
            //获取大盘的最低点
            BigDecimal minPoint=new BigDecimal(splitArr[5]);
            //获取成交量
            Long tradeAmt=Long.valueOf(splitArr[8]);
            //获取成交金额
            BigDecimal tradeVol=new BigDecimal(splitArr[9]);
            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(splitArr[30] + " " + splitArr[31]).toDate();
            StockMarketIndexInfo entity = StockMarketIndexInfo.builder()
                    .id(idWorker.nextId())
                    .marketCode(marketCode)
                    .marketName(marketName)
                    .openPoint(openPoint)
                    .preClosePoint(preClosePoint)
                    .curPoint(curPoint)
                    .maxPoint(maxPoint)
                    .minPoint(minPoint)
                    .tradeAmount(tradeAmt)
                    .tradeVolume(tradeVol)
                    .curTime(curTime)
                    .build();
            entities.add(entity);

        }
        log.info("当前时间：{},采集数据成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        int count = stockMarketIndexInfoMapper.insertBatch(entities);
        if(count > 0){
            rabbitTemplate.convertAndSend("stockExchange","inner.market",new Date());
            log.info("当前时间：{},采集大盘数据成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        }else {
            log.error("当前时间：{},采集大盘数据失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        }
    }

    @Override
    public void  getOuterMarketInfo() {
        String url = stockInfoConfig.getMarketUrl()+String.join(",",stockInfoConfig.getOuter());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.GET, httpEntity, String.class);
        int statusCodeValue1 = responseEntity.getStatusCodeValue();
        if(statusCodeValue1!=200){
                log.error("当前时间：{},采集数据失败，http状态码：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),statusCodeValue1);
                return;
        }
        log.info("当前时间：{},采集数据成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        String jsData = responseEntity.getBody();
        List outerStock = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.OUTER);
        log.info("当前时间：{},采集数据成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        int count = stockOuterMarketIndexInfoMapper.inserOutertBatch(outerStock);
        if(count > 0){
            rabbitTemplate.convertAndSend("stockExchange","outer.market",new Date());
            log.info("当前时间：{},采集大盘数据成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        }else {
            log.error("当前时间：{},采集大盘数据失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        }


    }

    @Override
    public void getStockRtIndex() {
        List<String> allStockIds = stockBusinessMapper.getAllStockIds();
        allStockIds = allStockIds.stream().map(id->{
            return id.startsWith("6")?"sh"+id:"sz"+id;
        }).collect(Collectors.toList());
        long startTime = System.currentTimeMillis();
        Lists.partition(allStockIds,20).forEach(list->{
            //1
//            String stockUrl = stockInfoConfig.getMarketUrl() + String.join(",", list);
//            ResponseEntity<String> responseEntity = restTemplate.exchange(stockUrl, HttpMethod.GET, httpEntity, String.class);
//            int statusCodeValue = responseEntity.getStatusCodeValue();
//            if(statusCodeValue!=200){
//                log.error("当前时间：{},采集数据失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
//                return;
//            }
//            String jsData = responseEntity.getBody();
//            List<StockRtInfo> listInfo = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
//            log.info("数据量：{}",listInfo);
//            int count = stockRtInfoMapper.insertBatch(listInfo);
//            if(count > 0){
//                log.info("当前时间：{},插入个股数据成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),listInfo);
//            }else {
//                log.error("当前时间：{},插入个股数据失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),listInfo);
//            }
            //2
//            new Thread(()->{
//                String stockUrl = stockInfoConfig.getMarketUrl() + String.join(",", list);
//                ResponseEntity<String> responseEntity = restTemplate.exchange(stockUrl, HttpMethod.GET, httpEntity, String.class);
//                int statusCodeValue = responseEntity.getStatusCodeValue();
//                if(statusCodeValue!=200){
//                    log.error("当前时间：{},采集数据失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
//                    return;
//                }
//                String jsData = responseEntity.getBody();
//                List<StockRtInfo> listInfo = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
//                log.info("数据量：{}",listInfo);
//                int count = stockRtInfoMapper.insertBatch(listInfo);
//                if(count > 0){
//                    log.info("当前时间：{},插入个股数据成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),listInfo);
//                }else {
//                    log.error("当前时间：{},插入个股数据失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),listInfo);
//                }
//            }).start();
            //3
                threadPoolTaskExecutor.execute(()->{
                    String stockUrl = stockInfoConfig.getMarketUrl() + String.join(",", list);
                    ResponseEntity<String> responseEntity = restTemplate.exchange(stockUrl, HttpMethod.GET, httpEntity, String.class);
                    int statusCodeValue = responseEntity.getStatusCodeValue();
                    if(statusCodeValue!=200){
                        log.error("当前时间：{},采集数据失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
                        return;
                    }
                    String jsData = responseEntity.getBody();
                    List<StockRtInfo> listInfo = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
                    log.info("数据量：{}",listInfo);
                    int count = stockRtInfoMapper.insertBatch(listInfo);
                    if(count > 0){
                        log.info("当前时间：{},插入个股数据成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),listInfo);
                    }else {
                        log.error("当前时间：{},插入个股数据失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),listInfo);
                    }
                });
        });
        long takeTime = System.currentTimeMillis()-startTime;

        log.info("当前时间：{},采集数据耗时：{}ms",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),takeTime);

    }

    @Override
    public void getStockSectorRtIndex() {
        String result = restTemplate.getForObject(stockInfoConfig.getBlockUrl(), String.class);
        //响应结果转板块集合数据
        List<StockBlockRtInfo> infos = parserStockInfoUtil.parse4StockBlock(result);
        log.info("板块数据量：{}",infos.size());
        long startTime = System.currentTimeMillis();
        Lists.partition(infos,20).forEach(list->{
//            threadPoolTaskExecutor.execute(()->{
//                stockBlockRtInfoMapper.insertBatch(list);
//            });
            stockBlockRtInfoMapper.insertBatch(list);
        });
        long takeTime = System.currentTimeMillis()-startTime;

        log.info("当前时间：{},采集数据耗时：{}ms",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),takeTime);

    }


    @PostConstruct
    public void initData(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Referer","https://finance.sina.com.cn/stock/");
        headers.add("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36");
        httpEntity = new HttpEntity<>(headers);
    }
}
