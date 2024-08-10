package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domain.*;
import com.itheima.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author py-qz
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2024-06-20 15:00:05
* @Entity com.itheima.stock.pojo.entity.StockRtInfo
*/
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    List<StockUpdownDomain> getNewestStockInfo(@Param("timePoint") Date curDate);

    List<StockUpdownDomain> getStockFourInfo();

    List<Map> getStockUpdownCount(@Param("openTime") Date openTime,@Param("curTime") Date curTime,@Param("flag") int flag);

    List<Map> getIncreaseRangeInfoByDate(@Param("dateTime") Date curDate);

    List<Stock4MinuteDomain> getStock4MinuteInfo(@Param("openDate") Date openDate,@Param("endDate") Date endDate,@Param("stockCode") String stockCode);

    List<Stock4EvrDayDomain> getStock4DkLine(@Param("startDate") Date startDate,@Param("endDate") Date endDate,@Param("stockCode") String stockCode);
    List<Date> getCloseDates(@Param("stockCode") String stockCode,@Param("startDate") Date startDate,@Param("endDate") Date endDate);
    List<Stock4EvrDayDomain> getStockCreenDkLineData(@Param("stockCode") String stockCode,@Param("dates") List<Date> dates);

    int insertBatch(@Param("list") List<StockRtInfo> list);

    List<Stock4EvrWeekDomain> getStockWeekInfoByCode(@Param("curDate") Date curDate,@Param("stockCode") String stockCode);


    StockLatestTSDomain getStockLatestTS(@Param("code") String code);

    List<Map<String, Object>> getLiveTradingPipelineByCode(@Param("code") String code);

    List<UserInfoDomain> getStockPageInfoByMultiCondition();
}
