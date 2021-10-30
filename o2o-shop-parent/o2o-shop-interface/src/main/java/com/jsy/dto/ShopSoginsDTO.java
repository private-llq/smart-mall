package com.jsy.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class ShopSoginsDTO {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 门店招牌uuid
     */
    private String uuid;

    /**
     * 门店招牌图片ids
     */
    private String signsImages;

    /**
     * 标题
     */
    @Length(max = 15, message = "长度不能超过15")
    private String mainTitle;

    /**
     * 副标题
     */
    @Length(max = 20, message = "长度不能超过20")
    private String subTitle;

    /**
     * 招牌模板图片ids
     */
    private List<String> signsTemplates;

    /**
     * 商家uuid
     */
    private String shopInfoUuid;


}
