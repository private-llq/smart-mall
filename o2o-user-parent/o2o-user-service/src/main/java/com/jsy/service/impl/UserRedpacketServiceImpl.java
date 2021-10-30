package com.jsy.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.basic.util.vo.UserEntity;
import com.jsy.client.ShopClient;
import com.jsy.client.ShopInfoClient;
import com.jsy.domain.CommonRedpacket;
import com.jsy.domain.ShopInfo;
import com.jsy.domain.ShopRedpacket;
import com.jsy.domain.UserRedpacket;
import com.jsy.dto.SelectUserAllRedpacketDto;
import com.jsy.dto.SelectUserNoUserRedPacketDto;
import com.jsy.dto.UserGetRedPacketDto;
import com.jsy.dto.UserRedpacketDTO;
import com.jsy.enums.EnumRedpacketType;
import com.jsy.mapper.UserRedpacketMapper;
import com.jsy.query.UserRedpacketQuery;
import com.jsy.service.IUserRedpacketService;
import com.jsy.vo.ReceiveRedpacketVo;
import lombok.Data;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import javax.xml.bind.SchemaOutputResolver;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserRedpacketServiceImpl extends ServiceImpl<UserRedpacketMapper, UserRedpacket> implements IUserRedpacketService {
    @Resource
    private ShopClient shopClient;

    @Resource
    private  UserRedpacketMapper userRedpacketMapper;

    @Resource
    private ShopInfoClient shopInfoClient;

    //新增抢到的红包
    @Override
    @Transactional
    public boolean insterRedPacket(UserRedpacket userRedpacket) {
        /*int a=1/0;*/
        int insert = userRedpacketMapper.insert(userRedpacket);
        if(insert>0){
            return true;
        }
        return false;
    }
    //查询用户在店铺中没有使用的红包
    @Override
    public List<SelectUserNoUserRedPacketDto> selectUserNoUserRedPacket(String shopUuid) {

        UserEntity userDto = CurrentUserHolder.getUserEntity();//解析token

        if(Objects.isNull(userDto)){
            throw new JSYException(-1,"用户信息验证失败");
        }
        List<SelectUserNoUserRedPacketDto> dtoList=new LinkedList<>();
        List<UserRedpacket> userRedpackets = userRedpacketMapper.selectList(new QueryWrapper<UserRedpacket>().eq("shop_uuid", shopUuid).eq("user_uuid", userDto.getUid()).eq("deleted",1));
       if(userRedpackets.size()>0){
           for (UserRedpacket userRedpacket : userRedpackets) {
               Integer validity = userRedpacket.getValidity();//有效期
               long s= (long) validity*24*60*60*1000;//有效期是多少毫秒
               long l = userRedpacket.getGetTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();//获取时间戳
               long time = new Date().getTime();//当前时间搓
                  if(time-l<s){
                      SelectUserNoUserRedPacketDto dto=new SelectUserNoUserRedPacketDto();
                      BeanUtils.copyProperties(userRedpacket,dto);
                      dto.setStatusA(1);//可以使用
                      long s1 = s+l;
                      LocalDateTime localDateTime = new Date(s1).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
                      dto.setDueTime(localDateTime);//到期时间
                      dtoList.add(dto);
                  }else{
                      SelectUserNoUserRedPacketDto dto=new SelectUserNoUserRedPacketDto();
                      BeanUtils.copyProperties(userRedpacket,dto);
                      dto.setStatusA(0);//不可以使用
                      long s1 = s+l;
                      LocalDateTime localDateTime = new Date(s1).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
                      dto.setDueTime(localDateTime);//到期时间
                      dtoList.add(dto);
                  }

           }
       }
        return dtoList;
    }
    //根据用户和红包uuid查询已领红包数据
    @Override
    public UserRedpacket selectUserRedpacket(String userActiveUuid) {
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        QueryWrapper<UserRedpacket> eq = new QueryWrapper<UserRedpacket>().eq("activitie_uuid", userActiveUuid).eq("user_uuid", userEntity.getUid());
        List<UserRedpacket> userRedpackets = userRedpacketMapper.selectList(eq);

       /* OptionalLong max = userRedpackets.stream().mapToLong(x -> {
            return x.getGetTime().toEpochSecond(ZoneOffset.of("+8"));
        }).max();
        long asLong = max.getAsLong();*/
        if (userRedpackets.size()!=0) {
            UserRedpacket userRedpacket = userRedpackets.stream().sorted(Comparator.comparing(x -> {
                return x.getGetTime().toEpochSecond(ZoneOffset.of("+8"));
            })).collect(Collectors.toList()).get(userRedpackets.size()-1);
            return userRedpacket;
        }else {
            return null;
        }

    }
    //查询所有用户的红包
    @Override
    public List<SelectUserAllRedpacketDto> selectUserAllRedpacket() {
        UserEntity userDto = CurrentUserHolder.getUserEntity();//解析token
        if(Objects.isNull(userDto)){
            throw new JSYException(-1,"用户信息验证失败");
        }
        List<SelectUserAllRedpacketDto> list=new ArrayList<>();//返回数据集合

        QueryWrapper<UserRedpacket> eq = new QueryWrapper<UserRedpacket>().eq("user_uuid", userDto.getUid()).eq("deleted", 1).eq("type","1");

        List<UserRedpacket> userRedpackets = userRedpacketMapper.selectList(eq);
        for (UserRedpacket userRedpacket : userRedpackets) {
            SelectUserAllRedpacketDto selectUserAllRedpacketDto =new SelectUserAllRedpacketDto();
            BeanUtils.copyProperties(userRedpacket,selectUserAllRedpacketDto);
            long l = userRedpacket.getGetTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();//领取的时间
            long v = (long)userRedpacket.getValidity()*24*60*60*1000;//有效期
            long date = v+l;
            String format = new SimpleDateFormat().format(date);
            selectUserAllRedpacketDto.setValidityTime(format);
            String shopName = shopInfoClient.selectShopMessage(userRedpacket.getShopUuid()).getData().getShopName();
            selectUserAllRedpacketDto.setShopName(shopName);
            if (new Date().getTime()>date) {
                selectUserAllRedpacketDto.setStatesUS(1);
            }else {
                selectUserAllRedpacketDto.setStatesUS(0);
            }
            list.add(selectUserAllRedpacketDto);
        }


        return list;
    }
    //根据用户和活动uuid查询已领红包数据集合
    @Override
    public List<UserRedpacket> selectUserRedpacketAll(String activity) {
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        if(Objects.isNull(userEntity)){
            throw new JSYException(-1,"用户信息验证失败");
        }
        String uid = userEntity.getUid();//用户uuid
        List<UserRedpacket> userRedpackets = userRedpacketMapper.selectList(new QueryWrapper<UserRedpacket>().eq("user_uuid", uid).eq("activitie_uuid", activity));
        return userRedpackets;
    }

    /******************************************/
    @Override
    public void receiveRedpacket(ReceiveRedpacketVo receiveRedpacketVo) {
        UserDto userDto=CurrentUserHolder.getCurrentUser();
        if(Objects.isNull(userDto)){
            throw new JSYException(-1,"用户信息验证失败");
        }
        UserRedpacket existRedpacket=baseMapper.selectOne(new QueryWrapper<UserRedpacket>().eq("user_uuid",userDto.getUuid()).eq("type",receiveRedpacketVo.getType()).eq("redpacket_uuid",receiveRedpacketVo.getRedpacketUuid()));
        if(Objects.nonNull(existRedpacket)){
            throw new JSYException(-1,"您已经领取过该红包，请勿重复领取");
        }
        UserRedpacket userRedpacket=new UserRedpacket();
        userRedpacket.setUuid(UUIDUtils.getUUID());
        userRedpacket.setRedpacketUuid(receiveRedpacketVo.getRedpacketUuid());
        userRedpacket.setType(receiveRedpacketVo.getType());

        ObjectMapper mapper=new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());

        if (receiveRedpacketVo.getType().equals(1)){
            /*查看红包是否满足发放条件*/
            CommonResult commonResult;
            try{
                commonResult=shopClient.getCommonRedpacket(receiveRedpacketVo.getRedpacketUuid());
            }catch (Exception e){
                throw new JSYException(-1,"领取失败");
            }
            if(commonResult.getCode()==-1){
                throw new JSYException(-1,commonResult.getMessage());
            }
            CommonRedpacket commonRedpacket=mapper.convertValue(commonResult.getData(),CommonRedpacket.class);
            userRedpacket.setUserUuid(userDto.getUuid());
            userRedpacket.setGetTime(LocalDateTime.now());
            userRedpacket.setMoney(commonRedpacket.getMoney());
            userRedpacket.setDeleted(1);
        }else if (receiveRedpacketVo.getType().equals(2)){
            /*查看红包是否满足发放条件*/
            CommonResult commonResult;
            try {
                commonResult=shopClient.grant(receiveRedpacketVo.getRedpacketUuid());
                System.out.println(commonResult);
            }catch (Exception e){
                throw new JSYException(-1,"领取失败");
            }
            if(commonResult.getCode()==-1){
                throw new JSYException(-1,commonResult.getMessage());
            }
            ShopRedpacket shopRedpacket=mapper.convertValue(commonResult.getData(),ShopRedpacket.class);
            userRedpacket.setUserUuid(userDto.getUuid());
            userRedpacket.setShopUuid(shopRedpacket.getShopUuid());
            userRedpacket.setGetTime(LocalDateTime.now());
            userRedpacket.setMoney(shopRedpacket.getMoney());
            userRedpacket.setDeleted(1);
        }else {
            throw new JSYException(-1,"红包类型有误");
        }
        save(userRedpacket);
    }

    @Override
    public PageList<UserRedpacketDTO> queryByPage(UserRedpacketQuery query) {
       // UserDto userDto=CurrentUserHolder.getCurrentUser();
        UserEntity userDto = CurrentUserHolder.getUserEntity();
        if(Objects.isNull(userDto)){
            throw new JSYException(-1,"用户信息验证失败");
        }
        Page<UserRedpacket> page = new Page<UserRedpacket>(query.getPage(),query.getRows());

        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_uuid", userDto.getUid());
        queryWrapper.orderByAsc("deleted");
        queryWrapper.orderByDesc("get_time");
        page = super.page(page,queryWrapper);
        if(CollectionUtils.isEmpty(page.getRecords())){
            return new PageList<UserRedpacketDTO>(page.getTotal(),null);
        }
        List<String> shopRedpacketUuids=new ArrayList<>();
        List<String> commonRedpacketUuids=new ArrayList<>();
        Set<String> shopUuids=new HashSet<>();
        page.getRecords().forEach(x->{
            if(x.getType().equals(1)){
                commonRedpacketUuids.add(x.getRedpacketUuid());
            }
            if(x.getType().equals(2)){
                shopRedpacketUuids.add(x.getRedpacketUuid());
                shopUuids.add(x.getShopUuid());
            }
        });
        Map<String, CommonRedpacket> commonMap = null;
        Map<String, ShopRedpacket> shopMap = null;
        Map<String, ShopInfo> shopInfoMap = null;
        if(!CollectionUtils.isEmpty(commonRedpacketUuids)){
            commonMap=shopClient.getMapByUuid(commonRedpacketUuids);
        }
        if(!CollectionUtils.isEmpty(shopRedpacketUuids)){
            shopMap=shopClient.getShopMapByUuid(shopRedpacketUuids);
            shopInfoMap=shopClient.getShopInfoMapByUuid(new ArrayList<>(shopUuids));
        }
        List<UserRedpacketDTO> userRedpacketDTOList=new ArrayList<>();

        for (UserRedpacket x : page.getRecords()) {
            UserRedpacketDTO dto=new UserRedpacketDTO();
            dto.setUuid(x.getUuid());
            dto.setType(x.getType());
            dto.setTypeName(EnumRedpacketType.getName(x.getType()));
            dto.setRedpacketUuid(x.getRedpacketUuid());
            dto.setGetTime(x.getGetTime());
            dto.setUsedTime(x.getUsedTime());
            dto.setShopUuid(x.getShopUuid());
            if(x.getType().equals(1)){
                CommonRedpacket commonRedpacket=commonMap.get(x.getRedpacketUuid());
                dto.setBegintime(commonRedpacket.getBegintime());
                dto.setEndtime(commonRedpacket.getEndtime());
                dto.setMoney(commonRedpacket.getMoney());
            }
            if(x.getType().equals(2)){
                ShopRedpacket shopRedpacket=shopMap.get(x.getRedpacketUuid());
                dto.setBegintime(shopRedpacket.getBegintime());
                dto.setEndtime(shopRedpacket.getEndtime());
                dto.setMoney(shopRedpacket.getMoney());
                dto.setShopName(shopInfoMap.get(x.getShopUuid()).getShopName());
            }
            if(x.getDeleted()==1){
                dto.setUseType("待使用");
            }else {
                dto.setUseType("已使用");
            }
            userRedpacketDTOList.add(dto);
        }
        return new PageList<UserRedpacketDTO>(page.getTotal(),userRedpacketDTOList);
    }

    @Override
    public UserRedpacket getByUuid(String uuid) {
        QueryWrapper<UserRedpacket> uuid1 = new QueryWrapper<UserRedpacket>().eq("uuid", uuid).eq("deleted",1);
        UserRedpacket userRedpacket = baseMapper.selectOne(uuid1);
        return userRedpacket;
    }

    @Override
    public List<UserRedpacketDTO> queryByShop(String shopUuid) {
        UserDto userDto=CurrentUserHolder.getCurrentUser();
        if(Objects.isNull(userDto)){
            throw new JSYException(-1,"用户信息验证失败");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("userUuid",userDto.getUuid());
        map.put("shopUuid",shopUuid);
        map.put("time",new Date());
        List<UserRedpacketDTO> userRedpackets=baseMapper.queryUsableRedpacket(map);
        userRedpackets.forEach(x->{
            x.setTypeName(EnumRedpacketType.getName(x.getType()));
        });
        return userRedpackets;
    }
    @Override
    public void useRedpacket(String uuid) {
        UserDto userDto=CurrentUserHolder.getCurrentUser();
        if(Objects.isNull(userDto)){
            throw new JSYException(-1,"用户信息验证失败"); 
        }
        UserRedpacket userRedpacket=baseMapper.selectOne(new QueryWrapper<UserRedpacket>().eq("uuid",uuid));
        if(!userRedpacket.getUserUuid().equals(userDto.getUuid())){
            throw new JSYException(-1,"该红包不属于该用户");
        }
        if(!userRedpacket.getDeleted().equals(1)){
            throw new JSYException(-1,"该红包不可使用");
        }
        UserRedpacket updateRedpacket=new UserRedpacket();
        updateRedpacket.setDeleted(2);
        updateRedpacket.setUsedTime(LocalDateTime.now());
        baseMapper.update(updateRedpacket,new QueryWrapper<UserRedpacket>().eq("uuid",uuid));
    }
}
