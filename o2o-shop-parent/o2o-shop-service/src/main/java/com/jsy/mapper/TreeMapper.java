package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.Tree;
import com.jsy.query.MySortQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2021-11-02
 */
@Component
public interface TreeMapper extends BaseMapper<Tree> {
     void sortMenu(@Param("query") MySortQuery mySortQuery);
}
