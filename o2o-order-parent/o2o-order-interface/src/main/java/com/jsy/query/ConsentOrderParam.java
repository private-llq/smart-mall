package com.jsy.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @className（类名称）: ConsentOrderParam
 * @description（类描述）: this is the ConsentOrderParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/11/25
 * @version（版本）: v1.0
 */
@Data
public class ConsentOrderParam implements Serializable {
  private   Long shopId;
  private   Long orderId;
}
