package com.jsy.controller;

import com.jsy.dto.SelectShopCommentPageDto;
import com.jsy.dto.SelectShopCommentScoreDto;
import com.jsy.query.CreateCommentParam;
import com.jsy.query.SelectShopCommentPageParam;
import com.jsy.service.INewCommentService;
import com.jsy.domain.NewComment;
import com.jsy.query.NewCommentQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.utils.MyPage;
import com.jsy.vo.SelectCommentAndReplyVo;
import com.zhsj.baseweb.annotation.LoginIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

import java.util.List;

@RestController
@RequestMapping("/newComment")
public class NewCommentController {
    @Autowired
    public INewCommentService newCommentService;

    /**
     * 保存和修改公用的
     *
     * @param newComment 传递的实体
     * @return Ajaxresult转换结果
     */
    @PostMapping(value = "/save")
    public CommonResult save(@RequestBody NewComment newComment) {
        try {
            if (newComment.getId() != null) {
                newCommentService.updateById(newComment);
            } else {
                newCommentService.save(newComment);
            }
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1, "操作失败！");
        }
    }

    /**
     * 删除对象信息
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public CommonResult delete(@PathVariable("id") Long id) {
        try {
            newCommentService.removeById(id);
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1, "删除失败！");
        }
    }

    /**
     * 根据id查询一条
     *
     * @param id
     */
    @GetMapping(value = "/{id}")
    public NewComment get(@PathVariable("id") Long id) {
        return newCommentService.getById(id);
    }


    /**
     * 返回list列表
     *
     * @return
     */
    @GetMapping(value = "/list")
    public List<NewComment> list() {

        return newCommentService.list(null);
    }


    /**
     * 分页查询数据
     *
     * @param query 查询对象
     * @return PageList 分页对象
     */
    @PostMapping(value = "/pagelist")
    public PageList<NewComment> json(@RequestBody NewCommentQuery query) {
        Page<NewComment> page = new Page<NewComment>(query.getPage(), query.getRows());
        page = newCommentService.page(page);
        return new PageList<NewComment>(page.getTotal(), page.getRecords());
    }
/**************************************arli**********************************/
    //新增一条评论
    @RequestMapping(value = "/createComment",method =RequestMethod.POST)
    public CommonResult<Boolean> createComment(@RequestBody CreateCommentParam param) {
        Boolean b = newCommentService.createComment(param);
        return CommonResult.ok(b);
    }

    //查询店铺的评分
    @RequestMapping(value = "/selectShopCommentScore",method =RequestMethod.GET)
    public CommonResult<SelectShopCommentScoreDto> selectShopCommentScore(@RequestParam("shopId") Long shopId) {
        SelectShopCommentScoreDto value= newCommentService.selectShopCommentScore(shopId);
        return CommonResult.ok(value);
    }


   /* //分页查询店铺的评论
    @LoginIgnore
    @RequestMapping(value = "/selectShopCommentPage",method =RequestMethod.POST)
    public CommonResult<Page<NewComment>> selectShopCommentPage(@RequestBody SelectShopCommentPageParam param) {
       Page<NewComment>   values=newCommentService.selectShopCommentPage(param);
        return CommonResult.ok(values);
    }*/

    //分页查询店铺的评论and回复
   /* @LoginIgnore*/
    @RequestMapping(value = "/selectCommentAndReply",method =RequestMethod.POST)
    public CommonResult<MyPage> selectCommentAndReply(@RequestBody SelectShopCommentPageParam param) {
        MyPage values=newCommentService.selectCommentAndReply(param);
        return CommonResult.ok(values);
    }






}
