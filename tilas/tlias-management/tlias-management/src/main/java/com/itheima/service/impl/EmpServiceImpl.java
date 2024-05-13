package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.PageBean;
import com.itheima.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    /*@Override
    public PageBean find(String name, Short gender, LocalDate begin, LocalDate end, Integer page, Integer pageSize) {
        //1. 调用EmpMapper 查询当前页数据
        // 计算开始索引
        int start = ( page - 1 ) * pageSize;

        List<Emp> emps = empMapper.find(name, gender, begin, end, start, pageSize);

        //2. 调用EmpMapper 查询总记录数
        Long total = empMapper.getTotal();


        PageBean pageBean = new PageBean();
        pageBean.setRows(emps);
        pageBean.setTotal(total);

        return pageBean;
    }*/


    @Override
    public PageBean find(String name, Short gender, LocalDate begin, LocalDate end, Integer page, Integer pageSize) {

        //1. 开启分页功能
        PageHelper.startPage(page,pageSize);
        //2. 查询
        List<Emp> emps = empMapper.find(name, gender, begin, end);//select * from emp
        Page<Emp> p = (Page<Emp>) emps;

        PageBean pageBean = new PageBean();
        pageBean.setRows(p.getResult());
        pageBean.setTotal(p.getTotal());

        return pageBean;
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        empMapper.deleteBatch(ids);
    }

    @Override
    public void add(Emp emp) {
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());

        empMapper.add(emp);
    }

    @Override
    public Emp findById(Integer id) {
        return empMapper.findById(id);
    }

    @Override
    public void update(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());

        empMapper.update(emp);

    }
}
