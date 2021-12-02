package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.parameter.NewShopAuditParam;
import com.jsy.service.INewShopService;
import com.jsy.service.IShopAuditService;
import com.jsy.domain.ShopAudit;
import com.jsy.query.ShopAuditQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/shopAudit")
public class ShopAuditController {
    @Autowired
    public IShopAuditService shopAuditService;
    @Autowired
    public INewShopService shopService;

 /**
  * @author Tian
  * @since 2021/12/1-15:04
  * @description 审核接口
  **/
    @PostMapping(value = "/updateRejected")
    public CommonResult updateRejected(@RequestBody NewShopAuditParam auditParam){
       boolean b= shopAuditService.addAudit(auditParam);
        return CommonResult.ok();
    }
    /**
     * @author Tian
     * @since 2021/12/1-15:04
     * @description 屏蔽接口
     **/
    @PostMapping(value = "/updateShielding")
    public CommonResult updateShielding(@RequestBody NewShopAuditParam auditParam){
        boolean b= shopAuditService.updateShielding(auditParam);
        return CommonResult.ok();
    }

     /**
      * @author Tian
      * @since 2021/12/1-18:03
      * @description 查询审核不通过理由
      **/

     @GetMapping(value = "/getRejected")
     public CommonResult getRejected(@RequestParam("shopId")Long shopId){
         ShopAudit shopAudit = shopAuditService.getOne(new QueryWrapper<ShopAudit>()
                         .eq("shop_id", shopId)
                         .orderByDesc("create_time")
                         .last("limit 1")
         );
         return CommonResult.ok(shopAudit);
     }

      /**
       * @author Tian
       * @since 2021/12/1-18:04
       * @description 查询屏蔽理由
       **/

}
