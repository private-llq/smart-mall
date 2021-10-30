
package com.jsy.query;
import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * @author yu
 * @since 2020-12-19
 */

@Data
public class ShopApplySeckillQuery extends BaseQuery{

    //传入状态
    private  Integer state;

    //时间查询
    private LocalDateTime selectTime;
}