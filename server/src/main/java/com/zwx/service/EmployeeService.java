package com.zwx.service;

import com.zwx.dto.EmployeeLoginDTO;
import com.zwx.entity.Employee;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {
    Employee login(EmployeeLoginDTO employeeLoginDTO);
}
