package com.wxc.reggie.dto;

import com.wxc.reggie.entity.Setmeal;
import com.wxc.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDishDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
