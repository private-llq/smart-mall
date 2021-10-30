package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.Timetable;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "SERVICE-SHOP",configuration = FeignConfiguration.class)
public interface TimeTableClient {

    /**
     * 查询这批活动的时长
     */
    @ApiOperation("查询这批活动的时长")
    @RequestMapping(value = "/timetable/pub/selectTimeLong",method = RequestMethod.GET)
    CommonResult<Timetable> selectTimeLong();
}
