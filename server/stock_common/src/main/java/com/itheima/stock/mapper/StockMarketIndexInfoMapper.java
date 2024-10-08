package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.pojo.entity.StockMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author py-qz
* @description 针对表【stock_market_index_info(国内大盘数据详情表)】的数据库操作Mapper
* @createDate 2024-06-20 15:00:05
* @Entity com.itheima.stock.pojo.entity.StockMarketIndexInfo
*/
public interface StockMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);
    List<InnerMarketDomain> getInnerMarketInfo(@Param("marketIds") List<String> marketIds, @Param("timePoint") Date timePoint);


    List<Map> gerSumAmtInfo(@Param("openDate") Date openDate,@Param("endDate") Date endDate,@Param("marketCodes") List<String> marketCodes);

    int insertBatch(@Param("infos") List<StockMarketIndexInfo> entities);


}
