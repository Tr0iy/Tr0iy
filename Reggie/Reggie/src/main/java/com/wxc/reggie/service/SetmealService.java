package com.wxc.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxc.reggie.common.R;
import com.wxc.reggie.dto.SetmealDishDto;
import com.wxc.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    R<String> saveSetmeal(SetmealDishDto setmealDto);

    R<Page> pageBySetmeal(int page, int pageSize, String name);

    R<String> sellStatus(Integer status, List<Long> ids);

    R<String> deleteSetmeal(List<Long> ids);

    R<SetmealDishDto> getSetmealDish(Long id);

    R<String> updateSetmeal(SetmealDishDto setmealDishDto);

    R<List<Setmeal>> selectSetmealList(Setmeal setmeal);
}
