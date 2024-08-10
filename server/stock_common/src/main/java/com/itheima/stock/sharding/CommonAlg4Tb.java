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

//定义个股流水表的分表算法类，覆盖个股表
public class CommonAlg4Tb implements PreciseShardingAlgorithm<Date> , RangeShardingAlgorithm<Date>  {

    //精准查询时走该方法，条件必须是=或in（按月分表1-12月）
    @Override
    public String doSharding(Collection<String> tbNames, PreciseShardingValue<Date> shardingValue) {
        //逻辑表
        String logicTableName = shardingValue.getLogicTableName();
        //分片键
        String columnName = shardingValue.getColumnName();
        //获取等值查询的条件值
        Date curTime = shardingValue.getValue();
        //获取条件值对应的年份，从ds集合中过滤出以该年份结尾的数据源
        String yearMonth = new DateTime(curTime).toString("yyyyMM");
        Optional<String> result = tbNames.stream().filter(tbName -> tbName.endsWith(yearMonth)).findFirst();
        if(result.isPresent()){
            return  result.get();
        }
        return null;
    }

    //范围查询 betwee and
    @Override
    public Collection<String> doSharding(Collection<String> tbNames, RangeShardingValue<Date> shardingValue) {
        String logicTableName = shardingValue.getLogicTableName();
        String columnName = shardingValue.getColumnName();
        Range<Date> valueRange = shardingValue.getValueRange();
        if(valueRange.hasLowerBound()){
            Date startTime = valueRange.lowerEndpoint();
            int startYearMonth = Integer.parseInt(new DateTime(startTime).toString("yyyyMM"));
            tbNames = tbNames.stream().filter(tbName->Integer.parseInt(tbName.substring(tbName.lastIndexOf("_")+1))>=startYearMonth).collect(Collectors.toList());
        }
        if(valueRange.hasUpperBound()){
            Date endTime = valueRange.upperEndpoint();
            int endYearMonth = Integer.parseInt(new DateTime(endTime).toString("yyyyMM"));
            tbNames = tbNames.stream().filter(tbName->Integer.parseInt(tbName.substring(tbName.lastIndexOf("_")+1))<=endYearMonth).collect(Collectors.toList());
        }
        return tbNames;
    }
}
