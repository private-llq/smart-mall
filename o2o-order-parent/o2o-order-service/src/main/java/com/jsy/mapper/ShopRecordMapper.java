package com.jsy.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.domain.ShopRecord;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2020-12-17
 */
public interface ShopRecordMapper extends BaseMapper<ShopRecord> {

    @Select("select * from t_shop_record where account_name = #{uuid}")
    ShopRecord selectByAName(String uuid);

    @Delete("delete from t_shop_record where order_uuid = #{uuid}")
    int deleteByOuid(String uuid);


    @Delete({
            "<script>",
            "delete from t_shop_record where order_uuid in ",
            "<foreach collection='strings' item='item' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    int deleteByStr(@Param("strings")List<String> strings);

    IPage<ShopRecord> pageShopRecord(Page<ShopRecord> page, QueryWrapper queryWrapper);
}
