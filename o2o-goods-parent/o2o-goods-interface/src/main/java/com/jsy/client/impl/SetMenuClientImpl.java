package com.jsy.client.impl;

import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.SetMenuClient;
import com.jsy.domain.SetMenu;
import com.jsy.dto.SetMenuDto;

import java.util.List;

public class SetMenuClientImpl implements SetMenuClient {
    @Override
    public CommonResult<SetMenu> getShopIdMenus(Long shopId) {
        return null;
    }

    @Override
    public CommonResult<List<SetMenuDto>> batchIds(List<Long> ids) {
        return null;
    }
}
