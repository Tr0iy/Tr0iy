package com.itheima.mapper;



import com.itheima.pojo.Emp;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;


@Mapper
public interface EmpMapper {
/*
    //查询当前页记录
    @Select("select * from emp limit #{start} , #{pageSize};")
    public List<Emp> find(String name, Short gender, LocalDate begin, LocalDate end, Integer start, Integer pageSize);

    //查询总记录数

    @Select("select count(*) from emp;")
    public Long getTotal();*/

    //@Select("select * from emp")
    public List<Emp> find(String name, Short gender, LocalDate begin, LocalDate end);



    public void deleteBatch(Integer[] ids);

    /*
        alt + 鼠标左键，整列编辑
     */

    @Insert("insert into emp (username, name, gender, image, job, entrydate, dept_id, create_time, update_time) values " +
            "(#{username},\n" +
            "            #{name},\n" +
            "            #{gender},\n" +
            "            #{image},\n" +
            "            #{job},\n" +
            "            #{entrydate},\n" +
            "            #{deptId},\n" +
            "            #{createTime},\n" +
            "            #{updateTime});")
    public void add(Emp emp);


    @Select("select * from emp where  id = #{id};")
    public Emp findById(Integer id);


    @Update("update emp set name = #{name}," +
            "username = #{username}," +
            "gender = #{gender}," +
            "image = #{image}," +
            "job = #{job}," +
            "entrydate = #{entrydate}," +
            "dept_id = #{deptId}," +
            "update_time = #{updateTime}" +
            " where  id = #{id};")
    public void update(Emp emp);


}
