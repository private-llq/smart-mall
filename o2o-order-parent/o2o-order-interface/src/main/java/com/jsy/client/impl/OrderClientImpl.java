package com.jsy.client.impl;

import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.OrderClient;
import com.jsy.query.OrderQuery;
import com.jsy.vo.OrderVo;
import com.jsy.vo.ShopEvaluationVo;
import com.jsy.vo.ShopFinsh;
import com.jsy.vo.ShopRecordVo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
public class OrderClientImpl implements OrderClient {

    @Override
    public CommonResult<Boolean> saveOrder(OrderVo ordervo) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult<Boolean> finishOrder(Long id) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult<Boolean> userEvaluation(ShopEvaluationVo shopEvaluationVo) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult<Boolean> deleteOrder(String uuid) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult changeStatus(String uuid) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult deleteByStatus() {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult getOrder(Long id) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }


    @Override
    public CommonResult pageOrder(OrderQuery query) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult goodsOrder(String uuid) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult waitOrder(String uuid) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult backOrder(String uuid) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult historyOrder(String uuid) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult pageBill(String uuid) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult mouthBill(OrderQuery query) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult shopFinish(ShopFinsh shopFinsh) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult getOrderByUuid(String uuid) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult finishOrderByUuid(String uuid) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult deleteOrderByUuid(String uuid) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult backOrderByUuid(String uuid) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult getRedpacket(OrderQuery query) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }

    @Override
    public CommonResult shopRecord(ShopRecordVo shopRecordVo) {
            return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");

    }
}
