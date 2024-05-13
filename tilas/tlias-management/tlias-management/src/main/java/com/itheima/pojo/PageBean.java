package com.itheima.pojo;

import lombok.Data;

import java.util.List;

@Data // lombok
public class PageBean {

    private List rows;//当前页的数据
    private Long total;//总记录数

}
