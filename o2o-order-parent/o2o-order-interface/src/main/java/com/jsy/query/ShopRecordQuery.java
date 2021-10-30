
package com.jsy.query;

import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * @author yu
 * @since 2020-12-17
 */
@Data
public class ShopRecordQuery extends BaseQuery{

    /**
     * 资产账号uuid
     */
    private String assetsUuid;
    /**
     * 创建时间
     */
    private LocalDateTime createTime1;
    /**
     * 创建时间
     */
    private LocalDateTime createTime2;
    /**
     * 流水单号
     */
    private String accountNumber;
    /**
     * 状态：1有效，2无效
     */
    private Integer stateId;
}