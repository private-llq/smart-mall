package com.jsy.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.domain.Record;
import com.jsy.dto.RecordDTO;
import com.jsy.mapper.RecordMapper;
import com.jsy.service.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lijin
 * @since 2020-11-30
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {

    @Autowired
    private RecordMapper RecordMapper;


    @Override
    public List<RecordDTO> listUserRecord(String shopUuid, String goodsUuid) {

        return  RecordMapper.listUserRecord(shopUuid,goodsUuid);
    }
}
