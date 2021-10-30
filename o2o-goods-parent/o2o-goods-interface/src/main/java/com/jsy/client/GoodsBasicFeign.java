package com.jsy.client;
import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.GoodsBasic;
import com.jsy.domain.GoodsCommendDTO;
import com.jsy.dto.SelectGoodsDiscountStatuNowDto;
import com.jsy.vo.RecordVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "SERVICE-GOODS",configuration = FeignConfiguration.class)
public interface GoodsBasicFeign {
    /**
     * 店铺中根据条件查询商品
     * @param uuid
     * @return
     */
    @DeleteMapping("/goodsBasic/shop/{uuid}")
    CommonResult<Boolean> deleteBySid(@PathVariable("uuid") String uuid);

    /**
     * 根据id查询商品信息
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/goodsBasic/goods/{uuid}",method = RequestMethod.GET)
    GoodsBasic getGoods(@PathVariable("uuid")String uuid);

    /**
     * 修改库存，每次减一
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/goodsBasic/updateGoods/{id}",method = RequestMethod.GET)
    int updateGoods(@PathVariable("id") @RequestParam("id") Long goodsId);

    /**
     *
     * @param RecordVo
     */
    @RequestMapping(value = "/goodsBasic/insertProductRecord",method = RequestMethod.POST)
    void insertProductRecord(@RequestBody RecordVo RecordVo);

    /**
     * 根据店铺ids查出店铺销量高的商品
     * @param map
     * @return
     */
    @PostMapping("/goodsBasic/pub/commend")
    CommonResult<List<GoodsCommendDTO>> commendGoods(@RequestBody Map<String,Object> map);


    @ApiOperation("查询当前商家 当前海报所关联 的商品 并排序 传入posterUuid")
    @GetMapping("/goodsBasic/pub/listGoods")
    CommonResult<List<GoodsBasic>> goodsList(@RequestParam("posterUuid") String posterUuid) ;

    @ApiOperation("根据商品的uuid查询商品当前是否有折扣以及商品的折扣后的价格")
    @RequestMapping(value = "/selectGoodsDiscountStatuNow", method = RequestMethod.POST)
    public CommonResult<SelectGoodsDiscountStatuNowDto> selectGoodsDiscountStatuNow(String goodUuid);
}
