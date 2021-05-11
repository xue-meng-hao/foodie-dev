package com.imooc.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;

import java.util.List;

public interface CategoryService {
    /**
     * 查询所有一级分类
     *
     * @return
     */
    public List<Category> queryAllRootLevelCat();

    /**
     * 根据一级分类id查询子分类
     *
     * @param rootCatId 一级分类Id
     * @return
     */
    public List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 获取该品类中最新的六条商品信息
     */
    public List<NewItemsVO> getSixNewItemLazy(Integer rootCatId);
}
