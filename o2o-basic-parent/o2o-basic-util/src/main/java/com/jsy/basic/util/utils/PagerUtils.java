package com.jsy.basic.util.utils;

import com.jsy.basic.util.exception.JSYException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
  * @Author lxr
  * @Description 代码实现分页
  * @Date 2020/12/4 10:18
  * @Param
  **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class  PagerUtils<T> {

    private Integer total; //查询总数
    private List<T> records; //返回的数据

    //集合手动分页
    public PagerUtils<T> queryPage(int page, int size, List<T> list) {
        int start = (page - 1) * size;
        int end = size * page;

        PagerUtils<T> utils = new PagerUtils<>();
        if (list.size()!=0) {
            //end 超过总长
            if (end > list.size()) {
                end = list.size();
            }
            if (start > end) {
                throw new JSYException(500,"页码错误,重新输入");
            }

            List<T> ts = list.subList(start, end);

            utils.setTotal(list.size());
            utils.setRecords(ts);
        }else if(list.size()==0){
            utils.setTotal(0);
            utils.setRecords(list);
        }

        return utils;



    }

}
