package com.jsy.mapper;
import com.jsy.domain.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.dto.RecordDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import java.util.List;
/**
 * <p>
 *  Mapper 接口
 * </p>
 * @author lijin
 * @since 2020-11-30
 */
@Component
public interface RecordMapper extends BaseMapper<Record> {
    List<RecordDTO> listUserRecord(@Param("shopUuid") String shopUuid, @Param("goodsUuid") String goodsUuid);
}
