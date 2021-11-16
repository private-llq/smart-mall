package com.jsy.basic.util;

import java.util.ArrayList;
import java.util.List;

//分页对象：easyui只需两个属性，total(总数),datas（分页数据）就能实现分页
public class PageList<T> {
    /**
     * 总条数
     */
    private Long total=0L;
    private List<T> rows = new ArrayList<T>();
    /**
     * 当前页
     */
    private long current = 1;

    public long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "PageList{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }

    //提供有参构造方法，方便测试
    public PageList(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }
    //除了有参构造方法，还需要提供一个无参构造方法
    public PageList() {
    }
}
