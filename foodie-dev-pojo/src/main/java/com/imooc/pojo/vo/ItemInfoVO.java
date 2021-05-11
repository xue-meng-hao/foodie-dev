package com.imooc.pojo.vo;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;

import java.util.List;

/**
 * 商品详情VO
 */
public class ItemInfoVO {
    private Items item;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;
    private List<ItemsImg> itemImgList;

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public List<ItemsSpec> getItemSpecList() {
        return itemSpecList;
    }

    public void setItemSpecList(List<ItemsSpec> itemSpecList) {
        this.itemSpecList = itemSpecList;
    }

    public ItemsParam getItemParams() {
        return itemParams;
    }

    public void setItemParams(ItemsParam itemParams) {
        this.itemParams = itemParams;
    }

    public List<ItemsImg> getItemImgList() {
        return itemImgList;
    }

    public void setItemImgList(List<ItemsImg> itemImgList) {
        this.itemImgList = itemImgList;
    }
}
