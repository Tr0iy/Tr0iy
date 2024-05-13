package com.wxc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxc.reggie.common.BaseContext;
import com.wxc.reggie.common.R;
import com.wxc.reggie.entity.ShoppingCart;
import com.wxc.reggie.mapper.ShoppingCartMapper;
import com.wxc.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public R<ShoppingCart> subDishOrSetmeal(ShoppingCart shoppingCart) {
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        //代表数量减少的是菜品数量
        if (dishId != null) {
            // 通过dishId查出购物车对象
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
            // 这里必须要加两个条件，否则会出现用户互相修改对方与自己购物车中相同套餐或者是菜品的数量
            queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
            ShoppingCart cart1 = shoppingCartMapper.selectOne(queryWrapper);
            cart1.setNumber(cart1.getNumber() - 1);
            Integer LatestNumber = cart1.getNumber();
            if (LatestNumber > 0) {
                //对数据进行更新操作
                shoppingCartMapper.updateById(cart1);
            } else if (LatestNumber == 0) {
                //如果购物车的菜品数量减为0，那么就把菜品从购物车删除
                shoppingCartMapper.deleteById(cart1.getId());
            } else if (LatestNumber < 0) {
                return R.error("操作异常");
            }

            return R.success(cart1);
        }

        Long setmealId = shoppingCart.getSetmealId();
        if (setmealId != null) {
            // 代表是套餐数量减少
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId).eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
            ShoppingCart cart2 = shoppingCartMapper.selectOne(queryWrapper);
            cart2.setNumber(cart2.getNumber() - 1);
            Integer LatestNumber = cart2.getNumber();
            if (LatestNumber > 0) {
                //对数据进行更新操作
                shoppingCartMapper.updateById(cart2);
            } else if (LatestNumber == 0) {
                //如果购物车的套餐数量减为0，那么就把套餐从购物车删除
                shoppingCartMapper.deleteById(cart2.getId());
            } else if (LatestNumber < 0) {
                return R.error("操作异常");
            }
            return R.success(cart2);
        }
        //如果两个大if判断都进不去
        return R.error("操作异常");
    }

    @Override
    public R<ShoppingCart> addDishOrSetmeal(ShoppingCart shoppingCart) {

        // 设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        if (dishId != null) {
            // 添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);

        } else {
            // 添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        // 查询当前菜品或者套餐是否在购物车中
        // SQL:select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart cartServiceOne = shoppingCartMapper.selectOne(queryWrapper);

        if (cartServiceOne != null) {
            //如果已经存在，就在原来数量基础上加一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartMapper.updateById(cartServiceOne);
        } else {
            // 如果不存在，则添加到购物车，数量默认就是一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }

    @Override
    public R<String> cleanShoppingCart() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        shoppingCartMapper.delete(queryWrapper);

        return R.success("清空购物车成功");
    }

    @Override
    public R<List<ShoppingCart>> selectShoppingCart() {

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartMapper.selectList(queryWrapper);

        return R.success(list);
    }
}
