package com.jsy.service;

import com.jsy.domain.UserDataRecord;
import com.baomidou.mybatisplus.extension.service.IService;

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

    UserDataRecord getUserDataRecord(String imId);
}
