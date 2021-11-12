package com.jsy.service.impl;

import com.jsy.domain.Feedback;
import com.jsy.mapper.FeedbackMapper;
import com.jsy.service.IFeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tian
 * @since 2021-11-12
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService {

}
