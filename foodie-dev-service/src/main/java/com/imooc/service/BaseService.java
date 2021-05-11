package com.imooc.service;

import com.github.pagehelper.PageInfo;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public class BaseService {
    protected PagedGridResult setterPagedGrid(List<?> list, Integer pageNum) {
        PageInfo<?> pageList = new PageInfo(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(pageNum);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}
