package com.zwx.controller.admin;


import com.zwx.constant.JwtClaimsConstant;
import com.zwx.dto.EmployeeDTO;
import com.zwx.dto.EmployeeLoginDTO;
import com.zwx.dto.EmployeePageQueryDTO;
import com.zwx.dto.EmployeePassWordChangeDTO;
import com.zwx.entity.Employee;
import com.zwx.properties.JwtProperties;
import com.zwx.result.PageResult;
import com.zwx.result.Result;
import com.zwx.service.EmployeeService;
import com.zwx.utils.JwtUtil;
import com.zwx.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工：{}",employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("员工信息分页查询")
    public Result<PageResult>  page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工信息分页查询{}",employeePageQueryDTO);
        PageResult pageResult=employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    @PutMapping("/editPassword")
    @ApiOperation("修改密码")
    public Result editPassword(@RequestBody EmployeePassWordChangeDTO employeePassWordChangeDTO){
        log.info("员工修改密码：{}",employeePassWordChangeDTO.getEmpId());
        employeeService.editPassword(employeePassWordChangeDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用员工账号")
    public Result setStatus(@PathVariable Integer status,long id){
        log.info("修改员工账号状态");
        employeeService.setStatus(status,id);
        return Result.success();
    }

    @GetMapping("{id}")
    @ApiOperation("根据id获取员工信息")
    public Result<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息");
        Employee employee=employeeService.getById(id);
        return Result.success(employee);
    }

    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工信息{}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result<String> logout(){
        return Result.success();
    }
}
