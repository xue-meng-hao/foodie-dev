package com.imooc.controller;

import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "首页", tags = {"首页展示的页面"})
@RestController
@RequestMapping("index")
public class IndexController {
    @Autowired
    private CarouselService carouselService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取首页轮播图
     *
     * @return
     */
    @GetMapping("/carousel")
    public IMOOCJSONResult carousel() {
        List<Carousel> carousels = carouselService.queryAll(YesOrNo.YES.type);
        if (carousels == null) {
            IMOOCJSONResult.errorMsg("未找到轮播图！");
        }
        return IMOOCJSONResult.ok(carousels);
    }

    /**
     * 获取商品分类一级分类
     *
     * @return
     */
    @GetMapping("/cats")
    public IMOOCJSONResult cats() {
        List<Category> categories = categoryService.queryAllRootLevelCat();
        if (categories == null) {
            IMOOCJSONResult.errorMsg("未查询到分类！");
        }
        return IMOOCJSONResult.ok(categories);
    }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult subCat(@ApiParam(name = "rootCatId", value = "一级分类id", required = true) @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return IMOOCJSONResult.errorMsg("分类不存在！");
        }
        List<CategoryVO> subCatList = categoryService.getSubCatList(rootCatId);
        return IMOOCJSONResult.ok(subCatList);
    }

    @ApiOperation(value = "查询每个一级分类下最新6条商品数据", notes = "查询每个一级分类下最新6条商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public IMOOCJSONResult sixNewItem(@ApiParam(name = "rootCatId", value = "一级分类id", required = true) @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return IMOOCJSONResult.errorMsg("分类不存在！");
        }
        List<NewItemsVO> newItemsVOList = categoryService.getSixNewItemLazy(rootCatId);
        return IMOOCJSONResult.ok(newItemsVOList);
    }
}
