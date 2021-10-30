package com.jsy.controller;
import com.jsy.basic.util.constant.Global;
import com.jsy.basic.util.constant.PassToken;
import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.GouldUtil;
import com.jsy.basic.util.vo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * 错误码code
 * @author yu
 * @since 2020-11-27 16:03
 */
@RestController
@Api(value = "/pub/api/point",tags ="app-地图")
@RequestMapping("/pub/api/point")
public class PointController {
	@Autowired
	private GouldUtil gouldUtil;

	public static final Logger log = LoggerFactory.getLogger(PointController.class);
	/**
	 * 测试通过
	 * @param longitude
	 * @param lat
	 * @return
	 */
	@ApiOperation(value = "根据经纬度获取地址")
	@GetMapping("/getAddress/{longitude}/{lat}")
	@PassToken
	public CommonResult getAddressTo(@PathVariable("longitude") String longitude, @PathVariable("lat") String lat){
		try {
			String address = gouldUtil.getAMapByLngAndLat(longitude, lat);
			if (Global.Str_1.equals(address)){
				return CommonResult.error(JSYError.NOT_FOUND.getCode(),"经纬度不正确");
			}
			return CommonResult.ok(address);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			throw new JSYException(JSYError.INTERNAL.getCode(),"获取地址失败");
		}
	}

	/**
	 * 测试通过
	 * @param address
	 * @return
	 */
	@ApiOperation(value = "根据地址获取经纬度")
	@GetMapping("/getLonLat/{address}")
	@PassToken
	public CommonResult getLonLat(@PathVariable("address") String address) {
		try {
			String result = gouldUtil.getLonLat(address);
			if (Global.Str_1.equals(result)){
				 return CommonResult.error(JSYError.NOT_FOUND.getCode(),"请输入正确地址");
			}
			return  CommonResult.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			throw new JSYException(JSYError.INTERNAL.getCode(),"获取经纬度失败");
		}
	}

	/**
	 * 根据二点的地址算出距离
	 * @param start
	 * @param end
	 * @return
	 */
	@RequestMapping(value = "/getDistance/{start}/{end}",method = RequestMethod.GET)
	public CommonResult getDistance(@PathVariable("start") String start , @PathVariable("end") String end){
		String lonLat = gouldUtil.getLonLat(start);
		String lonLat1 = gouldUtil.getLonLat(end);
		if (Global.Str_1.equals(lonLat)){
			return CommonResult.error(JSYError.NOT_FOUND.getCode(),"请输入正确地址");
		}
		if (Global.Str_1.equals(lonLat1)){
			return CommonResult.error(JSYError.NOT_FOUND.getCode(),"请输入正确地址");
		}
		double distance = 0;
		try {
			distance = gouldUtil.getDistance(lonLat, lonLat1);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return CommonResult.error(JSYError.INTERNAL.getCode(),"请稍后再试");
		}
		return CommonResult.ok(distance);
	}



}