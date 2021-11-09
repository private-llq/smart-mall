package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.domain.Tree;
import com.jsy.mapper.TreeMapper;
import com.jsy.query.MySortQuery;
import com.jsy.service.ITreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2021-11-02
 */
@Service
public class TreeServiceImpl extends ServiceImpl<TreeMapper, Tree> implements ITreeService {

    @Autowired
    private TreeMapper treeMapper;


    /**
     * 查询所有一级菜单
     * @return
     */
    @Override
    public List<Tree> getOneTree() {

        List<Tree> list = treeMapper.selectList(new QueryWrapper<Tree>().eq("parent_id", 0).eq("deleted",0));
        return list;
    }

    /**
     * 查询本级下面的子菜单（不包含本级）
     * @return
     */
    @Override
    public List<Tree> getSunTree(Integer id) {
        List<Tree> list = treeMapper.selectList(new QueryWrapper<Tree>().eq("parent_id", id).eq("deleted",0));
        return list;
    }



    /**
     * 新增菜单  参数：id ----添加子级需要传入父id
     *               父级id -----用于判断没有父级的为一级菜单
     *                name-------名称
     *                 level------级别（深度）根据B端需要的保留字段
     *                  imgPath-----图片路径
     */
    @Override
    @Transactional
    public void addTree(Tree tree) {
       /* if (tree.getParentId()==null){
            tree.setParentId(0);
        }*/
        List<Tree> list = treeMapper.selectList(new QueryWrapper<Tree>().eq("parent_id", tree.getParentId()));
        if (list.size()!=0){
            int i = list.stream().max(Comparator.comparing(Tree::getRanks)).get().getRanks() + 1;
            tree.setRanks(i);
        }else {
            tree.setRanks(0);
        }

        treeMapper.insert(tree);


    }


    /**
     * 删除一条菜单记录
     * 判断是否有子级如果有则不能删除
     * @param id
     */
    @Override
    public void delTreeOne(Integer id) {
        List<Tree> list = treeMapper.selectList(new QueryWrapper<Tree>().eq("parent_id", id).eq("deleted",0));
        if (list.size()==0){
            treeMapper.deleteById(id);
        }else {
            throw new JSYException(-1,"该级下面还有子菜单不能删除！");
        }


    }

    /**
     * 修改菜单信息
     *
     * 根据id修改
     *
     * 可修改内容：name 、 img_path
     */
    @Override
    public void updateTree(Tree tree) {
        if (tree.getId()==null){
            throw new JSYException(-1,"id不能为空！");
        }

        treeMapper.update(tree,new QueryWrapper<Tree>().eq("id",tree.getId()));
    }


    /**
     * 查询当前级下的所有菜单
     * 传入父级id去查询子级的父id
     * @param id
     * @return
     */
    @Override
    public List<Tree> selectAllTree(Integer id){
        List<Tree> list = treeMapper.selectList(new QueryWrapper<Tree>().eq("deleted",0));
        List<Tree> collect = list.stream().filter(x -> x.getParentId() == id)//过滤出子级父id等于传入的父级id
                .map(x -> {
                    x.setChildrens(getChildrens(x, list));//从所有数据中找出N条 符合 子级父id等于传入的父级id 的数据 ，然后再把这N条数据当做参数，找到他们的子级
                    return x;
                }).sorted(Comparator.comparing(Tree::getRanks)).collect(Collectors.toList());

        return collect;
    }



                                       //N条数据          //所有数据
    private List<Tree> getChildrens(Tree root,List<Tree> childrens ){
        List<Tree> collect = childrens.stream().filter(x ->
             x.getParentId() == root.getId()//过滤出父级id和子级的父id相等的数据
        ).map(x->{
            x.setChildrens(getChildrens(x,childrens));//循环比较
            return x;
        }).sorted(Comparator.comparing(Tree::getRanks)).collect(Collectors.toList());

        return collect;
    }

    /**
     * 递归删除子节点
     */
    public boolean removeAllTree(Integer id){
        ArrayList<Integer> idList = new ArrayList<>();

        //先把父级id放进去
        idList.add(id);

        //再找出所有子级id放进去
        List<Tree> list = treeMapper.selectList(new QueryWrapper<Tree>().eq("parent_id", id).eq("deleted",0));
        list.forEach(x->{
            idList.add(x.getId());
        });

        //递归的将一级分类下的id也加入到集合中
        this.getIds(idList,id);


        //批量删除集合中的id
        int i = baseMapper.deleteBatchIds(idList);
        return i>0;

    }


    //递归方法
    private void getIds(ArrayList<Integer> idList, Integer oneId) {
        //查询二级分类的对象
        List<Tree> treeList = baseMapper.selectList(new QueryWrapper<Tree>().eq("parent_id",oneId).eq("deleted",0));

        //遍历二级分类的对象，把二级分类的id加入到要删除的集合中
        for (Tree tree : treeList) {
            Integer id = tree.getId();
            idList.add(id);
            //把二级分类的每一个ID，查询它下面的子节点
            this.getIds(idList,id);
        }
    }

    /**
     * 菜单排序
     * @param mySortQuery
     * @return
     */
    @Override
    public void sortMenu(MySortQuery mySortQuery) {
        treeMapper.sortMenu(mySortQuery);
    }


    /**
     * 按级别查询菜单
     */
    @Override
    public Map<Integer, List<Tree>> selectLevel() {
        List<Tree> treeList = treeMapper.selectList(new QueryWrapper<Tree>().eq("deleted", 0));
        Map<Integer, List<Tree>> collect = treeList.stream().collect(Collectors.groupingBy(Tree::getLevel));
        return collect;
    }

}