package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.domain.ShopWithdraw;
import com.jsy.dto.SelectWithdrawDto;
import com.jsy.dto.SelectWithdrawMonthDto;
import com.jsy.mapper.ShopCapitalMapper;
import com.jsy.mapper.ShopWithdrawMapper;
import com.jsy.query.AddCapitalParam;
import com.jsy.query.AddShopWithdrawOneParam;
import com.jsy.query.SelectWithdraParam;
import com.jsy.query.SelectWithdrawMonthParam;
import com.jsy.service.IShopCapitalService;
import com.jsy.service.IShopWithdrawService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商家提现记录表 服务实现类
 * </p>
 *
 * @author arli
 * @since 2021-12-24
 */
@Service
@Slf4j
public class ShopWithdrawServiceImpl extends ServiceImpl<ShopWithdrawMapper, ShopWithdraw> implements IShopWithdrawService {
    @Resource
    private IShopCapitalService service;

    @Resource
    private ShopWithdrawMapper shopWithdrawMapper;
    //新增一条提现记录
    @Override
    @Transactional
    public Boolean addShopWithdrawOne(AddShopWithdrawOneParam param) {
        ShopWithdraw entity = new ShopWithdraw();
        entity.setCreateDate(LocalDate.now());//创建时间
        BeanUtils.copyProperties(param,entity);
        int insert = shopWithdrawMapper.insert(entity);//增加提现记录
        if (insert>0) {
            AddCapitalParam param1 = new AddCapitalParam();
            param1.setShopId(entity.getShopId());
            param1.setMoney(entity.getMoney());
            Boolean aBoolean = service.subtractCapital(param1);//减少店铺余额
            if(aBoolean){
                return true;
            }
        }
        return false;
    }
    //查询提现记录
    @Override
    public List<SelectWithdrawDto> selectWithdraw(SelectWithdraParam param) {
        Integer page = param.getPage();//当前页数
        Integer size = param.getSize();//每页多少条
        Integer start=(page-1)*size;//开始位置
        List<SelectWithdrawDto> selectWithdrawDtos=new ArrayList<>();//返回对象
        for (Integer i = 0; i < size; i++) {
            LocalDate now = LocalDate.now();
            LocalDate localDate = now.plusDays(-start);
            LocalDate yesterday = localDate.plusDays(-i);
            QueryWrapper<ShopWithdraw> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shop_id",param.getShopId());
            queryWrapper.eq("create_date",yesterday);
            List<ShopWithdraw> shopWithdraws = shopWithdrawMapper.selectList(queryWrapper);
            SelectWithdrawDto selectWithdrawDto=new SelectWithdrawDto();
            selectWithdrawDto.setDate(yesterday);
            selectWithdrawDto.setSize(shopWithdraws.size());
            selectWithdrawDto.setShopWithdraws(new ArrayList<>());
            selectWithdrawDto.setShopWithdraws(shopWithdraws);
            boolean add = selectWithdrawDtos.add(selectWithdrawDto);
        }
        return selectWithdrawDtos;
    }


    //查询某个月提现记录
    @Override
    public SelectWithdrawMonthDto selectWithdrawMonth(SelectWithdrawMonthParam param) {
        SelectWithdrawMonthDto dto=new SelectWithdrawMonthDto();


        String year = param.getYear();
        String month = param.getMonth();
        Long shopId = param.getShopId();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {
            parse = dateFormat.parse(year + "-" + month + "-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LocalDateTime localDateTime = parse.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime firstday = localDateTime.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime lastDay = localDateTime.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate localDate = firstday.toLocalDate();
        LocalDate localDate1 = lastDay.toLocalDate();

        Period between = Period.between(localDate,localDate1);



       Integer total=0;

        List<SelectWithdrawDto> selectWithdrawDtos=new ArrayList<>();
        for (int i = 0; i <=between.getDays(); i++) {

            LocalDate Time = localDate.minusDays(-i);
            QueryWrapper<ShopWithdraw> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shop_id",shopId);
            queryWrapper.eq("create_date",Time);
            List<ShopWithdraw> shopWithdraws = shopWithdrawMapper.selectList(queryWrapper);
            SelectWithdrawDto selectWithdrawDto=new SelectWithdrawDto();
            selectWithdrawDto.setShopWithdraws(new ArrayList<>());
            selectWithdrawDto.getShopWithdraws().addAll(shopWithdraws);
            selectWithdrawDto.setDate(Time);
            selectWithdrawDto.setSize(shopWithdraws.size());
            total=total+shopWithdraws.size();//当月的总数量
            boolean add = selectWithdrawDtos.add(selectWithdrawDto);

        }

        dto.setYearMonth(year+":"+month);
        dto.setSize(total);
        dto.setSelectWithdrawDtos(new ArrayList<>());
        dto.setSelectWithdrawDtos(selectWithdrawDtos);

        return dto;
    }
}
