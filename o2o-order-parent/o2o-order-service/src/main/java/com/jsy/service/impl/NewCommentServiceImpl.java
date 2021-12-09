package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.domain.NewComment;
import com.jsy.dto.SelectCommentAndReplyDto;
import com.jsy.dto.SelectShopCommentScoreDto;
import com.jsy.mapper.NewCommentMapper;
import com.jsy.query.CreateCommentParam;
import com.jsy.query.SelectShopCommentPageParam;
import com.jsy.service.INewCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.utils.MyPage;
import com.jsy.vo.SelectCommentAndReplyVo;
import com.zhsj.base.api.constant.RpcConst;
import com.zhsj.base.api.entity.UserDetail;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
@Service
public class NewCommentServiceImpl extends ServiceImpl<NewCommentMapper, NewComment> implements INewCommentService {

    @Resource
    private NewCommentMapper newCommentMapper;

    @DubboReference(version = RpcConst.Rpc.VERSION, group=RpcConst.Rpc.Group.GROUP_BASE_USER,  check = false)
    private IBaseUserInfoRpcService iBaseUserInfoRpcService;

     //新增一条评论
    @Override
    public Boolean createComment(CreateCommentParam param) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        System.out.println(loginUser);
        NewComment entity = new NewComment();
        BeanUtils.copyProperties(param,entity);
        entity.setName(loginUser.getNickName());
        entity.setUserId(loginUser.getId());
        newCommentMapper.insert(entity);
        return true;
    }
    //查询店铺的评分
    @Override
    public SelectShopCommentScoreDto selectShopCommentScore(Long shopId) {
        SelectShopCommentScoreDto scoreDto=new SelectShopCommentScoreDto();
        double value=5.0;
        QueryWrapper<NewComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id",shopId);
        List<NewComment> newComments = newCommentMapper.selectList(queryWrapper);
        int size = newComments.size();
        if (size==0) {
            scoreDto.setScore(5.0);
            scoreDto.setSize(0);
        }else {
            value=0.0;
            double sum = newComments.stream().mapToDouble(NewComment::getEvaluateLevel).sum();
            NumberFormat forma = NumberFormat.getIntegerInstance();
            forma.setMaximumFractionDigits(1);
            String format = forma.format(sum/size);
            Double aDouble = Double.valueOf(format);
            scoreDto.setScore(aDouble);
            scoreDto.setSize(size);
        }

        return scoreDto;
    }
    //分页查询店铺的评论
    @Override
    public Page<NewComment> selectShopCommentPage(SelectShopCommentPageParam param) {
        QueryWrapper<NewComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id",param.getShopId());
        queryWrapper.orderByDesc("create_time");
        Page<NewComment> page=new Page<>(param.getCurrent(),param.getAmount());
        Page<NewComment> page1 = newCommentMapper.selectPage(page, queryWrapper);
        return page1;
    }
   //查询评论和商家回复
    @Override
    public MyPage selectCommentAndReply(SelectShopCommentPageParam param) {
        MyPage<SelectCommentAndReplyDto> myPage=new MyPage();
        Integer current = param.getCurrent();//页码
        Integer amount = param.getAmount();//数量
        Integer    index=(current-1)*(amount);//起始位置
        Integer   end=amount;//每页数量
        List<SelectCommentAndReplyVo> selectCommentAndReplyVos =null;
        Integer   total=0;
        if (param.getIsPicture()==1) {
            selectCommentAndReplyVos= newCommentMapper.selectCommentAndReply(index, end, param.getShopId(),1);//查询有图片的
            total = newCommentMapper.selectCommentAndReplyTotal(param.getShopId(), 1);//总数
        }else {
            selectCommentAndReplyVos= newCommentMapper.selectCommentAndReply(index, end, param.getShopId(),0);//查询所有
            total = newCommentMapper.selectCommentAndReplyTotal(param.getShopId(), 0);//总数
        }

        List<SelectCommentAndReplyDto> list=new ArrayList<>();
        selectCommentAndReplyVos.forEach(a->{
            SelectCommentAndReplyDto selectCommentAndReplyDto=new SelectCommentAndReplyDto();
            BeanUtils.copyProperties(a,selectCommentAndReplyDto);

            UserDetail userDetail = iBaseUserInfoRpcService.getUserDetail(a.getUserId());//获取用户的头像
            String avatarThumbnail = userDetail.getAvatarThumbnail();
            System.out.println("用户头像"+avatarThumbnail);
            selectCommentAndReplyDto.setHeadpPhoto(avatarThumbnail);
            selectCommentAndReplyDto.setName(userDetail.getNickName());
            list.add(selectCommentAndReplyDto);
        });
        myPage.setRecords(list);//数据
        myPage.setTotal(total);//总数
        myPage.setSize(amount);//每页数量
        myPage.setCurrent(current);//前天页
        return myPage;
    }


}
