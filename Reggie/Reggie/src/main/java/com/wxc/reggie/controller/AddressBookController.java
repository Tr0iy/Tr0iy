package com.wxc.reggie.controller;

import com.wxc.reggie.common.BaseContext;
import com.wxc.reggie.common.R;
import com.wxc.reggie.entity.AddressBook;
import com.wxc.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址簿管理
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<AddressBook> saveAddressBook(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 根据地址id删除用户地址
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> deleteAddressBook(@RequestParam("ids") Long id) {
        return addressBookService.deleteAddressBook(id);
    }

    /**
     * 修改收货地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<String> updateAddressBook(@RequestBody AddressBook addressBook) {
        return addressBookService.updateAddressBook(addressBook);
    }

    /**
     * 设置默认地址
     */
    @PutMapping("default")
    public R<AddressBook> setDefaultAddressBook(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        return addressBookService.setDefaultAddressBook(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R getAddressBookById(@PathVariable Long id) {
        return addressBookService.getAddressBookById(id);
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public R<AddressBook> getDefaultAddressBook() {
        return addressBookService.getDefaultAddressBook();
    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        return addressBookService.selectAddressBookList(addressBook);
    }
}