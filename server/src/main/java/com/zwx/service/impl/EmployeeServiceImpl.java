package com.zwx.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zwx.constant.MessageConstant;
import com.zwx.constant.PasswordConstant;
import com.zwx.constant.StatusConstant;
import com.zwx.context.BaseContext;
import com.zwx.dto.EmployeeDTO;
import com.zwx.dto.EmployeeLoginDTO;
import com.zwx.dto.EmployeePageQueryDTO;
import com.zwx.dto.EmployeePassWordChangeDTO;
import com.zwx.entity.Employee;
import com.zwx.exception.AccountNotFoundException;
import com.zwx.exception.PasswordException;
import com.zwx.mapper.EmployeeMapper;
import com.zwx.result.PageResult;
import com.zwx.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;


@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //根据用户名查询数据库中的数据
        Employee employee=employeeMapper.getByUserName(username);

        if(employee==null){
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对，密码使用md5加密处理
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        if(!password.equals(employee.getPassword())){
            throw new PasswordException(MessageConstant.PASSWORD_ERROR);
        }

        if(employee.getStatus()== StatusConstant.DISABLE){
            //账号被锁定
            throw new PasswordException(MessageConstant.ACCOUNT_LOCKED);
        }

        return employee;
    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();
        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        employeeMapper.save(employee);
    }

    /**
     * 分页查询员工信息
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> page=employeeMapper.pageQuery(employeePageQueryDTO);
        long total=page.getTotal();
        List<Employee> result=page.getResult();

        return new PageResult(total,result);
    }

    /**
     * 员工修改密码
     * @param employeePassWordChangeDTO
     */
    @Override
    public void editPassword(EmployeePassWordChangeDTO employeePassWordChangeDTO) {
       Employee employee=employeeMapper.getByUserId(BaseContext.getCurrentId());

       employeePassWordChangeDTO.setOldPassword(DigestUtils.md5DigestAsHex(employeePassWordChangeDTO.getOldPassword().getBytes()));
       if(!employeePassWordChangeDTO.getOldPassword().equals(employee.getPassword())){
           throw new PasswordException(MessageConstant.PASSWORD_ERROR);
       }

        String passWord=DigestUtils.md5DigestAsHex(employeePassWordChangeDTO.getNewPassword().getBytes());
       Employee employee1=Employee.builder()
                        .id(BaseContext.getCurrentId())
                        .password(passWord)
                        .build();
        employeeMapper.update(employee1);
    }

    /**
     * 修改员工账号状态
     * @param status
     */
    @Override
    public void setStatus(Integer status,Long id) {
        Employee employee=Employee.builder()
                .id(id)
                .status(status)
                .build();

        employeeMapper.update(employee);
    }

    /**
     * 根据id获取员工信息
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getByUserId(id);
        return employee;
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employeeMapper.update(employee);
    }
}
