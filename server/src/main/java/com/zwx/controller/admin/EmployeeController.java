package com.zwx.controller.admin;


import com.zwx.constant.JwtClaimsConstant;
import com.zwx.dto.EmployeeLoginDTO;
import com.zwx.entity.Employee;
import com.zwx.properties.JwtProperties;
import com.zwx.result.Result;
import com.zwx.service.EmployeeService;
import com.zwx.utils.JwtUtil;
import com.zwx.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO){
        log.info("员工登录:{}",employeeLoginDTO);

        Employee employee=employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String,Object> claims=new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID,employee.getId());
        String token= JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO=EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getName())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }
}
