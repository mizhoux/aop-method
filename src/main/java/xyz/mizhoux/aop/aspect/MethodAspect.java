package xyz.mizhoux.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import xyz.mizhoux.aop.aspect.anno.MethodAspectAnno;
import xyz.mizhoux.aop.aspect.processor.MethodAspectProcessor;
import org.springframework.stereotype.Component;
import xyz.mizhoux.aop.aspect.processor.impl.MismatchMethodAspectProcessor;

/**
 * 方法切面
 *
 * @author 之叶
 * @date   2019/09/01
 */
@Aspect
@Component
public class MethodAspect implements ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ApplicationContext appContext;

    /**
     * 方法连接点（处理被 @MethodAspectAnno 注解的方法）
     */
    @Pointcut("@annotation(xyz.mizhoux.aop.aspect.anno.MethodAspectAnno)")
    public void methodPointcut() { }

    /**
     * 切入被 @MethodAspectAnno 注解的方法
     *
     * @param point 连接点
     * @param anno  注解
     * @return 方法返回值
     * @throws Throwable 可能抛出的异常
     */
    @SuppressWarnings("unchecked")
    @Around("methodPointcut() && @annotation(anno)")
    public Object doAround(ProceedingJoinPoint point, MethodAspectAnno anno) throws Throwable {
        // 通过注解获取处理器
        MethodAspectProcessor processor = getProcessor(anno);

        // 方法不匹配，即不是要处理的业务方法
        if (!processor.isMatched(point)) {
            // 方法不匹配时的执行动作
            processor.onMismatch(point);
            // 直接执行该方法并返回结果
            return point.proceed();
        }

        // 执行之前
        boolean permitted = processor.onBefore(point);
        // 开始执行的时间
        long startTime = System.currentTimeMillis();

        // 方法返回值
        Object result;
        // 是否抛出了异常
        boolean thrown = false;

        // 目标方法被允许执行
        if (permitted) {
            try {
                // 执行目标方法
                result = point.proceed();
            } catch (Throwable e) {
                // 抛出异常
                thrown = true;
                // 处理异常
                processor.onThrow(point, e);
                // 抛出异常的情况下，则构造一个返回值的实例，用于业务服务方法的返回
                result = processor.getOnThrow(point, e);
            }
        }
        // 目标方法被禁止执行
        else {
            // 禁止执行时的返回值
            result = processor.getOnForbid(point);
        }

        // 切面结束
        processor.onComplete(point, startTime, !permitted, thrown, result);

        return result;
    }

    private MethodAspectProcessor getProcessor(MethodAspectAnno anno) {
        Class<? extends MethodAspectProcessor> processorType = anno.value();

        try {
            return appContext.getBean(processorType);
        } catch (BeansException ex) {
            logger.error("{} 在 Spring 容器中不存在", processorType.getName());
        }

        return appContext.getBean(MismatchMethodAspectProcessor.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }
}
