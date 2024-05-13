package com.wxc.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wxc.reggie.common.R;
import com.wxc.reggie.entity.Employee;
import com.wxc.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 员工管理
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登入
     *
     * @param request
     * @param employee
     * @return
     * @RequestBody: 用来接收前端传递给后端的json字符串中的数据的
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // @RequestBody : 用来接收前端传递给后端的json字符串中的数据的
        return employeeService.login(request, employee);
    }

    /**
     * 员工退出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        return employeeService.logout(request);
    }

    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> saveEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        return employeeService.saveEmployee(request, employee);
    }

    /**
     * 员工信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageByEmployee(int page, int pageSize, String name) {
        log.info("page = {},pageSize = {},name = {}", page, pageSize, name);
        return employeeService.pageByEmployee(page, pageSize, name);
    }

    /**
     * 根据 ID 来修改员工信息
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> updateEmployeeById(HttpServletRequest request, @RequestBody Employee employee) {
        return employeeService.updateEmployeeById(request, employee);
    }

    /**
     * 查询员工信息
     *
     * @param id
     * @return
     * @PathVariable id在请求路径里面
     */
    @GetMapping("/{id}")
    public R<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    /**
     * 删除员工
     *
     * @param request
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> deleteEmployee(HttpServletRequest request, Long id) {
        return employeeService.deleteEmployee(request, id);
    }
}
