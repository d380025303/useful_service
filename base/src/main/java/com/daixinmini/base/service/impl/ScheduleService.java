package com.daixinmini.base.service.impl;

import com.daixinmini.base.service.IScheduleService;
import com.daixinmini.base.vo.ScheduleInvokeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
public class ScheduleService implements IScheduleService, InitializingBean {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ScheduleSettingConfig scheduleSettingConfig;
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<ScheduleInvokeVo> invokeList = scheduleSettingConfig.getInvoke();
        if (invokeList == null) {
            return;
        }
        for (ScheduleInvokeVo invokeVo : invokeList) {
            Object[] args = invokeVo.getArgs();
            String methodName = invokeVo.getMethodName();
            String cron = invokeVo.getCron();
            String[] split = methodName.split("\\.");
            String beanName = split[0];
            String targetMethodName = split[1];
            logger.info("beanName: " + beanName);
            logger.info("targetMethodName: " + beanName);
            logger.info("cron: " + cron);

            Object bean = applicationContext.getBean(beanName);
            Class<?> aClass = AopUtils.getTargetClass(bean);
            Method[] methods = aClass.getDeclaredMethods();
            Method targetMethod = null;
            for (Method method : methods) {
                method.setAccessible(true);
                String nowMethodName = method.getName();
                if (nowMethodName.equals(targetMethodName)) {
                    targetMethod = method;
                }
            }
            Method finalTargetMethod = targetMethod;

            ScheduledFuture future = threadPoolTaskScheduler.schedule(() -> {
                try {
                    finalTargetMethod.invoke(bean, args);
                } catch (InvocationTargetException e) {
                    Throwable targetException = e.getTargetException();
                    logger.error("e: " + targetException.getMessage(), targetException);
                } catch (Exception e) {
                    logger.error("e: " + e.getMessage(), e);
                }
            }, triggerContext -> {
                CronTrigger trigger = new CronTrigger(cron);
                return trigger.nextExecutionTime(triggerContext);
            });
        }
    }
}
