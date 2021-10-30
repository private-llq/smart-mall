package com.jsy.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsy.domain.UserFollow;
import com.jsy.dto.UserFollowDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserFollowMapper extends BaseMapper<UserFollow> {
    void follow(@Param("uuid")String uuid,
                @Param("userUuid")String userUuid,
                @Param("shopUuid")String shopUuid);

    List<UserFollowDTO> followList(@Param ("shopUuids")List<String> shopUuids , @Param("userUuid") String userUuid);



}
