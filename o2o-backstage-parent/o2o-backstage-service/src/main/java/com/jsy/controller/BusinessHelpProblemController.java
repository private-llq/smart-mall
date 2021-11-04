package com.jsy.controller;
import com.jsy.dto.BusinessHelpProblemDto;
import com.jsy.service.IBusinessHelpProblemService;
import com.jsy.domain.BusinessHelpProblem;
import com.jsy.query.BusinessHelpProblemQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/businessHelpProblem")
public class BusinessHelpProblemController {
    @Resource
    public IBusinessHelpProblemService businessHelpProblemService;

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResult add(@RequestBody BusinessHelpProblemDto businessHelpProblemDto){
        businessHelpProblemService.add(businessHelpProblemDto);
       return CommonResult.ok();
    }

}
