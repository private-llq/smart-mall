package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.NewShopClient;
import com.jsy.domain.NewOrder;
import com.jsy.domain.NewRefund;
import com.jsy.dto.SelectRefundByoderDto;
import com.jsy.mapper.NewOrderMapper;
import com.jsy.mapper.NewRefundMapper;
import com.jsy.query.AgreeRefundParam;
import com.jsy.query.ApplyRefundParam;
import com.jsy.query.ShopWhetherRefundParam;
import com.jsy.service.INewOrderService;
import com.jsy.service.INewRefundService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.base.api.constant.RpcConst;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 退款申请表 服务实现类
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
@Service
public class NewRefundServiceImpl extends ServiceImpl<NewRefundMapper, NewRefund> implements INewRefundService {
    @Resource
    private NewShopClient   newShopClient;
    @Resource
    private NewRefundMapper newRefundMapper;
    @Resource
    private NewOrderMapper newOrderMapper;
    @Resource
    private INewOrderService newOrderService;

    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_BASE_USER, check = false)
    private IBaseUserInfoRpcService iBaseUserInfoRpcService;

    //申请退款
    @Override
    @Transactional
    public Boolean applyRefund(ApplyRefundParam param) {
        NewRefund entity = new NewRefund();
        BeanUtils.copyProperties(param, entity);
        entity.setRefundStatus(0);//申请中
        int insert = newRefundMapper.insert(entity);
        if (insert > 0) {//将订单改为退款中
            NewOrder entity1 = new NewOrder();
            entity1.setId(param.getOrderId());
            entity1.setRefundApplyRole(param.getAccepts());
            entity1.setPayStatus(2);//退款中
            int i = newOrderMapper.updateById(entity1);
            if (i > 0) {
                return true;
            }
        }
        return false;
    }

    //根据订单号查询退款信息
    @Override
    public SelectRefundByoderDto selectRefundByoder(Long orderId) {
        QueryWrapper<NewRefund> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.orderByDesc("create_time");
        List<NewRefund> newRefunds = newRefundMapper.selectList(queryWrapper);
        if (newRefunds == null || newRefunds.size() < 1) {
            throw new JSYException(500, "无申请记录");
        }
        NewRefund newRefund = newRefunds.get(0);
        SelectRefundByoderDto dto = new SelectRefundByoderDto();
        //根据店铺id查询imid
        NewOrder newOrder = newOrderMapper.selectById(orderId);
        CommonResult<String> commonResult = newShopClient.getShopImd(newOrder.getShopId());
        if (commonResult!=null) {
            String data = commonResult.getData();
            dto.setImid(data);
        }



        BeanUtils.copyProperties(newRefund, dto);
        return dto;
    }

    //拒绝退款
    @Override
    @Transactional
    public Boolean refuseRefund(ShopWhetherRefundParam param) {
        NewOrder newOrder = newOrderMapper.selectById(param.getOrderId());
        if (newOrder == null) {
            throw new JSYException(500, "无此订单");
        }
        if (newOrder.getPayStatus() == 3) {
            throw new JSYException(500, "已退款");
        }
        QueryWrapper<NewRefund> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", param.getOrderId());
        queryWrapper.eq("accepts", param.getAccepts());
        queryWrapper.orderByDesc("create_time");
        List<NewRefund> newRefunds = newRefundMapper.selectList(queryWrapper);
        if (newRefunds.size() < 1) {
            throw new JSYException(500, "无申请记录");
        }
        NewRefund newRefund = newRefunds.get(0);
        newRefund.setRefundStatus(2);//拒绝退款
        newRefund.setRefusalCause(param.getRefusalCause());//拒绝原因
        int i = newRefundMapper.updateById(newRefund);
        if (i > 0) {
            newOrder.setPayStatus(4);//退款失败
            int i1 = newOrderMapper.updateById(newOrder);
            if (i1 > 0) {
                return true;
            }
        }
        return false;
    }

    //同意退款
    @Override
    @Transactional
    public Boolean agreeRefund(AgreeRefundParam param) {
        NewOrder newOrder = newOrderMapper.selectById(param.getOrderId());
        if (newOrder == null) {
            throw new JSYException(500, "无此订单");
        }
        if (newOrder.getPayStatus() == 3) {
            throw new JSYException(500, "已退款");
        }
        QueryWrapper<NewRefund> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", param.getOrderId());
        queryWrapper.eq("accepts", param.getAccepts());
        queryWrapper.orderByDesc("create_time");
        List<NewRefund> newRefunds = newRefundMapper.selectList(queryWrapper);
        if (newRefunds.size() < 1) {
            throw new JSYException(500, "无申请记录");
        }
        NewRefund newRefund = newRefunds.get(0);
        newRefund.setRefundStatus(1);//申请成功
        int i = newRefundMapper.updateById(newRefund);
        if (i > 0) {
            newOrder.setPayStatus(3);//退款成功
            int i1 = newOrderMapper.updateById(newOrder);
            if (i1 > 0) {
                Boolean value = newOrderService.allPayRefund(newOrder.getId());
                //实在真实退款待添加接口
                return true;
            }
        }
        return false;
    }
}
