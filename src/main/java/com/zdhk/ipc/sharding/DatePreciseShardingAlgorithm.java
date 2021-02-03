package com.zdhk.ipc.sharding;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

/**
 * 描述:
 * 分表
 *
 * @auther WangMin
 * @create 2021-01-07 16:36
 */
@Slf4j
public class DatePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Date> {



    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> preciseShardingValue) {
        Date date = preciseShardingValue.getValue();
        String suffix = ShardingUtils.getSuffixByYearMonth(date);
        for (String tableName : availableTargetNames) {
            if (tableName.endsWith(suffix)) {
                return tableName;
            }
        }
        throw new IllegalArgumentException("未找到匹配的数据表");
    }






    /*@Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Date> shardingValue) {

        log.info("logic table name:{},rout column:{}", shardingValue.getLogicTableName(), shardingValue.getColumnName());

        //精确分片
        log.info("column value:{}", shardingValue.getValue());


        String tb_name = shardingValue.getLogicTableName() + "_";


        // 根据当前日期 来 分库分表
        Date date = shardingValue.getValue();
        String year = String.format("%tY", date);
        String mon = String.format("%tm", date);
        String dat = String.format("%td", date);


        // 选择表
        tb_name = tb_name + year + "_" + mon;
        System.out.println("tb_name:" + tb_name);

        for (String each : collection) {
            if (each.equals(tb_name)) {
                return each;
            }
        }

        throw new IllegalArgumentException();
    }*/

}
