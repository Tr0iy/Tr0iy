package com.itheima.service;

import com.itheima.pojo.Emp;
import com.itheima.pojo.PageBean;

import java.time.LocalDate;

public interface EmpService {

    public PageBean find(String name, Short gender , LocalDate begin, LocalDate end , Integer page , Integer pageSize);


    void deleteBatch(Integer[] ids);

    public void add(Emp emp);

    public Emp findById(Integer id);

    public void update(Emp emp);
}
