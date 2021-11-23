package com.jsy.clent;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @className（类名称）: CommentClent
 * @description（类描述）: this is the CommentClent class
 * @author（创建人）: ${Administrator}
 * @createDate（创建时间）: 2021/11/23
 * @version（版本）: v1.0
 */
@FeignClient(value = "shop-service-order",configuration = FeignConfiguration.class)
public interface CommentClent {
    //查询店铺的评分
    @RequestMapping(value = "/newComment/selectShopCommentScore",method = RequestMethod.GET)
    public CommonResult<Double> selectShopCommentScore(Long shopId);

}
