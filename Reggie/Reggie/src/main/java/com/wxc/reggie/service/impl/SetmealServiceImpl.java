package com.wxc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxc.reggie.common.CustomException;
import com.wxc.reggie.common.R;
import com.wxc.reggie.dto.SetmealDishDto;
import com.wxc.reggie.entity.Category;
import com.wxc.reggie.entity.Setmeal;
import com.wxc.reggie.entity.SetmealDish;
import com.wxc.reggie.mapper.CategoryMapper;
import com.wxc.reggie.mapper.SetmealDishMapper;
import com.wxc.reggie.mapper.SetmealMapper;
import com.wxc.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public R<String> saveSetmeal(SetmealDishDto setmealDto) {

        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // 向 setmeal_dish 表插入多条数据
        for (int i = 0; i < setmealDishes.size(); i++) {
            setmealDishMapper.insert(setmealDishes.get(i));
        }
        return R.success("条件成功！");
    }

    @Override
    public R<Page> pageBySetmeal(int page, int pageSize, String name) {
        Page<Setmeal> mealPage = new Page<>(page, pageSize);
        Page<SetmealDishDto> mealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealMapper.selectPage(mealPage, queryWrapper);

        // pageInfo 里面的 records 是没有categoryName的！
        // 所以 records 直接 忽略，要在后面手动赋值
        BeanUtils.copyProperties(mealPage, mealDtoPage, "records");
        List<Setmeal> records = mealPage.getRecords();

        // 处理 records
        List<SetmealDishDto> list = records.stream().map((item) -> {
            SetmealDishDto setmealDto = new SetmealDishDto();
            // item是一条records(list)记录，将Setmeal中的字段赋值给 setmealDto
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        mealDtoPage.setRecords(list);

        return R.success(mealDtoPage);
    }

    @Override
    public R<String> sellStatus(Integer status, List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ids != null, Setmeal::getId, ids);

        List<Setmeal> list = setmealMapper.selectList(queryWrapper);

        for (Setmeal setmeal : list) {
            if (setmeal != null) {
                setmeal.setStatus(status);
                setmealMapper.updateById(setmeal);
            }
        }

        return R.success("售卖状态修改成功");
    }

    @Override
    @Transactional
    public R<String> deleteSetmeal(List<Long> ids) {
        // 查菜品的状态，是否可以删除（）
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.in(Setmeal::getId, ids);
        setmealQueryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(setmealQueryWrapper);
        if (count > 0) {
            throw new CustomException("菜品正在售卖中！不能删除！");
        }

        setmealMapper.deleteBatchIds(ids);

        LambdaQueryWrapper<SetmealDish> setmealDishQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishQueryWrapper.in(SetmealDish::getSetmealId, ids);

        setmealDishMapper.delete(setmealDishQueryWrapper);
        return R.success("删除菜品成功！");
    }

    @Override
    public R<SetmealDishDto> getSetmealDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDishDto setmealDto = new SetmealDishDto();
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
        //在关联表中查询，setmealdish
        queryWrapper.eq(id != null, SetmealDish::getSetmealId, id);

        if (setmeal != null) {
            BeanUtils.copyProperties(setmeal, setmealDto);
            List<SetmealDish> list = setmealDishMapper.selectList(queryWrapper);
            setmealDto.setSetmealDishes(list);
            return R.success(setmealDto);
        }
        return null;
    }

    @Override
    public R<String> updateSetmeal(SetmealDishDto setmealDishDto) {
        if (setmealDishDto == null) {
            return R.error("请求异常");
        }

        if (setmealDishDto.getSetmealDishes() == null) {
            return R.error("套餐没有菜品,请添加套餐");
        }
        List<SetmealDish> setmealDishes = setmealDishDto.getSetmealDishes();
        Long setmealId = setmealDishDto.getId();

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealId);
        setmealDishMapper.delete(queryWrapper);

        //为setmeal_dish表填充相关的属性
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
        }
        //批量把setmealDish保存到setmeal_dish表
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDishMapper.insert(setmealDish);
        }
        setmealMapper.updateById(setmealDishDto);

        return R.success("套餐修改成功");
    }

    @Override
    public R<List<Setmeal>> selectSetmealList(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealMapper.selectList(queryWrapper);

        return R.success(list);
    }
}