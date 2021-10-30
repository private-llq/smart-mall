package com.jsy.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.domain.Order;
import com.jsy.domain.OrderCommodity;
import com.jsy.dto.OrderDto;
import com.jsy.dto.OrderInfoDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yu
 * @since 2020-11-12
 */
@Component
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 查询到订单商品
     *
     * @param uuid
     * @return
     */
    @Select("select * from t_order_commodity where order_uuid = #{uuid}")
    List<OrderCommodity> getCommodity(String uuid);

    /**
     * 查询待用户评价订单
     *
     * @param uuid
     * @return
     */
    @Select("select * from t_order where evaluation_id = -1 and user_uuid = #{uuid}")
    List<Order> selectWait(String uuid);

    /**
     * 查询用户退款中订单
     *
     * @param uuid
     * @return
     */
    @Select("select * from t_order where pay_state = 3 and user_uuid = #{uuid}")
    List<Order> selectBack(String uuid);

    /**
     * 查询用户历史订单
     *
     * @param uuid
     * @return
     */
    @Select("select * from t_order where state_id = 3 and user_uuid = #{uuid}")
    List<Order> getHistory(String uuid);

    /**
     * 查询店家的这个用户未完成订单
     *
     * @param userUuid
     * @param uuid
     * @return
     */
    @Select("select * from t_order where state_id = 1 and user_uuid = #{userUuid} and shop_id = #{uuid}")
    List<Order> getByUid(String userUuid, String uuid);

    /**
     * 根据店家id查找Order信息 作废的和待删除的不在
     *
     * @param uuid
     * @return
     */

    List<Order> getBillBySid(String uuid);

    /**
     * 根据id将订单改为待删除状态
     *
     * @param uuid
     */
    @Update("update t_order set state_id = 4 where uuid = #{uuid}")
    int changeStatus(String uuid);

    /**
     * 删除所有数据库待删除订单,并删除该订单商品
     */
    @Delete("delete a,b from t_order as a left join t_order_commodity as b  on a.uuid = b.order_uuid where a.state_id = 4")
    int deleteByStatus();

    /**
     * 根据uuid删除订单及订单商品
     *
     * @param uuid
     */
    @Delete("delete a,b from t_order as a left join t_order_commodity as b  on b.order_uuid = a.uuid where a.uuid =#{uuid}")
    int deleteGoodsById(String uuid);

    /**
     * 根据uuid查找订单 1未支付，2已支付，3退款中的订单
     *
     * @param userUuid
     * @return
     */
    List<OrderInfoDto> getByUserUuid(String userUuid);

    /**
     * 根据uuid完成订单
     *
     * @param uuid
     */
    @Update("update t_order set state_id = 3 ,used = 1, service_time = NOW() where uuid = #{uuid}")
    int finishOrderByUuid(String uuid);

    /**
     * 将订单更改为待删除状态
     *
     * @param uuid
     */
    @Update("update t_order set state_id = 4 where uuid = #{uuid}")
    int deleteOrderByUuid(String uuid);

    /**
     * 根据用户uuid查找退款中订单
     *
     * @param uuid
     * @return
     */
    @Select("select * from t_order where pay_state > 1 and user_uuid = #{uuid}")
    List<Order> backOrderByUuid(String uuid);

    /**
     * 通过订单uuid查找订单
     *
     * @param uuid
     * @return
     */
    @Select("select * from t_order where uuid = #{uuid}")
    Order getByUuid(String uuid);

    /**
     * 获取区间内的总金额
     *
     * @param wrapper
     * @return
     */
    @Select("select SUM(t.order_price) from t_order t ${ew.customSqlSegment}")
    BigDecimal getMoney(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 在订单中查询店铺uuid和在店铺使用平台红包的数量
     *
     * @param wrapper
     * @return
     */
    @Select("select tr.shop_uuid as shopUuid,count(tr.shop_uuid) as count, COALESCE(SUM(tu.money),0) as money  " +
            "from t_order tr " +
            "left join t_user_redpacket tu on tr.redpacket_uuid =tu.redpacket_uuid and tu.type = 2 " +
            "${ew.customSqlSegment} group by tr.shop_uuid")
    List<JSONObject> getRedpacket(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 根据订单uuid完成评价
     *
     * @param orderUuid
     */
    @Update("update t_order set evaluation_id = 1 where uuid = #{orderUuid} and evaluation_id = 0")
    int updateEid(String orderUuid);

    //订单状态 0:待接单,1:已接单,2:正在配送，3:已完成,,4:待删除,5:作废
    List<OrderInfoDto> getRunOrder(String uuid);

    @Update("update t_order set state_id = 2 where uuid = #{uuid} and state_id = 1")
    int setRunOrder(String uuid);

    /**
     * 用户申请退款 ,在订单未完成的时候才能申请退款
     *
     * @param uuid 订单状态 0:待接单,1:已接单,2:正在配送，3:已完成,,4:待删除,5:作废
     *             支付状态 0:是未支付，1是支付成功，2是退款中，3是退款成功，4拒绝退款
     * @return
     */
    @Update("update t_order set pay_state = 2 where uuid = #{uuid} and state_id < 3")
    int cancelOrder(String uuid);


    @Delete({"<script>",
            "delete from t_order where uuid in ",
            "<foreach collection='strings' item='item' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    int cancelOrderList(@Param("strings") List<String> strings);


    @Select("select * from t_order where shop_uuid = #{uuid}")
    List<OrderDto> backOrderBySUuid(String uuid);

    /**
     * 只能把退款中的订单退款
     *
     * @param uuid
     * @return
     */
    @Update("update t_order set pay_state = 3 where uuid = #{uuid} and pay_state = 2")
    int finishCancelOrder(String uuid);

    //0是未支付，1是已支付，2是退款中，3是已退款
    @Update("update t_order set pay_state = 1 where uuid = #{uuid}")
    int payedOrder(String uuid);

    @Update("update t_order set state_id = #{stateId} where uuid = #{uuid}")
    int changeStatusByUid(Integer stateId, String uuid);

    /**
     * 根据支付状态获取订单
     *
     * @param stateId
     * @return
     */
    @Select("select * from t_order where pay_state = #{stateId}")
    List<Order> getByStateId(String stateId);

    @Update("update t_order set used = 1 where uuid = #{uuid}")
    int changeUsed(String uuid);

    @Select("select COALESCE(cast(SUM(t.order_price) as decimal(20,2)),0) as SUM,count(1) as count from t_order t ${ew.customSqlSegment}")
    Map<String,String> getTurnover(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select Count(1) from t_order where pay_state = 3 and user_uuid = #{uid}")
    int selectBackCount(String uid);

    IPage<OrderInfoDto> pageUserState(Page page, @Param(Constants.WRAPPER)QueryWrapper<Order> queryWrapper);

    @Select("select * from t_order ${ew.customSqlSegment}")
    List<Order> billDetails(@Param("ew") QueryWrapper<Order> queryWrapper);
}