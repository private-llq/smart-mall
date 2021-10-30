package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.GoodsBasic;
import com.jsy.domain.GoodsCommendDTO;
import com.jsy.dto.*;
import com.jsy.parameter.*;
import com.jsy.query.GoodsBasicQuery;
import com.jsy.service.IGoodsBasicService;
import com.jsy.service.impl.GoodsBasicServiceImpl;
import com.jsy.vo.AddGoodsVo;
import com.jsy.vo.GoodsVo;
import com.jsy.vo.ShelfTypeVo;
import com.jsy.vo.UpdateGoodtTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/goodsBasic")
@Api(tags = "商品模块")
@Slf4j
public class GoodsBasicController {
    @Resource
    public IGoodsBasicService goodsBasicService;
    @Resource
    public GoodsBasicServiceImpl goodsBasicServiceImpl;

    @ApiOperation("新增商品")
    @RequestMapping(value = "/addGoods", method = RequestMethod.POST)
    public CommonResult<Boolean> save(@RequestBody @ApiParam(name = "添加商品vo") GoodsBasicParam goodsBasicParam) {
        log.info("入参：", goodsBasicParam);
        Boolean aBoolean = goodsBasicService.addGoods(goodsBasicParam);
        return new CommonResult(200, "新增成功", aBoolean);
    }

    @ApiOperation("根据商品分类uuid类型查询商品集合")
    @RequestMapping(value = "/selectGoodSByType", method = RequestMethod.POST)
    public CommonResult<List<GoodsBasicDto>> save(@RequestBody GoodsBasicByTypeAndMarketable gbtm) {
        log.info("入参:", gbtm);
        List<GoodsBasicDto> goodsBasicDtos = goodsBasicService.selectGoodSByType(gbtm);
        return new CommonResult(200, "查询成功", goodsBasicDtos);

    }

    @ApiOperation("批量上/下架")
    @RequestMapping(value = "/putAngDownGoods", method = RequestMethod.POST)
    public CommonResult<Boolean> putAngDownGoods(@RequestBody PutAndDownGoodsParam param) {
        Boolean aBoolean = goodsBasicService.putAngDownGoods(param);
        if (param.getIsMarketable().equals("1")) {
            return new CommonResult(200, "上架成功", aBoolean);
        }
        return new CommonResult(200, "下架成功", aBoolean);
    }

    public static void main(String[] args) {
        Stream.generate(Math::random).limit(5).forEach(System.out::print);
        List<Integer> collect = Stream.iterate(0,i -> i + 1).limit(5).collect(Collectors.toList());
        System.out.println(collect);

    }

    @ApiOperation("批量调整分类")
    @RequestMapping(value = "/adjustClassify", method = RequestMethod.POST)
    public CommonResult<Boolean> delete(@RequestBody GoodsAdjustClassifyParam goodsAdjustClassifyParam) {
        Boolean aBoolean = goodsBasicService.adjustClassify(goodsAdjustClassifyParam);
        if (aBoolean) {
            return new CommonResult(200, "修改分类成功", aBoolean);
        }
        return new CommonResult(200, "修改分类失败", aBoolean);
    }

    @ApiOperation("排序商品")
    @RequestMapping(value = "/rankGoods", method = RequestMethod.POST)
    public CommonResult<Boolean> rankGoods(@RequestBody List<GoodRankParam> goodRankParams) {
        Boolean aBoolean = goodsBasicService.rankGoods(goodRankParams);
        if (aBoolean) {
            return new CommonResult(200, "排序成功", aBoolean);
        }
        return new CommonResult(200, "排序失败", aBoolean);

    }

    @ApiOperation("根据商品uuid查询商品的详情")
    @RequestMapping(value = "/selectDetails", method = RequestMethod.GET)
    public CommonResult<GoodsSelectDetailsDTO> selectDetails(@RequestParam String goodsUUid) {
        GoodsSelectDetailsDTO goodsSelectDetailsDTO = goodsBasicService.selectDetails(goodsUUid);
        return new CommonResult(200, "查询成功", goodsSelectDetailsDTO);

    }

    @ApiOperation("对下架商品重新编辑修改")
    @RequestMapping(value = "/updateGoods", method = RequestMethod.POST)
    public CommonResult<Boolean> updateGoods(@RequestBody GoodsBasicUpdateParam goodsBasicUpdateParam) {
        Boolean aBoolean = goodsBasicService.updateGoods(goodsBasicUpdateParam);
        return new CommonResult(200, "修改成功", aBoolean);
    }

    @ApiOperation("查询参加活动的商品集合1(进行中)0(已结束)")
    @RequestMapping(value = "/SelectJoinActivityGoods", method = RequestMethod.POST)
    public CommonResult<ArrayList<SelectJoinActivityGoodsDto>> SelectJoinActivityGoods(@RequestBody SelectJoinActivityGoodsParam param) {
        ArrayList<SelectJoinActivityGoodsDto> selectJoinActivityGoodsDtos = goodsBasicService.SelectJoinActivityGoods(param);
        return new CommonResult(200, "查询成功", selectJoinActivityGoodsDtos);
    }
    
    @ApiOperation("批量修改商品的折扣")
    @RequestMapping(value = "/batchUpdateGoodsDiscount", method = RequestMethod.POST)
    public CommonResult<Integer> batchUpdateGoodsDiscount(@RequestBody ArrayList<BatchUpdateGoodsDiscountParam> BatchUpdateGoodsDiscountParam) {
        Integer integer = goodsBasicService.batchUpdateGoodsDiscount(BatchUpdateGoodsDiscountParam);
        return new CommonResult(200, "修改成功", integer);
    }

    @ApiOperation("根据商品类型和折扣状态查询商品信息")
    @RequestMapping(value = "/selectGoodsByDiscountStatusAndType", method = RequestMethod.POST)
    public CommonResult<List<GoodsBasicDiscountDto>> selectGoodsByDiscountStatusAndType(@RequestBody SelectGoodsByDiscountStatusAndTypeParam param) {
        List<GoodsBasicDiscountDto> goodsBasicDtos = goodsBasicService.selectGoodsByDiscountStatusAndType(param);
        return new CommonResult(200, "查询成功", goodsBasicDtos);
    }
    @ApiOperation("批量修改商品的折扣")
    @RequestMapping(value = "/batchUpdateGoodsDiscountTwo", method = RequestMethod.POST)
    public CommonResult<Boolean> batchUpdateGoodsDiscountTwo(@RequestBody BatchUpdateGoodsDiscountParamTwo paramTwo) {
        Boolean aBoolean = goodsBasicService.batchUpdateGoodsDiscountTwo(paramTwo);
        if(aBoolean){
            return new CommonResult(200, "修改成功", aBoolean);
        }
        return new CommonResult(-1, "修改失败", aBoolean);

    }


    @ApiOperation("根据商品的uuid查询商品当前是否有折扣以及商品的折扣后的价格")
    @RequestMapping(value = "/selectGoodsDiscouStatuNownt", method = RequestMethod.POST)
    public CommonResult<SelectGoodsDiscountStatuNowDto> selectGoodsDiscountStatuNow(String goodUuid) {
        SelectGoodsDiscountStatuNowDto goodsDiscountStatuNowDto = goodsBasicService.selectGoodsDiscountStatuNow(goodUuid);
        return CommonResult.ok(goodsDiscountStatuNowDto);
    }

    /*********************************************************************************************************/
    /**
    * 新增商品
    * @param  addGoodsVo 新增传递的实体
    * @return CommonResult 转换结果
    */

    @ApiOperation("新增商品")
    @RequestMapping(value="/add",method= RequestMethod.POST)
    public CommonResult<Boolean> save(@RequestBody @ApiParam(name = "添加商品vo") AddGoodsVo addGoodsVo){
        log.info("入参：{}",addGoodsVo);
        goodsBasicService.add(addGoodsVo);
        return new CommonResult(200,"新增成功",true);
    }
    /**
     * 修改商品
     * @param  goodsVo 修改传递的实体
     * @return CommonResult 转换结果
     */
    @ApiOperation("修改商品")
    @RequestMapping(value="/update",method= RequestMethod.PUT)
    public CommonResult<Boolean> update(@RequestBody @ApiParam(name = "修改商品vo") GoodsVo goodsVo){
        log.info("入参：{}",goodsVo);
        Integer update = goodsBasicServiceImpl.update(goodsVo);
        return  new CommonResult(200,"新增成功",true);
    }

    /**
     * 单个放入回收站
     * @param uuid
     * @return
     */
    @ApiOperation("[商家]根据uuid单个放入回收站")
    @DeleteMapping("/deleteOne/{uuid}")
    public CommonResult deleteOne(@PathVariable("uuid") String uuid) {
        goodsBasicService.deleteOne(uuid);
        return  new CommonResult(200,"操作成功",true);
    }

    /**
     * 批量放入回收站
     * @param ids
     * @return
     */
    @ApiOperation("[商家]根据uuid批量放入回收站")
    @RequestMapping(value = "/deleteUuids",method = RequestMethod.POST)
    public CommonResult batchDelete(@ApiParam("需要删除商品的id数组") @RequestBody ArrayList<String> ids) {
        System.out.println(ids.get(0));
        goodsBasicService.batchDelete(ids);
        return  new CommonResult(200,"操作成功",true);
    }


    /**
     * 单个商品彻底删除
     * @param uuid
     * @return
     */
    @ApiOperation("[商家]根据uuid彻底删除单个")
    @DeleteMapping("/actualdel/{uuid}")
    public CommonResult actualdel(@PathVariable("uuid") String uuid) {
        goodsBasicService.actualDelOne(uuid);
        return  new CommonResult(200,"操作成功",true);
    }

    /**
     * 批量彻底删除
     * @param uuids
     * @return
     */
    @ApiOperation("[商家]根据uuid彻底删除多个商品")
    @DeleteMapping("/actualdels/{uuids}")
    public CommonResult actualDel(@ApiParam("实际删除商品的uuid") @PathVariable("uuids") String uuids) {
        goodsBasicService.batchActualDel(uuids);
        return  new CommonResult(200,"操作成功",true);
    }

    @ApiOperation("[商家]回收站恢复商品")
    @PutMapping("/recover")
    public CommonResult recover(@RequestBody List<String> uuids) {
        goodsBasicService.recover(uuids);
        return  new CommonResult(200,"操作成功",true);
    }

    @ApiOperation("[商家]商品批量上下架")
    @PutMapping("/goodsshelf")
    public CommonResult onShelf(@RequestBody ShelfTypeVo shelfType) {
        goodsBasicService.onshelf(shelfType.getUuids(),shelfType.getShelfType());
        return  new CommonResult(200,"操作成功",true);
    }
    @ApiOperation("根据uuid查询商品信息")
    @RequestMapping(value = "/{uuid}",method = RequestMethod.GET)
    public CommonResult<GoodsVo> get(@ApiParam("商品id")@PathVariable("uuid")String uuid) {
        GoodsVo goodsVo = goodsBasicService.findOne(uuid);
        return CommonResult.ok(goodsVo);

    }
    @ApiOperation("[商家]修改商品分类")
    @PutMapping("/toOtherType")
    public CommonResult toOtherType(@RequestBody UpdateGoodtTypeVo ugtv) {
        goodsBasicService.toOtherType(ugtv.getGUuid(),ugtv.getNewTypeUuid());
        return CommonResult.ok();
    }
    @ApiOperation("根据店铺uuid删除商品")
    @DeleteMapping("/shop/{uuid}")
    public CommonResult<Boolean> deleteBySid(@ApiParam("店铺uuid") @PathVariable("uuid") String uuid) {
        return goodsBasicService.deleteBySid(uuid) == true ? CommonResult.ok() : CommonResult.error(-1,"操作失败");
    }
    @ApiOperation("店铺中根据条件查询商品")
    @PostMapping("shop/{uuid}")
    public CommonResult<PageList<GoodsBasic>> findGoodsInShop(@ApiParam("店铺uuid") @PathVariable("uuid") String uuid,
                                                              @ApiParam("分页查询条件")@RequestBody GoodsBasicQuery query){
        return CommonResult.ok(goodsBasicService.findGoodsInShop(uuid,query));
    }

    //以下为内部调用-----------------------------------------------------------------

    /**
     * 内部调用
     * 根据id查询商品信息
     * @param uuid
     * @return
     */
    @ApiOperation("根据uuid查询上架商品信息")
    @RequestMapping(value = "/goods/{uuid}",method = RequestMethod.GET)
    public GoodsBasic getGoods(@PathVariable("uuid")String uuid) {
        return goodsBasicService.getOne(new QueryWrapper<GoodsBasic>().eq("uuid",uuid).eq("is_marketable","1").eq("status",1));
    }

    /**
     * 内部调用
     * @param uuid
     * @return
     */
    @ApiOperation("修改库存，每次减一")
    @RequestMapping(value = "/updateGoods/{uuid}",method = RequestMethod.GET)
    public int updateGoods(@PathVariable("uuid") String uuid){
       return goodsBasicService.updateGoods(uuid);
    }


    @ApiOperation("根据店铺ids查出店铺销量高的商品")
    @PostMapping("/pub/commend")
    public CommonResult<List<GoodsCommendDTO>> commendGoods(@RequestBody Map<String,Object> map) {
        return CommonResult.ok(goodsBasicService.commendGoods(map));
    }


}
