package com.jsy.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 2020-12-04
 * @author yu
 */
@Component
public interface BaseSql<E> {
    /**
     * 通用查询
     * @param tableName 表名
     * @param wrapper 查询条件
     * @return
     */
    @Select("select ${ew.SqlSelect} from ${tableName} ${ew.customSqlSegment}")
    List<E> listByCondition(@Param("tableName") String tableName, @Param("ew") Wrapper wrapper);

    /**
     * 通用修改
     * @param tableName
     * @param wrapper
     * @return
     */
    @Update("update ${tableName} set ${ew.sqlSet} ${ew.customSqlSegment}")
    int updateByCondition(@Param("tableName") String tableName, @Param("ew") Wrapper wrapper);

    /**
     * 通用删除
     * @param tableName
     * @param Wrapper
     * @return
     */
    @Delete("delete ${ew.SqlSelect} from ${tableName} ${ew.customSqlSegment}")
    int deleteByCondition(@Param("tableName") String tableName, @Param("ew") Wrapper Wrapper);

    /**
     * 未完成
     * @param tableName
     * @param wrapper
     * @return
     */
    @Insert("insert into ${tableName} ${ew.} valus ${ew.}")
    int insertByCondition(@Param("tableName") String tableName, @Param("ew") Wrapper wrapper);
}
