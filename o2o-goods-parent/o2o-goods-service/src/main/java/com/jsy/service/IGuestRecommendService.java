package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.basic.util.PageInfo;
import com.jsy.domain.GuestRecommend;
import com.jsy.dto.GuestRecommendDto;
import com.jsy.dto.MatchTheUserDto;
import com.jsy.query.GuestRecommendQuery;

public interface IGuestRecommendService  extends IService<GuestRecommend> {

    void saveGuestRecommend(GuestRecommend guestRecommend);

    PageInfo<GuestRecommendDto> pageGuestRecommend(GuestRecommendQuery query);

    PageInfo<MatchTheUserDto> matchTheUser(GuestRecommendQuery query);
}
