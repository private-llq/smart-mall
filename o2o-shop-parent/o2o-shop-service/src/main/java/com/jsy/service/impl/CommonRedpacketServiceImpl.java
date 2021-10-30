package com.jsy.service.impl;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.domain.CommonRedpacket;
import com.jsy.mapper.CommonRedpacketMapper;
import com.jsy.service.ICommonRedpacketService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lijin
 * @since 2020-11-20
 */
@Service
public class CommonRedpacketServiceImpl extends ServiceImpl<CommonRedpacketMapper, CommonRedpacket> implements ICommonRedpacketService {

    @Override
    public void saveAndUpdate(CommonRedpacket commonRedpacket) {
        LocalDateTime now = LocalDateTime.now();
        if(now.compareTo(commonRedpacket.getBegintime())==-1){
            throw new JSYException(-1,"红包发放时间不能小于当前时间！");
        }
        if(commonRedpacket.getNum()<=0 || commonRedpacket.getMoney()<=0){
            throw new JSYException(-1,"请输入正确的红包数量及金额！");
        }
        try {
            if(!StringUtils.isEmpty(commonRedpacket.getUuid())){
                super.update(commonRedpacket,new QueryWrapper<CommonRedpacket>().eq("uuid",commonRedpacket.getUuid()));
            }else{
                commonRedpacket.setDeleted(0);
                commonRedpacket.setUuid(UUIDUtils.getUUID());
                super.save(commonRedpacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JSYException(-1,"新增店铺红包失败！");
        }
    }

    @Override
    public void deleteById(String uuid) {
        CommonRedpacket commonRedpacket=getByUuid(uuid);
        if(commonRedpacket.getDeleted()!=0){
           throw new JSYException(-1,"启用红包后无法删除！");
        }
        super.remove(new QueryWrapper<CommonRedpacket>().eq("uuid",uuid));
    }

    @Override
    public CommonRedpacket grant(String uuid) {
        CommonRedpacket commonRedpacket=getByUuid(uuid);
        if(Objects.isNull(commonRedpacket)){
            throw new JSYException(-1,"未查询到该店铺红包");
        }
        if(commonRedpacket.getDeleted()!=1){
            throw new JSYException(-1,"该红包已失效");
        }
        if(commonRedpacket.getEndtime().isBefore( LocalDateTime.now())){
            commonRedpacket.setDeleted(2);
            baseMapper.update(commonRedpacket,new QueryWrapper<CommonRedpacket>().eq("uuid",uuid));
            throw new JSYException(-1,"红包活动已结束");
        }
        if(commonRedpacket.getNum()<1){
            throw new JSYException(-1,"很抱歉，红包已经被一抢而空");
        }
        commonRedpacket.setNum(commonRedpacket.getNum()-1);
        baseMapper.update(commonRedpacket,new QueryWrapper<CommonRedpacket>().eq("uuid",uuid));
        return  commonRedpacket;
    }

    @Override
    public CommonRedpacket getByUuid(String uuid) {
        return baseMapper.selectOne(new QueryWrapper<CommonRedpacket>().eq("uuid",uuid));
    }

    @Override
    public Map<String, CommonRedpacket> getMapByUuid(List<String> uuids) {
        List<CommonRedpacket> list=baseMapper.selectList(new QueryWrapper<CommonRedpacket>().in("uuid",uuids));
        Map<String, CommonRedpacket> map=new HashMap<>();
        list.forEach(x->{
            map.put(x.getUuid(),x);
        });
        return map;
    }
}
