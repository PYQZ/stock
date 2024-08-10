package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domain.StockDescriptionDomain;
import com.itheima.stock.pojo.entity.StockBusiness;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author py-qz
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2024-06-20 15:00:05
* @Entity com.itheima.stock.pojo.entity.StockBusiness
*/
public interface StockBusinessMapper {



    int deleteByPrimaryKey(String id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);

    List<StockBusiness> findAll();
    List<String> getAllStockIds();
    List<Map<String, String>> getFuzzyInfoByCode(@Param("searchStr") String searchStr);

    StockDescriptionDomain getStockDescriptionInfoByCode(@Param("code") String code);
}
