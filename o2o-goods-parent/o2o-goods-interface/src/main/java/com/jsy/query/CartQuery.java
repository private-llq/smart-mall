package com.jsy.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartQuery implements Serializable {
    /**
     * 商店uuid
     */
    private String shopUuid;

    /**
     * 红包uuid
     */
    private String redPackageUuid;

    /**
     * 用户uuid
     */
    private String userUuid;
}
