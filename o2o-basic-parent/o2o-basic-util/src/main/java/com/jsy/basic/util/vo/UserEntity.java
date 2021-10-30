package com.jsy.basic.util.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ling
 * @since 2020-11-11 17:42
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class UserEntity implements Serializable {
	private String id;

	@ApiModelProperty("uid")
	private String uid;

	private Integer deleted;

	@ApiModelProperty("业主ID")
	private Long householderId;
	
	@ApiModelProperty("昵称")
	private String nickname;
	
	@ApiModelProperty("头像地址")
	private String avatarUrl;

	@ApiModelProperty("人脸地址")
	private String faceUrl;

	@ApiModelProperty("电话号码")
	private String mobile;
	
	@ApiModelProperty("性别，0未知，1男，2女")
	private Integer sex;
	
	@ApiModelProperty("真实姓名")
	//@NotBlank(groups = {ProprietorRegister.class}, message = "姓名未填写!")
	//@Pattern(groups = {ProprietorRegister.class}, regexp = RegexUtils.REGEX_REAL_NAME, message = "请输入一个正确的姓名")
	private String realName;
	
	@ApiModelProperty("身份证")
	//@NotBlank(groups = {ProprietorRegister.class}, message = "身份证号码未输入!")
	//@Pattern(groups = {ProprietorRegister.class}, regexp = RegexUtils.REGEX_ID_CARD, message = "请输入一个正确的身份证号码!")
	private String idCard;
	
	@ApiModelProperty("是否实名认证")
	private Integer isRealAuth;
	
	@ApiModelProperty("省ID")
	private Integer provinceId;
	
	@ApiModelProperty("市ID")
	private Integer cityId;
	
	@ApiModelProperty("区ID")
	private Integer areaId;
	
	@ApiModelProperty("详细地址")
	private String detailAddress;
	
	@ApiModelProperty("离线推送id")
	private String regId;

	@ApiModelProperty("家属关系code")
	private Integer relationCode;


	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss",
			timezone = "GMT+8")
	private LocalDateTime createTime;


//	@ApiModelProperty("车辆信息")
//	private CarEntity carEntity;
//
//	@ApiModelProperty("导入excel记录用户的房屋信息")
//	private HouseEntity houseEntity;

	/**
	 * 业主登记验证接口
	 */
	public interface ProprietorRegister{}

}