package com.wxc.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
public class Employee {

    // serialVersionUID用作Serializable类中的版本控件
    // JavaBean规范中要实现java.io.Serializable接口实现序列化!!!
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    // 数据库里面是id_number 在application.yml配置即可，将数据库中表名和字段名中的下划线去掉，按照驼峰命名法映射
    private String idNumber;

    private Long status;

    // LocalDateTime : JDK8 线程安全
    @TableField(fill = FieldFill.INSERT)  // 插入时填充字段
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时填充字段
    private LocalDateTime updateTime;

    // MP中用于标识非主键的字段。将数据库列与 JavaBean 中的属性进行映射，
    // fill 字段填充标记，表示在插入时自动填充
    @TableField(fill = FieldFill.INSERT)  // 插入时填充字段
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时填充字段
    private Long updateUser;

}
