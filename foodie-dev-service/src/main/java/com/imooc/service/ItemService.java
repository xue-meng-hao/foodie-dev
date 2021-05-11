package com.imooc.service;

import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface ItemService {
    /**
     * 根据商品id查询详情
     *
     * @param itemId 商品id
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品id查询图片
     *
     * @param itemId 商品id
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id查询商品规格
     *
     * @param itemId 商品id
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id查询商品参数
     *
     * @param itemId 商品参数
     * @return
     */
    public ItemsParam queryItemParam(String itemId);

    /**
     * 根据商品id查询商品评价等级数量
     *
     * @param itemId
     */
    public CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品id查询商品评价（分页）
     *
     * @return
     */
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer pageNum, Integer pageSize);

    /**
     * 根据关键词搜索商品
     *
     * @return
     */
    public PagedGridResult searchItems(String keywords, String sort, Integer pageNum, Integer pageSize);

    /**
     * 根据分类查询商品
     *
     * @param catId    分类id
     * @param sort     排序方式
     * @param pageNum  页码
     * @param pageSize 每页包含条数
     * @return
     */
    public PagedGridResult searchByThirdCat(String catId, String sort, Integer pageNum, Integer pageSize);

    /**
     * 根据规格ids查询最新购物车数据，用于渲染购物车页面
     *
     * @param specIds
     * @return
     */
    public List<ShopcartVO> queryItemsBySpecIds(String specIds);

    /**
     * 根据规格id查询商品规格数据
     *
     * @param specId
     * @return
     */
    public ItemsSpec queryItemSpecById(String specId);

    /**
     * 根据商品id查询商品主图
     *
     * @param itemId
     * @return
     */
    public String queryItemMainImgById(String itemId);

    /**
     * 扣除数据库中库存
     *
     * @param specId
     * @param buyCounts
     */
    public void decreaseItemSpecStock(String specId, Integer buyCounts);
}
