package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.UserCollect;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2021-11-22
 */
@Component
public interface UserCollectMapper extends BaseMapper<UserCollect> {


    void deleteCollectType (@Param("type") Integer type);

    void delete0(@Param("id") Long id,@Param("userId") Long userId);

    void delete1(@Param("id")Long id, @Param("userId")Long userId);

    void delete2(@Param("id")Long id, @Param("userId")Long userId);

    void delete3(@Param("id")Long id, @Param("userId")Long userId);

    void myDeleteBatchIds(@Param("ids") List<Long> ids);
}
