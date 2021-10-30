package com.jsy.service.impl;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.RedisStateCache;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.*;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.domain.ShopOwner;
import com.jsy.dto.LoginDTO;
import com.jsy.mapper.ShopInfoMapper;
import com.jsy.mapper.ShopOwnerMapper;
import com.jsy.query.ShopOwnerQuery;
import com.jsy.service.IShopOwnerService;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
 * @since 2020-11-12
 */
@Service
public class ShopOwnerServiceImpl extends ServiceImpl<ShopOwnerMapper, ShopOwner> implements IShopOwnerService {

    static final String  group="LoginCode:";

    @Autowired
    private ShopInfoMapper shopInfoMapper;

    @Autowired
    private RedisStateCache redisStateCache;

    @Autowired
    private ShopOwnerMapper ShopOwnerMapper;

    @Override
    public PageList<ShopOwner> queryByPage(ShopOwnerQuery query) {
        Page<ShopOwner> page = new Page<ShopOwner>(query.getPage(),query.getRows());
        page = super.page(page);
        QueryWrapper queryWrapper=new QueryWrapper();
        if(Objects.nonNull(query.getName())){
            queryWrapper.like("name",query.getName());
        }
        if(Objects.nonNull(query.getPhone())){
            queryWrapper.like("phone",query.getPhone());
        }
        return new PageList<ShopOwner>(page.getTotal(),page.getRecords());
    }

    @Override
    public LoginDTO queryShopOwnerLogin(ShopOwner shopOwner) {
        if(Objects.isNull(shopOwner.getPhone()) || shopOwner.getPhone()==""){
            throw new JSYException(-1,"请输入电话号码");
        }
        if(Objects.isNull(shopOwner.getPassword()) || shopOwner.getPassword()==""){
            throw new JSYException(-1,"请输入密码");
        }
        String s = shopInfoMapper.selectShopStatus(shopOwner.getPhone());
        if ("0".equals(s)) {
            throw new JSYException(-1,"店铺正在审核，请耐心等待");
        }
        //查询当前用户信息
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("phone",shopOwner.getPhone());
        ShopOwner userDto=baseMapper.selectOne(queryWrapper);

        if (null == userDto) {
            throw new JSYException(-1,"登录失败，该电话号码未注册用户！");
        }
        //加密密码
        if (!shopOwner.getPassword().equals(userDto.getPassword())) {
            throw new JSYException(-1,"电话号码或者密码错误");
        }
        UserDto returnUser=new UserDto();
        BeanUtils.copyProperties(userDto,returnUser);
        returnUser.setUserType(2);
        //查询单位信息
        String token = JwtUtils.createUserToken(returnUser);

        LoginDTO loginDTO=new LoginDTO();
        loginDTO.setToken(token);
        loginDTO.setUser(returnUser);
        return loginDTO;
    }

    @Override
    public String updateShopOwner(ShopOwner shopOwner) {
        UserDto dto= CurrentUserHolder.getCurrentUser();
        if(!shopOwner.getUuid().equals(dto.getUuid())){
            throw new JSYException(-1,"用户信息验证不通过");
        }
        shopOwner.setUuid(null);
        if(StringUtils.isEmpty(shopOwner.getPhone())){
            ShopOwner getShopOwner=getByUuid(dto.getUuid());
            if(getShopOwner.getPhone().equals(shopOwner.getPhone())){
                return shopOwner.getUuid();
            }
            List<ShopOwner> userList=baseMapper.selectList(new QueryWrapper<ShopOwner>().eq("phone",shopOwner.getPhone()));
            if (userList.size()>0){
                throw new JSYException(-1,"该电话号码已经被注册");
            }
        }
        if(StringUtils.isEmpty(shopOwner.getPassword()) && (shopOwner.getPassword().length()<6 || shopOwner.getPassword().length()>12)){
            throw new JSYException(-1,"密码请保持在6-12位之间");
        }
        baseMapper.update(shopOwner,new QueryWrapper<ShopOwner>().eq("uuid",shopOwner.getUuid()));
        return shopOwner.getUuid();
    }

    @Override
    public String addShopOwner(ShopOwner shopOwner) {
        if(StringUtils.isEmpty(shopOwner.getPhone()) || StringUtils.isEmpty(shopOwner.getPassword())){
            throw new JSYException(-1,"请填写注册信息");
        }
        if(!RegexUtils.isMobile(shopOwner.getPhone())){
            throw new JSYException(-1,"请填写正确的手机号码");
        }
        if (shopOwner.getPassword().length()>6 && shopOwner.getPassword().length()<12){
            throw new JSYException(-1,"密码请保持在6-12位之间");
        }
        List<ShopOwner> shopOwnerList=baseMapper.selectList(new QueryWrapper<ShopOwner>().eq("phone",shopOwner.getPhone()));
        if (shopOwnerList.size()>0){
            throw new JSYException(-1,"该电话号码已经被注册");
        }
        shopOwner.setUuid(UUIDUtils.getUUID());
        baseMapper.insert(shopOwner);
        return shopOwner.getUuid();
    }

    @Override
    public ShopOwner getByUuid(String uuid) {
        return baseMapper.selectOne(new QueryWrapper<ShopOwner>().eq("uuid",uuid));
    }

    @Override
    public ShopOwner getByRelationUid(String uuid) {
        return baseMapper.getByRelationUid(uuid);
    }

    @Override
    public boolean sendSmsPassword(String phoneNumber) {
        if (Objects.isNull(phoneNumber)){
            throw new JSYException(-1,"请先填写手机号");
        }

        if(!RegexUtils.isMobile(phoneNumber)){
            throw new JSYException(-1,"请填写正确的手机号码！");
        }

        String url = "http://smsbanling.market.alicloudapi.com/smsapis";

        String appCode = "abfc59f0cdbc4c038a2e804f9e9e37de";
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Authorization", "APPCODE " + appCode);

        //验证码生成
        String code = String.valueOf((int)((Math.random()*9+1)*Math.pow(10,5)));

        //模板
        String template=",验证码5分钟内有效。工作人员不会向您索要,请勿向任何人泄露,以免造成账户或资金损失。";

        Map<String, String> queryParam = new HashMap<>(2);
        queryParam.put("mobile",phoneNumber);//手机号
        queryParam.put("msg",code+template);//内容
        queryParam.put("sign","e店保");//头标

        redisStateCache.cache(group+phoneNumber,code,5*60);//5分钟过期

        //发送短信
        HttpGet httpGet = MyHttpUtils.httpGet(url,queryParam);
        MyHttpUtils.setHeader(httpGet,headers);
        String result = (String) MyHttpUtils.exec(httpGet,1);
        //验证结果
        if ( Objects.isNull(result) ){
            return false;
        }
        Integer resultCode = JSON.parseObject(result).getInteger("result");
        if( resultCode != 0 ){
            return false;
        }
        return true;
    }

    @Override
    public String loginSms(String phoneNumber, Integer userCode) {

        if(!RegexUtils.isMobile(phoneNumber)){
            throw new JSYException(-1,"请填写正确的手机号码！");
        }
        //redis验证码
        String code = redisStateCache.get(group + phoneNumber);

        if (code==null||code==""){
            throw new JSYException(-1,"验证码已失效！");
        }

        if (!Integer.valueOf(code).equals(userCode)){
            throw new JSYException(-1,"验证码错误！");
        }

        ShopOwner dbOwner = ShopOwnerMapper.selectOne(new QueryWrapper<ShopOwner>().eq("phone", phoneNumber));
        if (Objects.isNull(dbOwner)){
            //验证成功，保存手机号码到数据库
            ShopOwner shopOwner = new ShopOwner();
            shopOwner.setPhone(phoneNumber);
            shopOwner.setUuid(UUIDUtils.getUUID());
            shopOwner.setName(phoneNumber);//默认用户名就是手机号
            ShopOwnerMapper.insert(shopOwner);

            ShopOwner dbOwner1 = ShopOwnerMapper.selectOne(new QueryWrapper<ShopOwner>().eq("phone", phoneNumber));
            //返回当前登录商家用户信息
            UserDto returnUser=new UserDto();
            BeanUtils.copyProperties(dbOwner1,returnUser);
            returnUser.setUserType(2);
            String token = JwtUtils.createUserToken(returnUser);
            return token;
        }
        //返回当前登录商家用户信息
        UserDto returnUser=new UserDto();
        BeanUtils.copyProperties(dbOwner,returnUser);
        returnUser.setUserType(2);
        String token = JwtUtils.createUserToken(returnUser);
        return token;
    }

}
