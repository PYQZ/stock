package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domain.OuterMarketDomain;
import com.itheima.stock.pojo.entity.StockOuterMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author py-qz
* @description 针对表【stock_outer_market_index_info(外盘详情信息表)】的数据库操作Mapper
* @createDate 2024-06-20 15:00:05
* @Entity com.itheima.stock.pojo.entity.StockOuterMarketIndexInfo
*/
public interface StockOuterMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockOuterMarketIndexInfo record);

    int insertSelective(StockOuterMarketIndexInfo record);

    StockOuterMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockOuterMarketIndexInfo record);

    int updateByPrimaryKey(StockOuterMarketIndexInfo record);

    List<OuterMarketDomain> getOuterMarketInfo(@Param("marketIds") List<String> marketIds, @Param("timePoint") Date timePoint);

    int inserOutertBatch(@Param("infos") List<StockOuterMarketIndexInfo> outerStock);
}
