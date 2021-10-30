package com.jsy.controller;

import com.jsy.service.IChatForEvaluationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatForEvaluation")
@Api("聊天回复")
public class ChatForEvaluationController {
    @Autowired
    public IChatForEvaluationService chatForEvaluationService;


}
