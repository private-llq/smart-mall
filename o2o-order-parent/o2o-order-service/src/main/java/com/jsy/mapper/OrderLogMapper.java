package com.jsy.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.domain.OrderLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2020-12-24
 */
public interface OrderLogMapper extends BaseMapper<OrderLog> {
    /**
     * PC
     * @title
     * @description
     * @params [id, uuid]
     * @since        - 2021-05-15
     * @throws
     * @return      Result<int>
     *
     * * * * * T * I * M * E * * * * *
     * 创建及修改内容
     * @author      shY
     * @createTime  2021-05-15
     * @editor      shY
     * @updateDesc  原著
     * @updateTime  2021-05-15
     */
    @Update("update t_order_log set state_id = 1,finish_person_uuid = #{uuid} where id = #{id}")
    int updateByUid(@Param("id") Integer id, @Param("uuid")String uuid);

    /**
     * PC
     * @title       
     * @description 
     * @params [page, rows, queryWrapper]
     * @since        - 2021-05-15 
     * @throws      
     * @return      Result<java.util.List<com.jsy.domain.OrderLog>>
     *
     * * * * * T * I * M * E * * * * *
     * 创建及修改内容
     * @author      shY
     * @createTime  2021-05-15 
     * @editor      shY
     * @updateDesc  原著
     * @updateTime  2021-05-15  
     */
    IPage<OrderLog> pageList(Page page, @Param(Constants.WRAPPER) QueryWrapper<OrderLog> queryWrapper);
}
