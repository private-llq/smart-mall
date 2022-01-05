package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.UserDataRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "SERVICE-USER",configuration = FeignConfiguration.class)
public interface UserDataRecordClient {
    /**
     * list列表
     * @return
     */
    @GetMapping("user/listUserDataRecord")
    CommonResult<List<UserDataRecord>> listUserDataRecord();

    /**
     * 查询
     * @param imId
     */
    @GetMapping("user/getUserDataRecord")
    CommonResult<UserDataRecord> getUserDataRecord(@RequestParam("imId")String imId);
}
