package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.ShopEvaluations;
import com.jsy.dto.ShopEvaluationDto;
import com.jsy.mapper.BaseMapper;
import com.jsy.vo.ShopEvaluationVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2020-11-13
 */
public interface IShopEvaluationService extends IService<ShopEvaluations> , BaseMapper<ShopEvaluationDto, ShopEvaluations> {

    void evaluationOrder(ShopEvaluationVo shopEvaluationVo);

    List<ShopEvaluationDto> getEvaluations(String uuid);

    int deleteBySid(Long id);

    String getLevel(String uuid);

    List<ShopEvaluationDto> showEvalution(String uid);

    int save(ShopEvaluationVo shopEvaluationVo);

    int updateById(ShopEvaluationVo shopEvaluationVo);

    int deleteEvaluation(String uuid);

    ShopEvaluationDto getEvaluationByOuid(String orderUuid);


}
