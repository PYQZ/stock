package com.itheima.stock.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDescriptionDomain {
    //个股主营业务查询接口
    //行业，也就是行业板块名称
    private String trade;
    //公司主营业务
    private String business;
    //股票编码
    private String code;
    //公司名称
    private String name;
}
