package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yu
 * @since 2021-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_shop_signs_template")
public class ShopSignsTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String uuid;

    /**
     * 门店招牌模板images_id
     */
    private String signsTemplateImages;

    /**
     * 门店招牌模板image——minio地址
     */
    @TableField(exist = false)
    private List<String> signsTemplateImagesUrl=new ArrayList<>();


}
