package com.zwx.aspect;

import com.zwx.annotation.AutoFill;
import com.zwx.constant.AutoFillConstant;
import com.zwx.context.BaseContext;
import com.zwx.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Slf4j
@Component
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.zwx.mapper.*.*(..)) && @annotation(com.zwx.annotation.AutoFill)")
    public void autoFillPointCut() {}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充");
        //获取当前被拦截方法上的数据库操作实现
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill=methodSignature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType=autoFill.value();
        //获取当前被拦截方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if(args==null||args.length==0){
            return;
        }
        Object entity=args[0];
        //准备赋值的数据
        LocalDateTime now=LocalDateTime.now();
        Long currentId= BaseContext.getCurrentId();
        //根据不同的操作类型，为对应的属性通过反射来赋值
        if(operationType==OperationType.INSERT){
            //为四个公共字段赋值
            try {
                Method setCreateTime=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
                Method setUpdateTime=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setCreateUser=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                Method setUpdateUser=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

                //通过反射为对象属性赋值
                setCreateTime.invoke(entity,now);
                setUpdateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (operationType == OperationType.UPDATE) {
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
}
