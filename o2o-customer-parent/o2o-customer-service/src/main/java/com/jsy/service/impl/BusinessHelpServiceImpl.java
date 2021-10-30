package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.domain.BusinessHelp;
import com.jsy.dto.BusinessHelpProblemDto;
import com.jsy.mapper.BusinessHelpMapper;
import com.jsy.service.IBusinessHelpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-05-26
 */
@Service
public class BusinessHelpServiceImpl extends ServiceImpl<BusinessHelpMapper, BusinessHelp> implements IBusinessHelpService {
    @Resource
    private  BusinessHelpMapper businessHelpMapper;
    @Override
    public List<BusinessHelp> selectType() {
        List<BusinessHelp> selectList = businessHelpMapper.selectList(null);

        List<BusinessHelp> collect = selectList.stream().filter(item -> item.getPid().equals("0"))//先查询一级分类（根节点）
                .map(item -> {  //lamd表达式  遍历
                    item.setChildren(getChrildrens(item,selectList)); //实体类必要要有一个集合   用来装子集的分类
                    return item;
                }).collect(Collectors.toList());
        return collect;
    }

    private List<BusinessHelp> getChrildrens(BusinessHelp root, List<BusinessHelp> allList) {
       List<BusinessHelp> businessHelpTree =  allList.stream().filter(item ->
           item.getPid().equals(root.getUuid())
       ).map(item -> {
                    item.setChildren(getChrildrens(item,allList));  //递归遍历下一个子类
                    return item;
                }).collect(Collectors.toList());
        return businessHelpTree;
    }


    @Override
    public List<BusinessHelp> selectName(String name) {
        List<BusinessHelp> businessHelps = businessHelpMapper.selectList(new QueryWrapper<BusinessHelp>()
               .like("help_name", name)
                .isNotNull("help_details")
        );
        return businessHelps;
    }
}
