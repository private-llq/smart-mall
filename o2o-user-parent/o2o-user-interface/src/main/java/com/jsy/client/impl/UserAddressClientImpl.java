package com.jsy.client.impl;

import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.UserAddressClient;

public class UserAddressClientImpl implements UserAddressClient {



    @Override
    public CommonResult getByUuid(String uuid) {
        return CommonResult.error(-1,"熔断");
    }
}
