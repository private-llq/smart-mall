package com.jsy.config;

import lombok.Data;

import java.io.Serializable;

/**
 * @className（类名称）: AlipayRefundParam
 * @description（类描述）: this is the AlipayRefundParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/17
 * @version（版本）: v1.0
 */
@Data
public class AlipayRefundParam implements Serializable {
  private   String orderNo;
}
