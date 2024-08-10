package com.itheima.stock.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

//公共分库算法类，覆盖个股，大盘，板块相关表
public class CommonAlg4Db implements PreciseShardingAlgorithm<Date> , RangeShardingAlgorithm<Date>  {

    //精准查询时走该方法，条件必须是=或in
    @Override
    public String doSharding(Collection<String> dsNames, PreciseShardingValue<Date> shardingValue) {
        //逻辑表
        String logicTableName = shardingValue.getLogicTableName();
        //分片键
        String columnName = shardingValue.getColumnName();
        //获取等值查询的条件值
        Date curTime = shardingValue.getValue();
        //获取条件值对应的年份，从ds集合中过滤出以该年份结尾的数据源
        String year = new DateTime(curTime).getYear() + "";
        Optional<String> result = dsNames.stream().filter(dsName -> dsName.endsWith(year)).findFirst();
        if(result.isPresent()){
            return  result.get();
        }
        return null;
    }

    //范围查询 betwee and
    @Override
    public Collection<String> doSharding(Collection<String> dsNames, RangeShardingValue<Date> shardingValue) {
        String logicTableName = shardingValue.getLogicTableName();
        String columnName = shardingValue.getColumnName();
        Range<Date> valueRange = shardingValue.getValueRange();
        if(valueRange.hasLowerBound()){
            Date startTime = valueRange.lowerEndpoint();
            int startYear = new DateTime(startTime).getYear();
            dsNames = dsNames.stream().filter(dsName->Integer.parseInt(dsName.substring(dsName.lastIndexOf("-")+1))>=startYear).collect(Collectors.toList());
        }
        if(valueRange.hasUpperBound()){
            Date endTime = valueRange.upperEndpoint();
            int endYear = new DateTime(endTime).getYear();
            dsNames = dsNames.stream().filter(dsName->Integer.parseInt(dsName.substring(dsName.lastIndexOf("-")+1))<=endYear).collect(Collectors.toList());
        }
        return dsNames;
    }
}
