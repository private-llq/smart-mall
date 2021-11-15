package com.jsy.client.impl;

import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.TreeClient;
import com.jsy.domain.Tree;

public class TreeClientImpl implements TreeClient {
    @Override
    public CommonResult<Boolean> selectAllTree(Integer id) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");
    }

    @Override
    public CommonResult<Tree> getTree(Long id) {
        return null;
    }
}
