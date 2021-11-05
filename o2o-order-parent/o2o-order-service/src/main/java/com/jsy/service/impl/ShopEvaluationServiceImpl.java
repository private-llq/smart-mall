package com.jsy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.constant.Global;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.domain.Order;

import com.jsy.domain.ShopEvaluations;
import com.jsy.dto.ShopEvaluationDto;
import com.jsy.mapper.ShopEvaluationMapper;
import com.jsy.service.IOrderService;
import com.jsy.service.IShopEvaluationService;


import com.jsy.vo.ShopEvaluationVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2020-11-13
 */
@Service
public class ShopEvaluationServiceImpl extends ServiceImpl<ShopEvaluationMapper, ShopEvaluations> implements IShopEvaluationService {

    @Autowired
    private ShopEvaluationMapper shopEvaluationMapper;

    @Autowired
    private IOrderService orderService;

    @Override
    public void evaluationOrder(ShopEvaluationVo shopEvaluationVo) {
        ShopEvaluations shopEvaluation = new ShopEvaluations();
        BeanUtils.copyProperties(shopEvaluationVo,shopEvaluation);
        shopEvaluationMapper.insert(shopEvaluation);
    }

    @Override
    public List<ShopEvaluationDto> getEvaluations(String uuid) {
        return toListDto(shopEvaluationMapper.getEvaluations(uuid));
    }

    @Override
    public int deleteBySid(Long id) {
        return shopEvaluationMapper.deleteBySid(id);
    }

    @Override
    public String getLevel(String uuid) {
        return shopEvaluationMapper.getLevel(uuid);
    }

    @Override
    public List<ShopEvaluationDto> showEvalution(String uid) {
        return shopEvaluationMapper.showEvalution(uid);
    }

    @Override
    public int save(ShopEvaluationVo shopEvaluationVo) {

        /*ShopEvaluation shopEvaluation = this.getOne(new QueryWrapper<ShopEvaluation>().eq("order_uuid", shopEvaluationVo.getOrderUuid()));
        if (shopEvaluation!=null){
            throw new JSYException(-1,"您已经评价过了");
        }*/
        Order order = orderService.getOne(new QueryWrapper<Order>().eq("uuid", shopEvaluationVo.getOrderUuid()));
        if (Global.Str1.equals(order.getEvaluationId())){
            throw new JSYException(-1,"您已经评价过了");
        }
        ShopEvaluations shopEvaluation1 = new ShopEvaluations();
        BeanUtil.copyProperties(shopEvaluationVo,shopEvaluation1);

        return this.save(shopEvaluation1)?1:0;
    }

    @Override
    public int updateById(ShopEvaluationVo shopEvaluationVo) {
        ShopEvaluations shopEvaluation = this.getOne(new QueryWrapper<ShopEvaluations>().eq("uuid", shopEvaluationVo.getUuid()));
        if (Objects.isNull(shopEvaluation)){
            throw new JSYException(-1,"当前评论不存在");
        }
        shopEvaluation.setEvaluateMessage(shopEvaluationVo.getEvaluateMessage());
        shopEvaluation.setEvaluateLevel(shopEvaluationVo.getEvaluateLevel());
        shopEvaluation.setImage(shopEvaluationVo.getImage());
        return this.updateById(shopEvaluation)?1:0;
    }

    @Override
    public int deleteEvaluation(String uuid) {
        return shopEvaluationMapper.deleteEvaluation(uuid);
    }

    @Override
    public ShopEvaluationDto getEvaluationByOuid(String orderUuid) {
        return shopEvaluationMapper.getEvaluationByOuid(orderUuid);
    }



    /**
     * 生成一个用户评论对象
     * @param shopEvaluationVo
     * @return
     */
    private static ShopEvaluations replaysShopEvaluation(ShopEvaluationVo shopEvaluationVo){
        ShopEvaluations shopEvaluation = new ShopEvaluations();
        shopEvaluation.setUserUuid(shopEvaluationVo.getUserUuid());
        shopEvaluation.setShopUuid(shopEvaluationVo.getShopUuid());
        shopEvaluation.setOrderUuid(shopEvaluationVo.getOrderUuid());
        shopEvaluation.setEvaluateMessage(shopEvaluationVo.getEvaluateMessage());
        shopEvaluation.setEvaluateLevel(shopEvaluationVo.getEvaluateLevel());
        shopEvaluation.setImage(shopEvaluationVo.getImage());
        shopEvaluation.setName(shopEvaluationVo.getName());
        shopEvaluation.setCreateTime(shopEvaluationVo.getCreateTime());
        return shopEvaluation;
    }

    @Override
    public ShopEvaluationDto toDto(ShopEvaluations entity) {
        if (Objects.isNull(entity)){
            return null;
        }
        ShopEvaluationDto shopEvaluationDto = new ShopEvaluationDto();
        shopEvaluationDto.setId(entity.getId());
        shopEvaluationDto.setShopUuid(entity.getShopUuid());
        shopEvaluationDto.setUserUuid(entity.getUserUuid());
        shopEvaluationDto.setOrderUuid(entity.getOrderUuid());
        shopEvaluationDto.setEvaluateMessage(entity.getEvaluateMessage());
        shopEvaluationDto.setEvaluateLevel(entity.getEvaluateLevel());
        shopEvaluationDto.setImage(entity.getImage());
        shopEvaluationDto.setCreateTime(entity.getCreateTime());
        return shopEvaluationDto;

    }
    @Override
    public ShopEvaluations toEntity(ShopEvaluationDto dto) {
        if (Objects.isNull(dto)){
            return null;
        }
        ShopEvaluations shopEvaluation = new ShopEvaluations();
        shopEvaluation.setId(dto.getId());
        shopEvaluation.setShopUuid(dto.getShopUuid());
        shopEvaluation.setUserUuid(dto.getUserUuid());
        shopEvaluation.setOrderUuid(dto.getOrderUuid());
        shopEvaluation.setEvaluateMessage(dto.getEvaluateMessage());
        shopEvaluation.setEvaluateLevel(dto.getEvaluateLevel());
        shopEvaluation.setImage(dto.getImage());
        return shopEvaluation;

    }
    @Override
    public List<ShopEvaluations> toListEntity(List<ShopEvaluationDto> list) {
        if (list.isEmpty()){
            return null;
        }
        List<ShopEvaluations> shopEvaluations = new ArrayList<ShopEvaluations>(list.size());
        for (ShopEvaluationDto shopEvaluationDto : list){
            ShopEvaluations shopEvaluation = toEntity(shopEvaluationDto);
            shopEvaluations.add(shopEvaluation);
        }
        return shopEvaluations;
    }

    @Override
    public List<ShopEvaluationDto> toListDto(List<ShopEvaluations> list) {
        if (list.isEmpty()){
            return null;
        }
        List<ShopEvaluationDto> shopEvaluationDtos = new ArrayList<ShopEvaluationDto>(list.size());
        for (ShopEvaluations ShopEvaluation : list){
            ShopEvaluationDto shopEvaluationDto = toDto(ShopEvaluation);
            shopEvaluationDtos.add(shopEvaluationDto);
        }
        return shopEvaluationDtos;
    }
}
