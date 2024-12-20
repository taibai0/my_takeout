package com.zwx.mapper;

import com.zwx.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    @Select("select * from sky_take_out.employee where username=#{username}")
    Employee getByUserName(String username);
}
