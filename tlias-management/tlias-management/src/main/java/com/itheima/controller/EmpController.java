package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;


    @GetMapping
    public Result find(String name, Short gender , LocalDate begin, LocalDate end ,Integer page , Integer pageSize){

        PageBean pageBean = empService.find(name, gender, begin, end, page, pageSize);
        return Result.success(pageBean);
    }
    @DeleteMapping("/{ids}")
    public Result deleteBatch(@PathVariable Integer[] ids){
        empService.deleteBatch(ids);
        return Result.success();
    }


    @PostMapping
    public Result add(@RequestBody Emp emp){
        empService.add(emp);
        return Result.success();
    }


    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id){
        Emp emp = empService.findById(id);
        return Result.success(emp);
    }


    @PutMapping
    public Result update(@RequestBody Emp emp){
        empService.update(emp);
        return Result.success();
    }

}
