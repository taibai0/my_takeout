package com.zwx.service;

import com.zwx.dto.EmployeeDTO;
import com.zwx.dto.EmployeeLoginDTO;
import com.zwx.dto.EmployeePageQueryDTO;
import com.zwx.dto.EmployeePassWordChangeDTO;
import com.zwx.entity.Employee;
import com.zwx.result.PageResult;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employeeDTO);

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void editPassword(EmployeePassWordChangeDTO employeePassWordChangeDTO);

    void setStatus(Integer status,Long id);

    Employee getById(Long id);

    void update(EmployeeDTO employeeDTO);
}
