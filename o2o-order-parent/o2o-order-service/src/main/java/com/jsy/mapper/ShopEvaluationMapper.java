package com.jsy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.ShopEvaluations;
import com.jsy.dto.ShopEvaluationDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2020-11-13
 */
@Component
public interface ShopEvaluationMapper extends BaseMapper<ShopEvaluations> {

    /**
     * 查询用户已评价信息
     * @param uuid
     * @return
     */
    @Select("select * from t_shop_evaluation where user_uuid = #{uuid}")
    List<ShopEvaluations> getEvaluations(String uuid);

    /**
     * 根据店家id查询店家的评论
     * @param id
     * @return
     */
    @Select("select * from t_shop_evaluation where shop_id = #{id}")
    List<ShopEvaluations> getBysid(Long id);

    /**
     * 根据店家id删除评论
     * @param id
     */
    @Delete("delete from t_shop_evaluation where shop_id = #{id}")
    int deleteBySid(Long id);

    @Select("select SUM(t.evaluate_level)/count(*)  from t_shop_evaluation  t where shop_uuid = #{uuid}")
    String getLevel(String uuid);

    @Select("select t.*,c.*  from  t_shop_evaluation t left join t_chat_for_evaluation c on t.uuid =c.evaluation_uuid")
    List<ShopEvaluationDto> showEvalution(String uid);

    @Delete("delete a,b from t_shop_evaluation a  left join t_chat_for_evaluation b on a.uuid = b.evaluation_uuid where a.uuid=#{uuid}")
    int deleteEvaluation(String uuid);

    @Select("select * from t_shop_evaluation  where order_uuid = #{orderUuid}")
    ShopEvaluationDto getEvaluationByOuid(@Param("orderUuid") String orderUuid);


}
