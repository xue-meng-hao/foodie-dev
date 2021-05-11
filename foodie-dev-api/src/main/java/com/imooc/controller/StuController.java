//package com.imooc.controller;
//
//import com.imooc.service.StuService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class StuController {
//    @Autowired
//    private StuService stuService;
//
//    @RequestMapping("/getStu")
//    public Object getStu(int id) {
//        return stuService.getStuInfo(id);
//    }
//
//    @PostMapping("/saveStu")
//    public Object saveStu() {
//        stuService.saveStu();
//        return "OK";
//    }
//
//    @PostMapping("/updateStu")
//    public Object updateStu(int id) {
//        stuService.updataStu(id);
//        return "OK";
//    }
//
//    @PostMapping("/deleteStu")
//    public Object deleteSut(int id) {
//        stuService.deleteStu(id);
//        return "OK";
//    }
//}
