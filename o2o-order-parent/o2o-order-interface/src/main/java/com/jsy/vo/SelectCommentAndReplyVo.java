package com.jsy.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @className（类名称）: selectCommentAndReplyVo
 * @description（类描述）: this is the selectCommentAndReplyVo class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/11/22
 * @version（版本）: v1.0
 */
@Data
public class SelectCommentAndReplyVo {
    private Long id ;
    private Long shopId;
    private  Long userId;
    private String name;
    private Long orderId;
    private String images;
    private String evaluateMessage;
    private Integer evaluateLevel;
    private LocalDateTime commenttime;
    private String reply;
    private LocalDateTime replytime;
}
