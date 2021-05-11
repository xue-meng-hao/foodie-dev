package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.enums.CommentLevel;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.SearchItemsVO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.service.BaseService;
import com.imooc.service.ItemService;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class ItemServiceImpl extends BaseService implements ItemService {
    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        List<ItemsSpec> itemsSpecs = itemsSpecMapper.selectByExample(example);
        return itemsSpecs;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        ItemsParam itemsParam = itemsParamMapper.selectOneByExample(example);
        return itemsParam;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
//        Integer totalCounts=getCommentCounts();
        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer totalCounts = goodCounts + normalCounts + badCounts;
        CommentLevelCountsVO countsVO = new CommentLevelCountsVO();
        countsVO.setTotalCounts(totalCounts);
        countsVO.setGoodCounts(goodCounts);
        countsVO.setNormalCounts(normalCounts);
        countsVO.setBadCounts(badCounts);
        return countsVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer pageNum, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("level", level);
        PageHelper.startPage(pageNum, pageSize);
        List<ItemCommentVO> list = itemsMapperCustom.queryItemComments(map);
        for (ItemCommentVO vo : list) {
            vo.setNickname(DesensitizationUtil.commonDisplay(vo.getNickname()));
        }
        PagedGridResult grid = setterPagedGrid(list, pageNum);
        return grid;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer pageNum, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("keywords", keywords);
        map.put("sort", sort);
        PageHelper.startPage(pageNum, pageSize);
        List<SearchItemsVO> searchItemsVOS = itemsMapperCustom.searchItems(map);
        PagedGridResult gridResult = setterPagedGrid(searchItemsVOS, pageNum);
        return gridResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchByThirdCat(String catId, String sort, Integer pageNum, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("catId", catId);
        map.put("sort", sort);
        PageHelper.startPage(pageNum, pageSize);
        List<SearchItemsVO> searchItemsVOS = itemsMapperCustom.searchByThirdCat(map);
        PagedGridResult gridResult = setterPagedGrid(searchItemsVOS, pageNum);
        return gridResult;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCounts(String itemId, Integer level) {
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);
        if (level != null) {
            condition.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(condition);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) {
        String[] ids = specIds.split(",");
        List<String> specIdsList = new ArrayList<>();
        Collections.addAll(specIdsList, ids);
        List<ShopcartVO> shopcartVOS = itemsMapperCustom.queryItemsBySpecIds(specIdsList);
        return shopcartVOS;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec queryItemSpecById(String specId) {
        ItemsSpec itemsSpec = itemsSpecMapper.selectByPrimaryKey(specId);
        return itemsSpec;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public String queryItemMainImgById(String itemId) {
        ItemsImg singleItemsImg = new ItemsImg();
        singleItemsImg.setItemId(itemId);
        singleItemsImg.setIsMain(YesOrNo.YES.type);
        ItemsImg itemsImg = itemsImgMapper.selectOne(singleItemsImg);
        String url = "";
        if (itemsImg != null) {
            url = itemsImg.getUrl();
        }
        return url;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, Integer buyCounts) {
        //查询库存呢
        //判断库存是否少于购买数量
        int updateNum = itemsMapperCustom.decreaseItemSpecStock(specId, buyCounts);
        if (updateNum != 1) {
            throw new RuntimeException("库存不足");
        }
    }
}
