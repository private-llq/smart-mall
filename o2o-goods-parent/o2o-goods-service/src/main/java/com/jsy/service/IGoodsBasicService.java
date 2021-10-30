package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.basic.util.PageList;
import com.jsy.domain.GoodsBasic;
import com.jsy.domain.GoodsCommendDTO;
import com.jsy.dto.*;
import com.jsy.parameter.*;
import com.jsy.query.GoodsBasicQuery;
import com.jsy.vo.AddGoodsVo;
import com.jsy.vo.GoodsVo;
import com.jsy.vo.RecordVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
public interface IGoodsBasicService extends IService<GoodsBasic> {

    //添加商品
    public Boolean addGoods(GoodsBasicParam goodsBasicParam);

    //根据商品分类uuid类型查询商品集合
    public List<GoodsBasicDto> selectGoodSByType(GoodsBasicByTypeAndMarketable gbtm);

    //批量上/下架
    public Boolean putAngDownGoods(PutAndDownGoodsParam P);

    //批量将商品调整分类
    public Boolean adjustClassify(GoodsAdjustClassifyParam goodsAdjustClassifyParam);

    //排序商品
    public Boolean rankGoods(List<GoodRankParam> goodRankParams);

    //根据商品uuid查询商品的详情
    public GoodsSelectDetailsDTO selectDetails(String goodsUUid);

    //对下架商品重新编辑修改
    public Boolean updateGoods(GoodsBasicUpdateParam goodsBasicUpdateParam);

    //查询参加活动的商品集合（1(进行中)0(已结束)）
    public ArrayList<SelectJoinActivityGoodsDto> SelectJoinActivityGoods(SelectJoinActivityGoodsParam param);

    //批量修改商品的折扣
    public Integer batchUpdateGoodsDiscount(ArrayList<BatchUpdateGoodsDiscountParam> BatchUpdateGoodsDiscountParam);

    //根据商品类型和折扣状态查询商品信息
    public List<GoodsBasicDiscountDto> selectGoodsByDiscountStatusAndType(SelectGoodsByDiscountStatusAndTypeParam param);

    //批量修改商品的折扣
    public Boolean batchUpdateGoodsDiscountTwo(BatchUpdateGoodsDiscountParamTwo paramTwo);
    //根据商品的uuid查询商品当前是否有折扣以及商品的折扣后的价格
    public SelectGoodsDiscountStatuNowDto selectGoodsDiscountStatuNow(String goodUuid);







    /*****************************************************************************/
    void add(AddGoodsVo goodsVo);

    void batchDelete(ArrayList<String> ids);

    GoodsVo findOne(String id);

    Boolean deleteBySid(String uuid);

    PageList<GoodsBasic> findGoodsInShop(String uuid, GoodsBasicQuery query);

    int updateGoods(String goodsUuid);

    void insertProductRecord(RecordVo recordVo);

    void updateGoodsReturn(String goodsUuid);

    List<GoodsCommendDTO> commendGoods(Map<String, Object> map);

    void batchActualDel(String uuids);

    void deleteOne(String uuid);

    void actualDelOne(String uuid);

    void recover(List<String> uuids);

    void onshelf(List<String> uuids, String shelfType);

    void toOtherType(String gUuid, String newTypeUuid);

    void saveRelatedGoods(String shopUuid, String goodsUuidIDS,String posterUuid);

    void deleteRelatedGoods(String shopUuid, String goodsUuid);
}
