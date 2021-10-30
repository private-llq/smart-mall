package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.validation.constraints.Size;

/**
 * <p>
 * 门店招牌表
 * </p>
 *
 * @author yu
 * @since 2021-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_shop_signs")
public class ShopSigns implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 门店招牌图片id
     */
    private String signsImage;

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
     * 商家uuid
     */
    private String shopUuid;


}
