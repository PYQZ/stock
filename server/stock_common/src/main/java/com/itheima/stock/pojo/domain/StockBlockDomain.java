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
public class StockBlockDomain {
    private Integer companyNum;
    /**
     * 交易量
     */
    private Long tradeAmt;
    /**
     * 板块编码
     */
    private String code;
    /**
     * 平均价
     */
    private BigDecimal avgPrice;
    /**
     * 板块名称
     */
    private String name;
    /**
     * 当前日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date curDate;
    /**
     *交易金额
     */
    private BigDecimal tradeVol;
    /**
     * 涨跌率
     */
    private BigDecimal updownRate;
}
