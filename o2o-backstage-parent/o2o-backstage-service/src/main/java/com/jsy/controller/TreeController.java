package com.jsy.controller;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.Tree;
import com.jsy.service.ITreeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tree")
@Api("商城首页菜单树")
public class TreeController {
    @Autowired
    public ITreeService treeService;

    /**
     * 查询所有一级菜单
     */
    @ApiOperation("查询所有一级菜单")
    @GetMapping("getOneTree")
    public CommonResult getOneTree() {
        List<Tree> list= treeService.getOneTree();
        return CommonResult.ok(list);
    }
    /**
     * 查询本级上面所有父级菜单（不包含本级）
     */
    @GetMapping("getParentTreeAll")
    public CommonResult<String> getParentTreeAll(@RequestParam ("id") Long id) {
        String str= treeService.getParentTreeAll(id);
        return CommonResult.ok(str);
    }
    /**
     * 查询本级上面的所有菜单id（不包含本级）
     */
    @ApiOperation("查询本级下面一级的子菜单（不包含本级）")
    @GetMapping("getSunTree")
    public CommonResult getSunTree(@RequestParam ("id") Long id) {
        List<Tree> list= treeService.getSunTree(id);
        return CommonResult.ok(list);
    }

    /**
     * 根据id查父级id
     */
    @PostMapping("addTree")
    public CommonResult addTree(@RequestBody Tree tree) {

        treeService.addTree(tree);
        return CommonResult.ok();
    }

    /**
     * 删除一条菜单
     */
    @DeleteMapping("delTreeOne")
    public CommonResult delTreeOne(@RequestParam("id") Long id) {
        treeService.delTreeOne(id);
        return CommonResult.ok();
    }

    /**
     * 删除该级+以及下面的所有子集
     * @param id
     * @return0
     */
    @DeleteMapping("removeAllTree")
    public CommonResult removeAllTree(@RequestParam("id") Long id) {
        treeService.removeAllTree(id);
        return CommonResult.ok();
    }

    /**
     * 修改菜单信息
     */
    @PostMapping("updateTree")
    public CommonResult updateTree(@RequestBody Tree tree) {
        treeService.updateTree(tree);
        return CommonResult.ok();
    }

    /**
     * 菜单排序
     */
    @PostMapping("sortMenu")
    public CommonResult sortMenu(@RequestBody List<Tree> trees) {
        treeService.sortMenu(trees);
        return CommonResult.ok();
    }
    /**
     * 查询当前级下的所有菜单
     * 传入父级id去查询子级的父id
     * @param id
     * @return
     */
    @GetMapping("selectAllTree")
    public CommonResult<List<Tree>> selectAllTree(@RequestParam("id") Long id){
        List<Tree> list = treeService.selectAllTree(id);
        return CommonResult.ok(list);
    }

    /**
     * (用户定制需求档案下拉框)
     */
    @GetMapping("selectAllTree2")
    public CommonResult<List<Tree>> selectAllTree2(@RequestParam("id") Long id){

        List<Tree> list = treeService.selectAllTree2(id);
        return CommonResult.ok(list);
    }

    /**
     * 按级别查询菜单
     */
    @GetMapping("selectLevel")
    public CommonResult<Map<Integer, List<Tree>>> selectLevel(){
        Map<Integer, List<Tree>> list = treeService.selectLevel();
        return CommonResult.ok(list);
    }

    /**
     * id查询菜单
     */
    @RequestMapping(value = "/getTree",method = RequestMethod.GET)
    public CommonResult<Tree> getTree(@RequestParam("id")Long id) {
        Tree tree = treeService.getById(id);
        return CommonResult.ok(tree);
    }
    /**
     * 查询父级
     */
    @RequestMapping(value = "/getParentList/{id}",method = RequestMethod.GET)
    public CommonResult<List<Tree>> getParentList(@PathVariable("id")Long id) {
        List<Tree> tree = treeService.getParentList(id);
        return CommonResult.ok(tree);
    }


}
