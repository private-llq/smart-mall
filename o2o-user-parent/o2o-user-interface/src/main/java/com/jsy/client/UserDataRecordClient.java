package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.UserDataRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "shop-service-user",configuration = FeignConfiguration.class)
public interface UserDataRecordClient {
    /**
     * list列表
     * @return
     */
    @GetMapping("userDataRecord/listUserDataRecord")
    CommonResult<List<UserDataRecord>> listUserDataRecord();

    /**
     * 查询
     * @param imId
     */
    @GetMapping("userDataRecord/getUserDataRecord")
    CommonResult<List<UserDataRecord>> getUserDataRecord(@RequestParam("imId")String imId);

    /**
     * 查询
     * @param treeId
     */
    @GetMapping("userDataRecord/getUserDataRecordTreeId")
    CommonResult<List<UserDataRecord>> getUserDataRecordTreeId(@RequestParam("treeId")Long treeId);

}
