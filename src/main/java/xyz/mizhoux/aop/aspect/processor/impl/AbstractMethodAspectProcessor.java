package xyz.mizhoux.aop.aspect.processor.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import xyz.mizhoux.aop.aspect.anno.MethodAspectAnno;
import xyz.mizhoux.aop.aspect.processor.MethodAspectProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 提供默认的两个功能：<br/>
 * （1）方法不匹配时记录日志<br/>
 * （2）目标方法抛出异常时记录日志
 *
 * @author 之叶
 * @date   2019/08/28
 */
public abstract class AbstractMethodAspectProcessor<R> implements MethodAspectProcessor<R> {

    @Override
    public void onMismatch(ProceedingJoinPoint point) {
        Logger logger = getLogger(point);
        String logTag = getLogTag(point);

        // 获得方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 获得方法
        Method method = signature.getMethod();
        // 获得方法的 @MethodAspectAnno 注解
        MethodAspectAnno anno = method.getAnnotation(MethodAspectAnno.class);
        // 获得方法切面处理器的 Class
        Class<? extends MethodAspectProcessor> processorType = anno.value();

        String processorName = processorType.getSimpleName();

        // 如果是接口或者抽象类
        if (processorType.isInterface() || Modifier.isAbstract(processorType.getModifiers())) {
            logger.warn("{} 需要指定具体的切面处理器，因为 {} 是接口或者抽象类", logTag, processorName);
            return;
        }

        logger.warn("{} 不是 {} 可以处理的方法，或者 {} 在 Spring 容器中不存在", logTag, processorName, processorName);
    }

    @Override
    public void onThrow(ProceedingJoinPoint point, Throwable e) {
        Logger logger = getLogger(point);
        String logTag = getLogTag(point);

        logger.error("{} 执行时出错", logTag, e);
    }

    /**
     * 获得被代理类的 Logger
     *
     * @param point 连接点
     * @return 被代理类的 Logger
     */
    protected Logger getLogger(ProceedingJoinPoint point) {
        Object target = point.getTarget();

        return LoggerFactory.getLogger(target.getClass());
    }

    /**
     * LogTag = 类名.方法名
     *
     * @param point 连接点
     * @return 目标类名.执行方法名
     */
    protected String getLogTag(ProceedingJoinPoint point) {
        Object target = point.getTarget();
        String className = target.getClass().getSimpleName();

        MethodSignature signature = (MethodSignature) point.getSignature();
        String methodName = signature.getName();

        return className + "." + methodName;
    }
}
