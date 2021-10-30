package com.jsy.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.GoodsBasic;
import com.jsy.domain.GoodsCommendDTO;
import com.jsy.parameter.BatchUpdateGoodsDiscountParam;
import com.jsy.vo.RecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
public interface GoodsBasicMapper extends BaseMapper<GoodsBasic> {

    int updateGoods(String Uuid);

    void insertProductRecord(RecordVo recordVo);

    void updateGoodsReturn(String Uuid);

    int updateStatus(List<String> list);

    List<GoodsCommendDTO> commendGoods(@Param("map")Map<String,Object> map);

    void batchActualDel(List<String> list);

    void deleteOne(String uuid);

    void actualDelOne(String uuid);

    int recoverOne(@Param("uuids") List<String> uuids);

    int onshelf(@Param("uuids") List<String> uuids, @Param("type") int type);

    int toOtherType(@Param("gUUid") String gUuid, @Param("typeUUid") String newTypeUuid);
}
