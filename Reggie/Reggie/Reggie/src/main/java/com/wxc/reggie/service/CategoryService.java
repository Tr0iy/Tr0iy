package com.wxc.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxc.reggie.common.R;
import com.wxc.reggie.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    R<String> saveSortInfo(Category category);

    R<String> deleteSortInfo(Long id);

    R<Page> pageSortInfo(int page, int pageSize);

    R<String> updateSortInfo(Category category);

    R<List<Category>> getListInfo(Category category);
}
