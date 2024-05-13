package com.wxc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxc.reggie.common.R;
import com.wxc.reggie.entity.Employee;
import com.wxc.reggie.mapper.EmployeeMapper;
import com.wxc.reggie.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public R<Employee> login(HttpServletRequest request, Employee employee) {
        // 1. 将页面提交的密码password进行md5加密处理, 得到加密后的字符串
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2. 根据页面提交的用户名username查询数据库中员工数据信息
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        // Employee::getUsername 查询SQL表里面的用户名
        // employee.getUsername() 获取JavaBean的用户名
        // eq()表示查询符合条件的数据
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeMapper.selectOne(queryWrapper);

        // 3. 如果没有查询到, 则返回登录失败结果 || 密码比对，如果不一致, 则返回登录失败结果
        if (emp == null || !emp.getPassword().equals(password)) {
            return R.error("登入失败");
        }

        // 4. 查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        // 6. 登录成功，将员工id存入Session, 并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    @Override
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @Override
    public R<String> saveEmployee(HttpServletRequest request, Employee employee) {
        //设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //获得当前登录用户的id
//        Long empId = (Long) request.getSession().getAttribute("employee");

//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeMapper.insert(employee);
        return R.success("新增员工成功");
    }

    @Override
    public R<Page> pageByEmployee(int page, int pageSize, String name) {

        // 构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        // 添加过滤条件(不为空就添加)
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // 添加排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        // 执行查询
        employeeMapper.selectPage(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @Override
    public R<String> deleteEmployee(HttpServletRequest request, Long id) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getId, id);
        super.removeById(id);
        return R.success("删除成功");
    }

    @Override
    public R<Employee> getEmployeeById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询出来！");
    }

    @Override
    public R<String> updateEmployeeById(HttpServletRequest request, Employee employee) {
        Long empId = (Long) request.getSession().getAttribute("employee");

        // employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);

        employeeMapper.updateById(employee);
        System.out.println(employee);
        return R.success("员工信息修改成功！");
    }

}
