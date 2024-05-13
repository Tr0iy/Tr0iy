package com.wxc.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxc.reggie.common.R;
import com.wxc.reggie.entity.OrderDetail;

public interface OrderDetailService extends IService<OrderDetail> {
    R<Page> pageOrderDetail(int page, int pageSize);
}
