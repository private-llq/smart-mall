package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.domain.ShopBill;
import com.jsy.dto.SelectBillDto;
import com.jsy.dto.SelectSevenBillAccompanyDto;
import com.jsy.dto.SelectSevenBillDto;
import com.jsy.mapper.ShopBillMapper;
import com.jsy.query.*;
import com.jsy.service.IShopBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.service.IShopCapitalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商家账单表 服务实现类
 * </p>
 *
 * @author arli
 * @since 2021-12-24
 */
@Service
@Slf4j
public class ShopBillServiceImpl extends ServiceImpl<ShopBillMapper, ShopBill> implements IShopBillService {
    @Resource
    private IShopCapitalService shopCapitalService;
    @Resource
    private ShopBillMapper  shopBillMapper;
    //添加一条收入/退款记录
    @Override
    @Transactional
    public Boolean addShopBillOne(AddShopBillOneParam param) {
        ShopBill entity = new ShopBill();
        BeanUtils.copyProperties(param,entity);
        LocalDateTime now = LocalDateTime.now();
        entity.setYear(now.getYear());
        entity.setMonth(now.getMonthValue());
        entity.setDay(now.getDayOfMonth());
        int insert = shopBillMapper.insert(entity);
        if (insert>0) {
            if(entity.getBillType()==0){//0收入
                AddCapitalParam param1 = new AddCapitalParam();
                param1.setShopId(entity.getShopId());
                param1.setMoney(entity.getMoney());
                Boolean aBoolean = shopCapitalService.addCapital(param1);
                if(aBoolean){
                    return true;
                }
            }
            if(entity.getBillType()==1){//1退款
                AddCapitalParam param1 = new AddCapitalParam();
                param1.setShopId(entity.getShopId());
                param1.setMoney(entity.getMoney());
                Boolean aBoolean = shopCapitalService.subtractCapital(param1);
                if(aBoolean){
                    return true;
                }
            }

        }
        return false;
    }




    //分页查询店铺收入、退款记录
    @Override
    public List<SelectBillDto> selectBill(SelectBillParam param) {
        List<SelectBillDto> selectBillDtos=new ArrayList<>();//返回数据
        Integer page = param.getPage();//当前页数
        Integer size = param.getSize();//每页多少条
        Integer start=(page-1)*size;//开始位置
        for (Integer i = 0; i <size; i++) {
            Calendar calendar = Calendar.getInstance();//当前时间
            Date date = calendar.getTime();
            log.info("当前时间"+date);
            calendar.add(Calendar.DATE,-start);//根据开始位置获取时间
            calendar.add(Calendar.DATE,-i);//加多少天
            Date time = calendar.getTime();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;// 记得加1
            int day = calendar.get(Calendar.DATE);
            log.info("当前时间"+time.toString()+"年"+year+"月"+month+"日"+day);
            QueryWrapper<ShopBill> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shop_id",param.getShopId());//店铺id
            queryWrapper.orderByDesc("create_time");//创建时间倒序排序
            queryWrapper.eq("year",year);
            queryWrapper.eq("month",month);
            queryWrapper.eq("day",day);
            List<ShopBill> shopBills = shopBillMapper.selectList(queryWrapper);
           log.info(shopBills.toString());

            SelectBillDto selectBillDto=new SelectBillDto();//一日数据返回对象

            selectBillDto.setDate(year+":"+month+":"+day);
            selectBillDto.setAmount(shopBills.size());
            BigDecimal income=BigDecimal.ZERO;//收入
            BigDecimal refund=BigDecimal.ZERO;//退款
            for (ShopBill shopBill : shopBills) {//0收入1退款
                if (shopBill.getBillType()==0) {//收入
                    income=income.add(shopBill.getMoney());
                }
                if (shopBill.getBillType()==1) {//退款
                    refund=refund.add(shopBill.getMoney());
                }
            }
            selectBillDto.setTotalMoney(income.subtract(refund));//当日收益（收入加退款）
            selectBillDto.setBillDataList(new ArrayList<>());//开辟空间
            selectBillDto.setBillDataList(shopBills);
            boolean add = selectBillDtos.add(selectBillDto);
        }
        return selectBillDtos;
    }


    //查询某一天店铺收入、退款记录
    @Override
    public SelectBillDto selectBillOneDay(SelectBillOneDayParam param) {


        LocalDateTime time = param.getTime();
        int year = time.getYear();//年
        int dayOfMonth = time.getDayOfMonth();//这个月的几号
        int monthValue = time.getMonthValue();//月
        QueryWrapper<ShopBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id",param.getShopId());//店铺id
        queryWrapper.orderByDesc("create_time");//创建时间倒序排序
        queryWrapper.eq("year",year);
        queryWrapper.eq("month",monthValue);
        queryWrapper.eq("day",dayOfMonth);
        List<ShopBill> shopBills = shopBillMapper.selectList(queryWrapper);
        log.info(shopBills.toString());

        SelectBillDto selectBillDto=new SelectBillDto();//一日数据返回对象
        selectBillDto.setDate(year+":"+monthValue+":"+dayOfMonth);
        selectBillDto.setAmount(shopBills.size());
        BigDecimal income=BigDecimal.ZERO;//收入
        BigDecimal refund=BigDecimal.ZERO;//退款
        for (ShopBill shopBill : shopBills) {//0收入1退款
            if (shopBill.getBillType()==0) {//收入
                income=income.add(shopBill.getMoney());
            }
            if (shopBill.getBillType()==1) {//退款
                refund=refund.add(shopBill.getMoney());
            }
        }
        selectBillDto.setTotalMoney(income.subtract(refund));//当日收益（收入加退款）
        selectBillDto.setBillDataList(new ArrayList<>());//开辟空间
        selectBillDto.setBillDataList(shopBills);
        return selectBillDto;
    }
    //查询某一时间段店铺收入、退款记录
    @Override
    public List<SelectBillDto> selectBillSomeTime(SelectBillSomeTimeParam param) {
        List<SelectBillDto> selectBillDtos=new ArrayList<>();//返回数据
        LocalDateTime startTime = param.getStartTime();
        LocalDateTime endTime = param.getEndTime();
        LocalDate  startLocalDate = startTime.toLocalDate();
        LocalDate endLocalDate = endTime.toLocalDate();
        Period between = Period.between(startLocalDate,endLocalDate);
        int days = between.getDays();
        log.info(days+"天");
        for (int i = 0; i <= days; i++) {
            LocalDate Time = endLocalDate.minusDays(i);
            int year = Time.getYear();//年
            int dayOfMonth = Time.getDayOfMonth();//这个月的几号
            int monthValue = Time.getMonthValue();//月
            QueryWrapper<ShopBill> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shop_id",param.getShopId());//店铺id
            queryWrapper.orderByDesc("create_time");//创建时间倒序排序
            queryWrapper.eq("year",year);
            queryWrapper.eq("month",monthValue);
            queryWrapper.eq("day",dayOfMonth);
            List<ShopBill> shopBills = shopBillMapper.selectList(queryWrapper);
            log.info(shopBills.toString());

            SelectBillDto selectBillDto=new SelectBillDto();//一日数据返回对象
            selectBillDto.setDate(year+":"+monthValue+":"+dayOfMonth);
            selectBillDto.setAmount(shopBills.size());
            BigDecimal income=BigDecimal.ZERO;//收入
            BigDecimal refund=BigDecimal.ZERO;//退款
            for (ShopBill shopBill : shopBills) {//0收入1退款
                if (shopBill.getBillType()==0) {//收入
                    income=income.add(shopBill.getMoney());
                }
                if (shopBill.getBillType()==1) {//退款
                    refund=refund.add(shopBill.getMoney());
                }
            }
            selectBillDto.setTotalMoney(income.subtract(refund));//当日收益（收入加退款）
            selectBillDto.setBillDataList(new ArrayList<>());//开辟空间
            selectBillDto.setBillDataList(shopBills);

            boolean add = selectBillDtos.add(selectBillDto);

        }
        return selectBillDtos;
    }
    //近N日成交额：统计最近N天完成交易的订单（不包括退款售后订单金额）
    @Override
    public SelectSevenBillDto selectSevenBill(SelectSevenBillParam param) {
        SelectSevenBillDto sevenBillDto=new SelectSevenBillDto();//返回数据
        sevenBillDto.setList(new ArrayList<>());
        BigDecimal totalAll=BigDecimal.ZERO;
        Integer number = param.getNumber();
        Long shopId = param.getShopId();
        LocalDate now = LocalDate.now();

        List<SelectSevenBillAccompanyDto> list =new ArrayList();
        for (Integer i = 0; i < number; i++) {
            LocalDate localDate = now.minusDays(i);
            QueryWrapper<ShopBill> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shop_id",shopId);
            queryWrapper.eq("year", localDate.getYear());
            queryWrapper.eq("month",localDate.getMonthValue());
            queryWrapper.eq("day",localDate.getDayOfMonth());
            List<ShopBill> shopBills = shopBillMapper.selectList(queryWrapper);
            BigDecimal totalOne=BigDecimal.ZERO;
            for (ShopBill shopBill : shopBills) {
                if (shopBill.getBillType()==0) {//收入
                    totalOne=totalOne.add(shopBill.getMoney());
                }else if(shopBill.getBillType()==1){//退款
                    totalOne=totalOne.subtract(shopBill.getMoney());
                }

            }
            SelectSevenBillAccompanyDto sevenBillAccompanyDto=new SelectSevenBillAccompanyDto();
            sevenBillAccompanyDto.setTime(localDate);
            sevenBillAccompanyDto.setOneDaymoney(totalOne);
            totalAll.add(totalOne);
            list.add(sevenBillAccompanyDto);

        }

        sevenBillDto.getList().addAll(list);
        sevenBillDto.setTotal(totalAll);


        return sevenBillDto;
    }


}
