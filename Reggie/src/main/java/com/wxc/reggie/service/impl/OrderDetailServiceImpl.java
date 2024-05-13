package com.wxc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxc.reggie.common.BaseContext;
import com.wxc.reggie.common.R;
import com.wxc.reggie.dto.OrdersDto;
import com.wxc.reggie.entity.OrderDetail;
import com.wxc.reggie.entity.Orders;
import com.wxc.reggie.mapper.OrderDetailMapper;
import com.wxc.reggie.mapper.OrdersMapper;
import com.wxc.reggie.service.OrderDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public R<Page> pageOrderDetail(int page, int pageSize) {
        // 分页构造器对象
        Page<Orders> pageOrders = new Page<>(page, pageSize);
        Page<OrdersDto> pageDto = new Page<>(page, pageSize);
        // 构造条件查询对象
        LambdaQueryWrapper<Orders> queryWrapperOrders = new LambdaQueryWrapper<>();
        queryWrapperOrders.eq(Orders::getUserId, BaseContext.getCurrentId());

        // 这里是直接把当前用户分页的全部结果查询出来，要添加用户id作为查询条件，
        // 否则会出现用户可以查询到其他用户的订单情况
        // 添加排序条件，根据更新时间降序排列
        queryWrapperOrders.orderByDesc(Orders::getOrderTime);
        ordersMapper.selectPage(pageOrders, queryWrapperOrders);

        // 对OrderDto进行需要的属性赋值
        List<Orders> records = pageOrders.getRecords();

        List<OrdersDto> orderDtoList = records.stream().map((item) -> {
            OrdersDto orderDto = new OrdersDto();
            // 此时的orderDto对象里面orderDetails属性还是空 下面准备为它赋值
            Long orderId = item.getId();//获取订单id
            List<OrderDetail> orderDetailList = getOrderDetailListByOrderId(orderId);
            BeanUtils.copyProperties(item, orderDto);
            // 对orderDto进行OrderDetails属性的赋值
            orderDto.setOrderDetails(orderDetailList);
            return orderDto;
        }).collect(Collectors.toList());

        // 使用dto的分页有点难度!!!
        BeanUtils.copyProperties(pageOrders, pageDto, "records");
        pageDto.setRecords(orderDtoList);
        return R.success(pageDto);
    }

    private List<OrderDetail> getOrderDetailListByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(queryWrapper);
        return orderDetailList;
    }
}