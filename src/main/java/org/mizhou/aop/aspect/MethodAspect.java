package org.mizhou.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.mizhou.aop.aspect.anno.MethodAspectAnno;
import org.mizhou.aop.aspect.processor.MethodAspectProcessor;
import org.springframework.stereotype.Component;

/**
 * ServiceMethodAspect
 *
 * @author 之叶
 * @date   2019/09/01
 */
@Aspect
@Component
public class MethodAspect {

    /**
     * 方法连接点（处理被 @MethodAspectAnno 注解的方法）
     */
    @Pointcut("@annotation(org.mizhou.aop.aspect.anno.MethodAspectAnno)")
    public void methodPointcut() { }

    /**
     * 切入被 @MethodAspectAnno 注解的方法
     *
     * @param point 连接点
     * @param anno 注解
     *
     * @return 方法返回值
     * @throws Throwable 可能抛出的异常
     */
    @Around("methodPointcut() && @annotation(anno)")
    public Object doAround(ProceedingJoinPoint point, MethodAspectAnno anno) throws Throwable {
        // 通过注解获取处理器
        MethodAspectProcessor processor = MethodAspectProcessor.get(anno);

        // 方法不匹配，即不是要处理的业务方法
        if (!processor.isMatched(point)) {
            // 方法不匹配时的执行动作
            processor.onMismatch(point);
            // 直接执行该方法并返回结果
            return point.proceed();
        }

        // 记下开始执行的时间
        long startTime = System.currentTimeMillis();

        // 方法返回值
        Object result;
        try {
            // 执行目标方法
            result = point.proceed();
            // 正常返回
            processor.onReturn(point, result);
        } catch (Throwable e) {
            // 处理异常
            processor.onThrow(point, e);
            // 抛出异常的情况下，则构造一个返回值的实例，用于业务服务方法的返回
            result = processor.returnWhenThrowing(point, e);
        }

        // 切面结束
        processor.onComplete(point, startTime, result);

        return result;
    }
}
