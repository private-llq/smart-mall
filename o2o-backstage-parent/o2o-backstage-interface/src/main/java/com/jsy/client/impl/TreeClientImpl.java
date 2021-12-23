package com.jsy.client.impl;

import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.TreeClient;
import com.jsy.domain.Tree;

import java.util.List;

public class TreeClientImpl implements TreeClient {

    @Override
    public CommonResult<List<Tree>> selectAllTree(Long id) {
        return null;
    }

    @Override
    public CommonResult<Tree> getTree(Long id) {
        return null;
    }

    @Override
    public CommonResult<String> getParentTreeAll(Long id) {
        return null;
    }
}
