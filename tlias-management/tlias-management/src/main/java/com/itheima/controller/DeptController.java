package com.itheima.controller;

import com.itheima.mapper.DeptMapper;
import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depts")
public class DeptController {


    /*@Autowired
    private DeptMapper deptMapper;*/

    @Autowired
    private DeptService deptService;

    /**
     * 列表查询：查询所有的部门表数据
     */

    @GetMapping
    public Result findAll(){
        //查询所有部门数据
        List<Dept> depts = deptService.findAll();
        return Result.success(depts);

    }


    /**
     * 删除部门
     */
    @DeleteMapping("/depts/{id}")
    public Result deleteById(@PathVariable Integer id){

        deptService.deleteById(id);

        return Result.success();
    }

    /*
        添加部门
     */

    @PostMapping("/depts")
    public Result add(@RequestBody Dept dept){

        deptService.add(dept);

        return Result.success();
    }



     /*
        根据id查询
     */

    @GetMapping("/depts/{id}")
    public Result findById(@PathVariable Integer id){

        Dept dept = deptService.findById(id);

        return Result.success(dept);
    }

    @PutMapping("/depts")
    public Result update(@RequestBody Dept dept){
        deptService.update(dept);
        return Result.success();
    }



}
