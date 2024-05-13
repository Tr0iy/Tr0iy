package com.wxc.reggie.dto;

import com.wxc.reggie.entity.OrderDetail;
import com.wxc.reggie.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

}
