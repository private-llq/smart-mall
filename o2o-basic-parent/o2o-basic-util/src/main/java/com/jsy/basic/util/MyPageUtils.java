package com.jsy.basic.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPageUtils {

    public static <T> PageInfo<T> pageMap(List<T> datas, int pageSize, int pageIndex){
        //HashMap<String, Object> map = new HashMap<>();
        PageInfo<T> pageInfo = new PageInfo<>();
        int size = datas.size();//总条数
        //总页数
        int totlePage = size / pageSize;//取整
        int a = size % pageSize;//取余
        if (a > 0) {
        }

        //map.put("pages", totlePage);//总页数
        //map.put("pageNum", pageIndex);//页码
        pageInfo.setCurrent(pageIndex);
        //map.put("pageSize", pageSize);//每页显示条数
        pageInfo.setSize(pageSize);
        //map.put("total", size);//总条数
        pageInfo.setTotal(size);
        int startNum = (pageIndex-1)* pageSize+1 ;                     //起始截取数据位置
        if(startNum > datas.size()){
            return null;
        }
        List<T> res = new ArrayList<>();
        int rum = datas.size() - startNum;
        if(rum < 0){
            return null;
        }
        if(rum == 0){                                               //说明正好是最后一个了
            int index = datas.size() -1;
            res.add(datas.get(index));
//            return map;
        }
        if(rum / pageSize >= 1){                                    //剩下的数据还够1页，返回整页的数据
            for(int i=startNum;i<startNum + pageSize;i++){          //截取从startNum开始的数据
                res.add(datas.get(i-1));
            }
        }else if((rum / pageSize == 0) && rum > 0){                 //不够一页，直接返回剩下数据
            for(int j = startNum ;j<=datas.size();j++){
                res.add(datas.get(j-1));
            }
        }
        //map.put("size",res.size());//当前页总条数
        //map.put("list",res); //分页当前页list数据
        pageInfo.setRecords(res);
        return pageInfo;
    }

}
