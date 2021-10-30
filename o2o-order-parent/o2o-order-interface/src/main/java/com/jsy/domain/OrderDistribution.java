package com.jsy.domain;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_order_distribution")
public class OrderDistribution implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单描述关联订单id
     */
    private Long orderId;

    /**
     * 收件人
     */
    private String name;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 收货地址
     */
    private String address;


    public void copy(OrderDistribution source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
