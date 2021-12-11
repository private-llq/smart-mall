package com.jsy.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.Timetable;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface IUserSearchHistoryService  {
    //新增搜索记录
    Boolean addSearchHistoryByUserId(Long userId, String searchkey);
    //删除个人历史数据
    Long delSearchHistoryByUserId(Long userId, String searchkey);
    //获取个人历史数据列表
    List<String> getSearchHistoryByUserId(Long userId);
    //新增一条热词搜索记录，将用户输入的热词存储下来
    Boolean incrementScoreByUserId(String searchkey);
    //根据searchkey搜索其相关最热的前十名 (如果searchkey为null空，则返回redis存储的前十最热词条
    List<String> getHotList(String searchkey);
    //每次点击给相关词searchkey热度+1
    Boolean incrementScore(String searchkey);
    //查询搜索历史
    List<String> selectSearchResultList(Long userId);
    //新增搜索记录
    Boolean addSearchKey(Long userId,String searchkey);
}
