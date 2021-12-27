package com.jsy.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @className（类名称）: SelectBillSomeTimeParam
 * @description（类描述）: this is the SelectBillSomeTimeParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/25
 * @version（版本）: v1.0
 */
@Data
public class SelectBillSomeTimeParam {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime endTime;
    private  Long shopId;
}
