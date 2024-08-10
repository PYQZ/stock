package com.itheima.stock.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author by itheima
 * @Date 2021/12/30
 * @Description
 */
@ConfigurationProperties(prefix = "stock")
@Data
public class StockInfoConfig {
    @ApiModelProperty("A股大盘ID集合")
    private List<String> inner;
    @ApiModelProperty("外盘ID集合")
    private List<String> outer;
    @ApiModelProperty("股票涨幅区间标题集合")
    private List<String> upDownRange;
    private String marketUrl;
    private String blockUrl;
}