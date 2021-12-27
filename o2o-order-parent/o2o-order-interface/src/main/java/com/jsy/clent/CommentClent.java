package com.jsy.clent;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.dto.SelectShopCommentScoreDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

/**
 * @className（类名称）: CommentClent
 * @description（类描述）: this is the CommentClent class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/11/23
 * @version（版本）: v1.0
 */
@FeignClient(value = "shop-service-order",configuration = FeignConfiguration.class)
public interface CommentClent {
    //查询店铺的评分
    @RequestMapping(value = "/newComment/selectShopCommentScore",method = RequestMethod.GET)
    public CommonResult<SelectShopCommentScoreDto> selectShopCommentScore(@RequestParam("shopId") Long shopId);

    //初始化店铺余额
    @GetMapping(value="/shopCapital/initializeShopCapital")
    public CommonResult<Boolean>  initializeShopCapital(@RequestParam("shopId") @NotNull Long shopId);



}
