package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.domain.BusinessHelp;
import com.jsy.domain.BusinessHelpProblem;
import com.jsy.dto.BusinessHelpProblemDto;
import com.jsy.mapper.BusinessHelpMapper;
import com.jsy.mapper.BusinessHelpProblemMapper;
import com.jsy.service.IBusinessHelpProblemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-05-27
 */
@Service
public class BusinessHelpProblemServiceImpl extends ServiceImpl<BusinessHelpProblemMapper, BusinessHelpProblem> implements IBusinessHelpProblemService {

    @Resource
    private BusinessHelpProblemMapper businessHelpProblemMapper;
    @Resource
    private BusinessHelpMapper businessHelpMapper;

    @Override
    public void add(BusinessHelpProblemDto businessHelpProblemDto) {

        BusinessHelpProblem businessHelpProblem = businessHelpProblemMapper.selectOne(new QueryWrapper<BusinessHelpProblem>()
                .eq("shop_uuid", businessHelpProblemDto.getShopUuid())
                .eq("help_uuid", businessHelpProblemDto.getHelpUuid())
                .ge("create_time", LocalDate.now())
                .le("create_time", LocalDate.now().plusDays(1))
        );


        if(Objects.nonNull(businessHelpProblem)){
            throw new JSYException(-1,"今天已经点击过来不能在进行点击");
        }

        BusinessHelp businessHelp = businessHelpMapper.selectById(businessHelpProblemDto.getHelpUuid());
        UpdateWrapper<BusinessHelp> updateWrapper = new UpdateWrapper<BusinessHelp>().eq("uuid",businessHelpProblemDto.getHelpUuid());
        if(businessHelpProblemDto.getState()==1){
            updateWrapper.set("yes_help",businessHelp.getYesHelp()+1);
        }else {
            updateWrapper.set("no_help",businessHelp.getNoHelp()+1);
        }
        businessHelpMapper.update(null,updateWrapper);


        BusinessHelpProblem businessHelpProblem1 = new BusinessHelpProblem();
        BeanUtils.copyProperties(businessHelpProblemDto,businessHelpProblem1);
        businessHelpProblem1.setUuid(UUIDUtils.getUUID());
        businessHelpProblemMapper.insert(businessHelpProblem1);
    }


}
