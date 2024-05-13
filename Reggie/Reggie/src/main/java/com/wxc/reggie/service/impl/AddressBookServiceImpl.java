package com.wxc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxc.reggie.common.BaseContext;
import com.wxc.reggie.common.R;
import com.wxc.reggie.entity.AddressBook;
import com.wxc.reggie.mapper.AddressBookMapper;
import com.wxc.reggie.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public R<String> deleteAddressBook(Long id) {
        if (id == null) {
            return R.error("请求异常");
        }
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getId, id).eq(AddressBook::getUserId, BaseContext.getCurrentId());
        addressBookMapper.delete(queryWrapper);
        return R.success("删除地址成功");
    }

    @Override
    public R<String> updateAddressBook(AddressBook addressBook) {
        if (addressBook == null) {
            return R.error("请求异常");
        }
        addressBookMapper.updateById(addressBook);
        return R.success("修改成功");
    }


    @Override
    public R getAddressBookById(Long id) {
        AddressBook addressBook = addressBookMapper.selectById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    @Override
    public R<AddressBook> setDefaultAddressBook(AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookMapper.update(addressBook, wrapper);

        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBookMapper.updateById(addressBook);
        return R.success(addressBook);
    }

    @Override
    public R<List<AddressBook>> selectAddressBookList(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        return R.success(addressBookMapper.selectList(queryWrapper));
    }

    @Override
    public R<AddressBook> getDefaultAddressBook() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookMapper.selectOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }
}
