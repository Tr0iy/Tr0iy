package com.wxc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxc.reggie.common.R;
import com.wxc.reggie.entity.AddressBook;

import java.util.List;

public interface AddressBookService extends IService<AddressBook> {
    R<String> deleteAddressBook(Long id);

    R<String> updateAddressBook(AddressBook addressBook);


    R getAddressBookById(Long id);

    R<AddressBook> setDefaultAddressBook(AddressBook addressBook);

    R<List<AddressBook>> selectAddressBookList(AddressBook addressBook);

    R<AddressBook> getDefaultAddressBook();
}
