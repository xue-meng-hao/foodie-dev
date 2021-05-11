package com.imooc.service.impl;

import com.imooc.mapper.StuMapper;
import com.imooc.pojo.Stu;
import com.imooc.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StuServiceImpl implements StuService {
    @Autowired
    private StuMapper stuMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Stu getStuInfo(int id) {
        return stuMapper.selectByPrimaryKey(id);
    }

    @Override
    public void saveStu() {
        Stu stu = new Stu();
        stu.setAge(18);
        stu.setId(1002);
        stu.setName("tom");
        stuMapper.insert(stu);
    }

    @Override
    public void updataStu(int id) {
        Stu stu = new Stu();
        stu.setId(id);
        stu.setName("harry");
        stuMapper.updateByPrimaryKeySelective(stu);
    }

    @Override
    public void deleteStu(int id) {
        Stu stu = new Stu();
        stu.setId(id);
        stuMapper.delete(stu);
    }
}
