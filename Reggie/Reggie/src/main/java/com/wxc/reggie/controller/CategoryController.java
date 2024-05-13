package com.wxc.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wxc.reggie.common.R;
import com.wxc.reggie.entity.Category;
import com.wxc.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> saveSortInfo(@RequestBody Category category) {
        return categoryService.saveSortInfo(category);
    }

    /**
     * 删除分类,根据id删除分类，删除之前做一下判断，是否有关联
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> deleteSortInfo(Long id) {
        return categoryService.deleteSortInfo(id);
    }

    /**
     * 修改分类
     *
     * @param category
     * @return
     */
    @PutMapping
    public R<String> updateSortInfo(@RequestBody Category category) {
        return categoryService.updateSortInfo(category);
    }

    /**
     * 分类管理-分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageSortInfo(int page, int pageSize) {
        return categoryService.pageSortInfo(page, pageSize);
    }

    /**
     * 根据条件查询分类数据
     *
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> getCategoryList(Category category) {
        return categoryService.getListInfo(category);
    }
}
