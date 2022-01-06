package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.basic.util.PageInfo;
import com.jsy.domain.UserDataRecord;
import com.jsy.dto.MatchTheUserDto;
import com.jsy.query.UserDataRecordQuery;

import java.util.List;

/**
 * <p>
 * 用户健康信息档案 服务类
 * </p>
 *
 * @author yu
 * @since 2022-01-04
 */
public interface IUserDataRecordService extends IService<UserDataRecord> {

    void saveUserDataRecord(UserDataRecord userDataRecord);

    List<UserDataRecord> getUserDataRecord(String imId);

    List<UserDataRecord> listUserDataRecord();


    List<UserDataRecord> getUserDataRecordTreeId(Long treeId);

    PageInfo<MatchTheUserDto> matchTheUser(UserDataRecordQuery query);
}
