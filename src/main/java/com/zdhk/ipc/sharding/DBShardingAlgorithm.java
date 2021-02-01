package com.zdhk.ipc.sharding;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * 描述:
 * 分库
 *
 * @auther WangMin
 * @create 2021-01-07 16:12
 */
@Slf4j
public class DBShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> shardingValue) {
        // 真实节点
        /*collection.stream().forEach((item) -> {
            log.info("actual node db:{}", item);
        });*/

        log.info("logic table name:{},rout column:{}", shardingValue.getLogicTableName(), shardingValue.getColumnName());

        //精确分片
        log.info("column value:{}", shardingValue.getValue());

        long orderId = shardingValue.getValue();

        long db_index = orderId & (2 - 1);  //对2取余

        for (String each : collection) {
            if (each.equals("db"+db_index)) {
                return each;
            }
        }

        throw new IllegalArgumentException();
    }

}
