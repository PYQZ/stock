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
public class StockLatestTSDomain {
    //最新交易量
    private Long tradeAmt;
    //前收盘价格
    private BigDecimal preClosePrice;
    //最低价
    private BigDecimal lowPrice;
    //最高价
    private BigDecimal highPrice;
    //开盘价
    private BigDecimal openPrice;
    //交易金额
    private BigDecimal tradeVol;
    //当前价格
    private BigDecimal tradePrice;
    //当前日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
    private Date curDate;
}
