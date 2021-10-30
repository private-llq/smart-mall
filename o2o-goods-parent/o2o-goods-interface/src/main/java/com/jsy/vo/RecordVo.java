package com.jsy.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordVo {
    private String id;
    private String uuid;
    private String goodsUuid;
    private String userUuid;
    private String shopUuid;
}
