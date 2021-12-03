package com.jsy.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @className（类名称）: acceptanceCheckParam
 * @description（类描述）: this is the acceptanceCheckParam class
 * @author（创建人）: ${Administrator}
 * @createDate（创建时间）: 2021/11/25
 * @version（版本）: v1.0
 */
@Data
public class AcceptanceCheckParam implements Serializable {
    private   Long shopId;
    private  String code;
}
