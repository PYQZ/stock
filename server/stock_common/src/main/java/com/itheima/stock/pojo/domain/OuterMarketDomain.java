package com.itheima.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OuterMarketDomain {
    //大盘名称
    private String name;
    //当前大盘点
    private BigDecimal curPoint;
    //跌涨值
    private BigDecimal upDown;
    //涨幅
    private BigDecimal rose;
    //当前时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date curTime;
}
