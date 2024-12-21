package com.zwx.mapper;

import com.github.pagehelper.Page;
import com.zwx.annotation.AutoFill;
import com.zwx.dto.EmployeePageQueryDTO;
import com.zwx.dto.EmployeePassWordChangeDTO;
import com.zwx.entity.Employee;
import com.zwx.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @Select("select * from sky_take_out.employee where username=#{username}")
    Employee getByUserName(String username);

    /**
     * 新增员工信息
     * @param employee
     */
    @Insert("insert into employee (name, username,password, phone, sex, id_number,status, create_time, update_time, create_user, update_user) "+
            "values (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(value = OperationType.INSERT )
    void save(Employee employee);

    /**
     * 分页查询员工信息
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    @Select("select * from sky_take_out.employee where id=#{id}")
    Employee getByUserId(long id);

    /**
     * 修改密码
     * @param employee
     */
    @AutoFill(value = OperationType.UPDATE )
    void update(Employee employee);
}
