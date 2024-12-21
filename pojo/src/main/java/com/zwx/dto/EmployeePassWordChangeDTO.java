package com.zwx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePassWordChangeDTO implements Serializable {
    //员工id
    private int empId;

    //新密码
    private String newPassword;

    //旧密码
    private String oldPassword;
}
