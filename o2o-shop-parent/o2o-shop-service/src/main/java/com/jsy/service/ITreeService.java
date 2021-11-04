package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.Tree;
import com.jsy.query.MySortQuery;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2021-11-02
 */
public interface ITreeService extends IService<Tree> {



    /**
     * 新增菜单  参数：id ----添加子级需要传入父id
     *               父级id -----用于判断没有父级的为一级菜单
     *                name-------名称
     *                 level------级别（深度）根据B端需要的保留字段
     *                  ranks ----排序序号
     *                  imgPath-----图片路径
     */
    void addTree(Tree tree);

    /**
     * 查询所有一级菜单
     * @return
     */
    List<Tree> getOneTree();

    /**
     * 查询本级下面的子菜单（不包含本级）
     * @return
     */
    List<Tree> getSunTree(Integer id);

    /**
     * 删除一条菜单
     */
    void delTreeOne(Integer id);


    /**
     * 递归删除子节点
     */
    boolean removeAllTree(Integer id);

    /**
     * 修改菜单信息
     */
    void updateTree(Tree tree);


    /**
     * 查询当前级下的所有菜单
     * 传入父级id去查询子级的父id
     * @param id
     * @return
     */
    List<Tree> selectAllTree(Integer id);

    /**
     * 菜单排序
     * @param mySortQuery
     * @return
     */
    void sortMenu(MySortQuery mySortQuery);
}
