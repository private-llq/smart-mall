package com.jsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.AjaxResult;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.constant.Global;
import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.basic.util.vo.UserEntity;
import com.jsy.domain.ShopEvaluation;
import com.jsy.dto.ChatForEvaluationDto;
import com.jsy.dto.ShopEvaluationDto;
import com.jsy.query.ShopEvaluationQuery;
import com.jsy.service.IChatForEvaluationService;
import com.jsy.service.IShopEvaluationService;
import com.jsy.vo.ChatForEvaluationVo;
import com.jsy.vo.ShopEvaluationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopEvaluation")
@Api(tags ="店铺评论服务")
public class ShopEvaluationController {

    @Autowired
    public IShopEvaluationService shopEvaluationService;

    @Autowired
    public IChatForEvaluationService chatForEvaluationService;

    /**
    * 保存和修改公用的
    * @param shopEvaluationVo  传递的实体
    * @return Ajaxresult转换结果
    */
    @ApiOperation(value = "店铺评论新增",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(required = true,value = "传入店铺评论对象")
    @RequestMapping(value="/saveEvaluation",method = RequestMethod.POST)
    public CommonResult saveEvaluation(@RequestBody ShopEvaluationVo shopEvaluationVo){
            int i = shopEvaluationService.save(shopEvaluationVo);
            return i==1?CommonResult.ok(): CommonResult.error(-1,"评论失败");
    }

    /**
    * 保存和修改公用的
    * @param shopEvaluationVo  传递的实体
    * @return Ajaxresult转换结果
    */
    @ApiOperation(value = "店铺评论修改",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(required = true,value = "传入店铺评论对象")
    @RequestMapping(value="/updateEvaluation",method = RequestMethod.POST)
    public CommonResult updateEvaluation(@RequestBody ShopEvaluationVo shopEvaluationVo){
            int i = shopEvaluationService.updateById(shopEvaluationVo);
            if (i==Global.INT_0){
                return CommonResult.error(-1,"修改失败");
            }
            return CommonResult.ok();
    }

    /**
    * 删除店铺的评论信息
    * @param uuid
    * @return
    */
    @ApiOperation(value = "店铺评论的删除",httpMethod = "DELETE",response = AjaxResult.class)
    @ApiParam(required = true,value = "根据id删除")
    @RequestMapping(value="/deleteEvaluation",method = RequestMethod.DELETE)
    public CommonResult<Boolean> deleteEvaluation(@RequestParam("uuid") String uuid){
            int i= shopEvaluationService.deleteEvaluation(uuid);
            if (i==Global.INT_0){
                return CommonResult.error(-1,"删除失败");
            }
            return CommonResult.ok();
    }

    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @ApiOperation(value = "分页查询",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(value = "分页查询")
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    public CommonResult pageEvaluation(@RequestBody ShopEvaluationQuery query) {
        Page<ShopEvaluation> page = new Page<ShopEvaluation>(query.getPage(),query.getRows());
        QueryWrapper<ShopEvaluation> queryWrapper = new QueryWrapper();
        queryWrapper.eq(StringUtils.isNotEmpty(query.getShopUuid()),"shop_uuid",query.getShopUuid())
                .eq(StringUtils.isNotEmpty(query.getUserUuid()),"user_uuid",query.getUserUuid())
                .eq(query.getEvaluateLevel()>Global.INT_0,"evaluate_level",query.getEvaluateLevel());
        try {
            page = shopEvaluationService.page(page,queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(JSYError.INTERNAL.getCode(),"服务繁忙，请稍后再试");
        }
        if (page.getTotal()>Global.INT_0){

        }
        return CommonResult.ok(new PageList<ShopEvaluation>(page.getTotal(),page.getRecords()));
    }

    /**
     * 查询用户已评价信息
     * @param
     * @return
     */
    @ApiOperation(value = "查询用户uuid已评价信息",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(value = "查询用户已评价信息")
    @RequestMapping(value = "/getEvaluations",method = RequestMethod.GET)
    public CommonResult getEvaluations(){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        List<ShopEvaluationDto> list = shopEvaluationService.getEvaluations(userEntity.getUid());
        if (list.isEmpty()){
            return CommonResult.ok();
        }
        return CommonResult.ok(list);
    }

    @ApiOperation(value = "根据店铺uuid查询总评分",httpMethod = "GET")
    @GetMapping(value = "/getLevel/{uuid}")
    public CommonResult getLevel(@PathVariable("uuid")String uuid){
        String acv = shopEvaluationService.getLevel(uuid);
        return CommonResult.ok(acv);
    }

    @ApiOperation(value = "店铺查询自己店铺的评价",httpMethod = "GET")
    @GetMapping(value = "/showEvaluation")
    public CommonResult showEvaluation(){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        List<ShopEvaluationDto> shopEvaluationDtos = shopEvaluationService.showEvalution(userEntity.getUid());
        return CommonResult.ok(shopEvaluationDtos);
    }


    @ApiOperation(value = "根据当前获取订单评论")
    @GetMapping("/getOrderUuidEvaluation")
    public CommonResult getOrderUuidEvaluation(@RequestParam("uuid")String orderUuid){
        ShopEvaluationDto shopEvaluationDto= shopEvaluationService.getEvaluationByOuid(orderUuid);
        return CommonResult.ok(shopEvaluationDto);
    }

    /**********************************chatForEvaluationVo***********************************/

    @ApiOperation(value = "店家回复店铺评论",httpMethod = "POST")
    @PostMapping(value = "/chatEvaluation")
    public CommonResult chatEvaluation(@RequestBody ChatForEvaluationVo chatForEvaluationVo){
        if (StringUtils.isBlank(chatForEvaluationVo.getDescription())){
            return CommonResult.error(-1,"请输入评论信息");
        }

        int i = chatForEvaluationService.save(chatForEvaluationVo);
        if (i==Global.INT_0){
            return CommonResult.error(-1,"评论失败");
        }
        return CommonResult.ok();
    }

    //暂定不做评论回复评论
    /*@ApiOperation(value = "评论回复评论",httpMethod = "POST")
    @PostMapping(value = "/chatWith")
    public CommonResult chatWith(@RequestBody ChatForEvaluationVo chatForEvaluationVo){
        int i = chatForEvaluationService.chatWith(chatForEvaluationVo);
        if (i==0){
            return CommonResult.error(-1,"评论失败");
        }
        return CommonResult.ok();
    }*/

    @ApiOperation(value = "查询",httpMethod = "GET")
    @GetMapping(value = "/selectChatList")
    public CommonResult selectChatList(@RequestParam("evaluationUuid") String evaluationUuid){
        List<ChatForEvaluationDto> list = chatForEvaluationService.selectChatList(evaluationUuid);
        //list.stream().filter(e->e.setList(e.getId().equals(e.getParentId())));
        return CommonResult.ok(list);
    }
}
