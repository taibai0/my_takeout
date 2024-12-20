package com.zwx.service.impl;

import com.zwx.constant.MessageConstant;
import com.zwx.constant.StatusConstant;
import com.zwx.dto.EmployeeLoginDTO;
import com.zwx.entity.Employee;
import com.zwx.exception.AccountNotFoundException;
import com.zwx.exception.PasswordException;
import com.zwx.mapper.EmployeeMapper;
import com.zwx.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


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
}
