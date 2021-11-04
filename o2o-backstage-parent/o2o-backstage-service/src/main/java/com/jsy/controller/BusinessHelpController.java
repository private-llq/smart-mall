package com.jsy.controller;
import com.jsy.dto.BusinessHelpProblemDto;
import com.jsy.service.IBusinessHelpService;
import com.jsy.domain.BusinessHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/businessHelp")
public class BusinessHelpController {
    @Autowired
    public IBusinessHelpService businessHelpService;

    //查询帮助中心的分类
    @GetMapping(value="/selectType")
    public  CommonResult<List<BusinessHelp>>  selectType(){
        List<BusinessHelp> businessHelps = businessHelpService.selectType();
        return  new CommonResult<>(200,"查询成功",businessHelps);
    }

    @GetMapping(value = "/selectList")
    public CommonResult<List<BusinessHelp>> selectList(String name){
        List<BusinessHelp> businessHelps = businessHelpService.selectName(name);
        return new CommonResult<>(200,"查询成功",businessHelps);
    }




}
