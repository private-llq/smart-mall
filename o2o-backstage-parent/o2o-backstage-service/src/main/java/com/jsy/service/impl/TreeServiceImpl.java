package com.jsy.service.impl;

import cn.hutool.core.util.ObjectUtil;
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

import java.util.*;
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
    public List<Tree> getSunTree(Long id) {
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
    public void delTreeOne(Long id) {
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
    public List<Tree> selectAllTree(Long id){

        List<Tree> list = treeMapper.selectList(new QueryWrapper<Tree>().eq("deleted",0));
        List<Tree> collect = list.stream().filter(x -> x.getParentId() == id)//过滤出子级父id等于传入的父级id
                .map(x -> {
                    x.setChildrens(getChildrens(x, list));//从所有数据中找出N条 符合 子级父id等于传入的父级id 的数据 ，然后再把这N条数据当做参数，找到他们的子级
                    return x;
                }).sorted(Comparator.comparing(Tree::getRanks)).collect(Collectors.toList());
        List<Tree> trees = new ArrayList<>();
        collect.forEach(x->{//数据合并
            List<Tree> childrens = x.getChildrens();
            trees.addAll(childrens);

        });
        List<Tree> treeList = trees.stream().sorted(Comparator.comparing(Tree::getRanks)).collect(Collectors.toList());
        return treeList;
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
     * 查询本级上面所有父级菜单（不包含本级）
     */
    //todo
    @Override
    public String getParentTreeAll(Long id) {
        List<Tree> treeList = treeMapper.selectList(null);
        List<Long> list = new ArrayList<>();
        Long temp = id;
        for (Tree tree : treeList) {
            for (Tree tree1 : treeList) {
                if (tree1.getId()==temp&&tree1.getParentId()!=0){
                    list.add(tree1.getParentId());
                    temp = tree1.getParentId();
                }
            }
        }
        Tree tree = treeMapper.selectById(temp);
        if (ObjectUtil.isNotNull(tree)){

            list.add(tree.getParentId());
        }
        Collections.reverse(list);
        return list.toString();
    }


    /**
     * 递归删除子节点
     */
    public boolean removeAllTree(Long id){
        ArrayList<Long> idList = new ArrayList<>();

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
    private void getIds(ArrayList<Long> idList, Long oneId) {
        //查询二级分类的对象
        List<Tree> treeList = baseMapper.selectList(new QueryWrapper<Tree>().eq("parent_id",oneId).eq("deleted",0));

        //遍历二级分类的对象，把二级分类的id加入到要删除的集合中
        for (Tree tree : treeList) {
            Long id = tree.getId();
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

    @Override
    public List<Tree> getParentList(Long id) {
        List<Tree> trees = treeMapper.selectList(null);
        List<Tree> treeList = new ArrayList<>();
        Tree tree1 = treeMapper.selectById(id);
        treeList.add(tree1);
        return getParentId(treeList,tree1.getParentId(),trees);
    }
    public List<Tree> getParentId(List<Tree> treeList,Long pid,List<Tree> trees) {
        Tree tree = trees.stream().filter(s -> s.getId() == pid).findFirst().get();
        treeList.add(tree);
        if (tree.getLevel() != 2) {
            getParentId(treeList, tree.getParentId(), trees);
        }
        return treeList;
    }




}
