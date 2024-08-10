package com.itheima.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stock4EvrWeekDomain {
    //一周内平均价
    private BigDecimal avgPrice;
    //一周内最低价
    private BigDecimal minPrice;
    //周一开盘价
    private BigDecimal openPrice;
    //一周内最高价
    private BigDecimal maxPrice;
    //周五收盘价（如果当前日期不到周五，则显示最新价格）
    private BigDecimal closePrice;
    //股票编码
    private String stockCode;
    //一周内最大时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
    private Date mxTime;

}
