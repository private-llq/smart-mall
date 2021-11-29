package com.jsy.controller;
import com.jsy.service.INewReplyService;
import com.jsy.domain.NewReply;
import com.jsy.query.NewReplyQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/newReply")
public class NewReplyController {
    @Autowired
    public INewReplyService newReplyService;

    /**
    * 保存和修改公用的
    * @param newReply  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody NewReply newReply){
        try {
            if(newReply.getId()!=null){
                newReplyService.updateById(newReply);
            }else{
                newReplyService.save(newReply);
            }
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1,"操作失败！");

        }
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @DeleteMapping(value="/{id}")
    public CommonResult delete(@PathVariable("id") Long id){
        try {
            newReplyService.removeById(id);
            return CommonResult.ok();
        } catch (Exception e) {
        e.printStackTrace();
            return  CommonResult.error(-1,"删除失败！");
        }
    }

    /**
    * 根据id查询一条
    * @param id
    */
    @GetMapping(value = "/{id}")
    public NewReply get(@PathVariable("id")Long id)
    {
        return newReplyService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public List<NewReply> list(){

        return newReplyService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<NewReply> json(@RequestBody NewReplyQuery query)
    {
        Page<NewReply> page = new Page<NewReply>(query.getPage(),query.getRows());
        page = newReplyService.page(page);
        return new PageList<NewReply>(page.getTotal(),page.getRecords());
    }
}
