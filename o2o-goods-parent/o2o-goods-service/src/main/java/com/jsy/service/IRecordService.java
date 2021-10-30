package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.Record;
import com.jsy.dto.RecordDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lijin
 * @since 2020-11-30
 */
public interface IRecordService extends IService<Record> {

    List<RecordDTO> listUserRecord(String shopUuid, String goodsUuid);
}
