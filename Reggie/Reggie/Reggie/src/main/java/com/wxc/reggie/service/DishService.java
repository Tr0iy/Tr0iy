package com.wxc.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxc.reggie.common.R;
import com.wxc.reggie.dto.DishDto;
import com.wxc.reggie.entity.Dish;

import java.util.List;


public interface DishService extends IService<Dish> {

    public R<String> saveDishWithFlavor(DishDto dishDto);

    R<Page> pageByDish(int page, int pageSize, String name);

    R<String> updateDishWithFlavor(DishDto dishDto);

    R<DishDto> getDishInfoById(Long id);

    R<String> deleteDish(List<Long> ids);

    R<String> sellStatus(Integer status, List<Long> ids);

    R<List<DishDto>> getDishInfoByCondition(Dish dish);
}
