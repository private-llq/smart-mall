
package com.jsy.query;
import com.jsy.basic.util.query.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author yu
 * @since 2021-11-08
 */
@Data
public class NewShopQuery extends BaseQuery{
    /**
     * 定位地址  经度
     */
    private String  longitude;
    //维度
    private String  latitude;
    /**
     * 分类id
     */
    private Long treeId;
     /**
      * @author Tian
      * @since 2021/11/29-9:15
      * @description  店铺名称
      **/
    private String shopName;


    @ApiModelProperty(value = "审核状态 0未审核 1已审核 2审核未通过  3资质未认证")
    private Integer state;
}