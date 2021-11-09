package com.jsy.service.impl;

import com.jsy.domain.UserRecord;
import com.jsy.mapper.UserRecordMapper;
import com.jsy.service.IUserRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户个人档案表 服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-11-09
 */
@Service
public class UserRecordServiceImpl extends ServiceImpl<UserRecordMapper, UserRecord> implements IUserRecordService {

}
