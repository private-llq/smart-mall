package com.jsy.utils;

import com.jsy.basic.util.exception.JSYException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class ListPagHelper<T> {
    private Integer total; //查询总数
    private List<T> records; //返回的数据


    public ListPagHelper(List<T> list, int pageNum, int pageSize) {

        this.total = list.size();
        int size = pageNum * pageSize > total ? total : pageNum * pageSize;

        int startNum = (pageNum - 1) * pageSize;
        if (startNum > size){
            startNum = 1;
        }
        records = list.subList(startNum, size);
    }

    public static <T> List<T> paging(List<T> list, int pageNum, int pageSize){
        int total = list.size();
        int size = pageNum * pageSize > total ? total : pageNum * pageSize;

        int startNum = (pageNum - 1) * pageSize;
        if (startNum > size){
            startNum = 1;
        }
        return list.subList(startNum, size);
    }


    /**
     * 将一个 list 集合, 分割成指定大小的"块"
     *
     * 主要用于大数据量的入库
     * 注: 此方法进行分割的 list 为只读状态, 不能进行其他操作
     * @param list 原始 list 列表
     * @param batchSize 指定每一"块"的数量
     * @return
     */
    public static <T> List<List<T>>  subList(List<T> list, int batchSize){
        //总数
        int len = list.size();
        if (batchSize == 0){
            batchSize = 1;
        }
        //轮数
        int times = len / batchSize;
        //如果不能整除，则要多跑一轮
        if(len%batchSize != 0){
            times++;
        }
        log.info("总共 {} 条记录, 分割为 {} 块", len, times);
        List<List<T>> resList = new ArrayList<>(times);
        int start = 1;
        int end = 1;

        for(int i=1;i<=times; i++){
            start = (i-1) * batchSize;
            end = start + batchSize;
            //最后一轮数据未满
            if(end>=len){
                end = len;
            }
            //子集为开区间[0,len)  =[0, len-1]
            List<T> subNewList = list.subList(start,end);
            resList.add(subNewList);
        }
        return resList;
    }
    public static <T> List<List<T>>  subList(List<T> list){
        return subList(list, 10000);
    }

    /**
     * 将一个 list 集合, 按指定数量分割成块
     * 主要用于大数据量的入库
     * 注: 此方法进行分割的 list 为只读状态, 不能进行其他操作
     * @param times 指定要分割的"块"数
     * @param list 原始 list 列表
     * @return
     */
    public static <T> List<List<T>>  subList(Integer times, List<T> list){
        //总数
        int len = list.size();
        if (times > len){
            throw new JSYException(500,"超出可分块数量");
        }
        // 使用 int 型计算每页数量会自动舍去小数位, 可能导致最终分割数量超出指定数
        // 这里需要先转换成 double 型计算每"块"数量, 再将计算结果向上取整
        double block = times.doubleValue();
        int batchSize = (int) Math.ceil(len / block);

        log.info("总共 {} 条记录, 分割为 {} 块, 每块 {} 条", len, times, batchSize);
        List<List<T>> resList = new ArrayList<>(times);
        int start = 1;
        int end = 1;

        for(int i=1;i<=times; i++){
            start = (i-1) * batchSize;
            end = start + batchSize;
            //最后一轮数据未满
            if(end>=len){
                end = len;
            }
            //子集为开区间[0,len)  =[0, len-1]
            List<T> subNewList = list.subList(start,end);
            resList.add(subNewList);
        }
        return resList;
    }


    public int getTotal() {
        return total;
    }

    public List<T> getDataList() {
        return records;
    }
}
