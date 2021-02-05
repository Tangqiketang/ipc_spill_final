package com.zdhk.ipc.mapper;

import com.zdhk.ipc.entity.TOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Wang Min
 * @since 2021-01-08
 */
public interface TOrderMapper extends BaseMapper<TOrder> {

    List<TOrder> listByRowNum(@Param("suffix") String suffix, @Param("size") Integer size, @Param("rowNum") Integer rowNum
            , @Param("userName") String userName);

}
