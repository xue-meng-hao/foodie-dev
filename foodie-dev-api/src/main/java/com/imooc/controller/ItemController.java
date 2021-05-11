package com.imooc.controller;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemInfoVO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.service.ItemService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "商品接口", tags = {"商品详情"})
@RestController
@RequestMapping("items")
public class ItemController extends BaseController {
    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult getItemDetail(@ApiParam(name = "itemId", value = "商品id", required = true) @PathVariable String itemId) {
        if (itemId == null) {
            return IMOOCJSONResult.errorMsg("未查询到商品信息");
        }
        Items item = itemService.queryItemById(itemId);
        List<ItemsSpec> itemSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemParams = itemService.queryItemParam(itemId);
        List<ItemsImg> itemImgList = itemService.queryItemImgList(itemId);
        ItemInfoVO itemInfo = new ItemInfoVO();
        itemInfo.setItem(item);
        itemInfo.setItemSpecList(itemSpecList);
        itemInfo.setItemParams(itemParams);
        itemInfo.setItemImgList(itemImgList);
        return IMOOCJSONResult.ok(itemInfo);
    }

    @ApiOperation(value = "查询商品评价数量", notes = "查询商品评价数量", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public IMOOCJSONResult getCommentLevel(@ApiParam(name = "itemId", value = "商品id", required = true) @RequestParam String itemId) {
        if (itemId == null) {
            return IMOOCJSONResult.errorMsg("未查询到商品信息");
        }
        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);
        return IMOOCJSONResult.ok(countsVO);
    }

    @ApiOperation(value = "查询商品评价", notes = "查询商品评价", httpMethod = "GET")
    @GetMapping("/comments")
    public IMOOCJSONResult getCommentLevel(@ApiParam(name = "itemId", value = "商品id", required = true) @RequestParam String itemId,
                                           @ApiParam(name = "level", value = "评价级别", required = false) @RequestParam Integer level,
                                           @ApiParam(name = "page", value = "查询第几页", required = false) @RequestParam Integer page,
                                           @ApiParam(name = "pageSize", value = "分页的每一页显示多少条", required = false) @RequestParam Integer pageSize) {
        if (itemId == null) {
            return IMOOCJSONResult.errorMsg("未查询到商品信息");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }
        PagedGridResult gridResult = itemService.queryPagedComments(itemId, level, page, pageSize);
        return IMOOCJSONResult.ok(gridResult);
    }

    @ApiOperation(value = "搜索商品结果", notes = "搜索商品结果", httpMethod = "GET")
    @GetMapping("/search")
    public IMOOCJSONResult getSearchItems(@ApiParam(name = "keywords", value = "关键字", required = true) @RequestParam String keywords,
                                          @ApiParam(name = "sort", value = "排序方式", required = false) @RequestParam String sort,
                                          @ApiParam(name = "page", value = "查询第几页", required = false) @RequestParam Integer page,
                                          @ApiParam(name = "pageSize", value = "分页的每一页显示多少条", required = false) @RequestParam Integer pageSize) {
        if (keywords == null) {
            return IMOOCJSONResult.errorMsg("请输入关键词");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }
        PagedGridResult gridResult = itemService.searchItems(keywords, sort, page, pageSize);
        return IMOOCJSONResult.ok(gridResult);
    }

    @ApiOperation(value = "查询分类商品结果", notes = "查询分类商品结果", httpMethod = "GET")
    @GetMapping("/catItems")
    public IMOOCJSONResult getCatItems(@ApiParam(name = "catId", value = "分类Id", required = true) @RequestParam String catId,
                                       @ApiParam(name = "sort", value = "排序方式", required = false) @RequestParam String sort,
                                       @ApiParam(name = "page", value = "查询第几页", required = false) @RequestParam Integer page,
                                       @ApiParam(name = "pageSize", value = "分页的每一页显示多少条", required = false) @RequestParam Integer pageSize) {
        if (catId == null) {
            return IMOOCJSONResult.errorMsg("请输入关键词");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }
        PagedGridResult gridResult = itemService.searchByThirdCat(catId, sort, page, pageSize);
        return IMOOCJSONResult.ok(gridResult);
    }

    //用于用户长时间未登录网址，刷新购物车数据(主要是价格)
    @ApiOperation(value = "根据商品规格ids查找最新的商品数据", notes = "根据商品规格ids查找最新的商品数据", httpMethod = "GET")
    @GetMapping("/refresh")
    public IMOOCJSONResult getCatItems(@ApiParam(name = "itemSpecIds", value = "拼接的规格ids", required = true, example = "1001,1003,1005") @RequestParam String itemSpecIds) {
        if (itemSpecIds == null) {
            return IMOOCJSONResult.ok();
        }
        List<ShopcartVO> shopcartVOS = itemService.queryItemsBySpecIds(itemSpecIds);
        return IMOOCJSONResult.ok(shopcartVOS);
    }
}
