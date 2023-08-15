package com.yl.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

@Mapper
public interface EnhancementBaseMapper<T> extends BaseMapper<T> {
    /**
     * 批量添加
     *
     * @param entityList 数据列表
     * @return 成功标示
     */
    Integer insertBatchSomeColumn(Collection<T> entityList);

}
