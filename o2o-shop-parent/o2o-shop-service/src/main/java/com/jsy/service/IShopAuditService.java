package com.jsy.service;

import com.jsy.domain.ShopAudit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.parameter.NewShopAuditParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2021-12-01
 */
public interface IShopAuditService extends IService<ShopAudit> {
 /**
  * @author Tian
  * @since 2021/12/1-14:56
  * @description 审核以及驳回接口
  **/
    boolean addAudit(NewShopAuditParam auditParam);
 /**
  * @author Tian
  * @since 2021/12/1-15:14
  * @description 屏蔽接口
  **/
    boolean updateShielding(NewShopAuditParam auditParam);
}
