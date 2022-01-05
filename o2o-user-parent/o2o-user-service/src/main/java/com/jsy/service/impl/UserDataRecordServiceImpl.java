package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.client.TreeClient;
import com.jsy.domain.Tree;
import com.jsy.domain.UserDataRecord;
import com.jsy.mapper.UserDataRecordMapper;
import com.jsy.service.IUserDataRecordService;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 用户健康信息档案 服务实现类
 * </p>
 *
 * @author yu
 * @since 2022-01-04
 */
@Service
public class UserDataRecordServiceImpl extends ServiceImpl<UserDataRecordMapper, UserDataRecord> implements IUserDataRecordService {

    @Autowired
    UserDataRecordMapper userDataRecordMapper;

    @Autowired
    TreeClient treeClient;

    @Override
    public void saveUserDataRecord(UserDataRecord userDataRecord) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }
        userDataRecord.setUid(String.valueOf(loginUser.getId()));
        userDataRecord.setImId(loginUser.getImId());
        Tree data = treeClient.getTree(userDataRecord.getShopTreeId()).getData();
        if (Objects.nonNull(data)){
            Long parentId = data.getParentId();
            Tree tree = treeClient.getTree(parentId).getData();
            if (Objects.nonNull(tree)){
                userDataRecord.setShopTreeName(tree.getName()+"-"+data.getName());
            }

        }
        userDataRecordMapper.insert(userDataRecord);
    }

    /**
     * @author Tian
     * @since 2021/12/3-11:32
     * @description 店铺分类名称
     **/
    private String getShopTreeIdName(String[] split) {
        String shopTreeIdName = "";
        if (split.length >= 0) {
            String name = treeClient.getTree(Long.valueOf(split[split.length - 1])).getData().getName();
            return name;
        }else {
            throw new JSYException(-1,"商品分类错误");
        }

    }

    @Override
    public UserDataRecord getUserDataRecord(String imId) {
        UserDataRecord userDataRecord = userDataRecordMapper.selectOne(new QueryWrapper<UserDataRecord>().eq("im_id", imId));
        return userDataRecord;
    }
}
