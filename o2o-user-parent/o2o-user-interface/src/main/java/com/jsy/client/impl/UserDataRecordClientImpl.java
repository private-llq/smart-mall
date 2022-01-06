package com.jsy.client.impl;

import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.UserDataRecordClient;
import com.jsy.domain.UserDataRecord;

import java.util.List;

public class UserDataRecordClientImpl implements UserDataRecordClient {
    @Override
    public CommonResult<List<UserDataRecord>> listUserDataRecord() {
        return null;
    }

    @Override
    public CommonResult<List<UserDataRecord>> getUserDataRecord(String imId) {
        return null;
    }

    @Override
    public CommonResult<List<UserDataRecord>> getUserDataRecordTreeId(Long treeId) {
        return null;
    }
}
