package org.mizhou.aop.aspect.processor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.mizhou.aop.aspect.anno.MethodAspectAnno;
import org.mizhou.aop.aspect.anno.Sharable;
import org.mizhou.aop.aspect.processor.impl.MismatchMethodAspectProcessor;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 方法切面处理器
 *
 * @param <R> 方法返回值的类型
 *
 * @author 之叶
 * @date   2019/08/28
 */
public interface MethodAspectProcessor<R> {

    /**
     * 用于缓存被 @Sharable 注解的 MethodAspectProcessor（即线程安全可共享的）
     */
    Map<Class, MethodAspectProcessor> PROCESSOR_CACHE = new ConcurrentHashMap<>();
    
    /**
     * 是否是要处理的方法
     *
     * @param point 方法的连接点
     * @return 是要处理的方法返回 true，否则返回 false
     */
    boolean isMatched(ProceedingJoinPoint point);

    /**
     * 如果是不要处理的方法，执行的动作
     *
     * @param point 方法的连接点
     */
    default void onMismatch(ProceedingJoinPoint point) {

    }

    // 下面的方法，只在 isMatched 返回 true 时有效

    /**
     * 执行之前的动作
     *
     * @param point 方法的连接点
     */
    default void onBefore(ProceedingJoinPoint point) {

    }

    /**
     * 正常返回时，执行的动作
     *
     * @param point  方法的连接点
     * @param result 方法返回的结果
     */
    default void onReturn(ProceedingJoinPoint point, R result) {

    }

    /**
     * 抛出异常时，执行的动作
     *
     * @param point 方法的连接点
     * @param e     抛出的异常
     */
    void onThrow(ProceedingJoinPoint point, Throwable e);

    /**
     * 构建抛出异常时的返回值
     *
     * @param point 方法的连接点
     * @param e     抛出的异常
     * @return 抛出异常时的返回值
     */
    R returnWhenThrowing(ProceedingJoinPoint point, Throwable e);

    /**
     * 切面完成时，执行的动作
     *
     * @param point     方法的连接点
     * @param startTime 执行的开始时间
     * @param result    执行获得的结果
     */
    void onComplete(ProceedingJoinPoint point, long startTime, R result);

    /**
     * 获取 和被注解方法匹配的 切面处理器
     *
     * @param anno 注解
     * @return 匹配的切面处理器
     * @throws Exception 反射创建切面处理器时的异常
     */
    static MethodAspectProcessor get(MethodAspectAnno anno) throws Exception {
        // 获取方法处理器的类型
        Class<? extends MethodAspectProcessor> processorType = anno.value();
        Sharable sharableAnno = processorType.getAnnotation(Sharable.class);

        // processorType 上存在 @Sharable 注解，方法处理器可共享
        if (sharableAnno != null) {
            // 尝试先从缓存中获取
            MethodAspectProcessor processor = PROCESSOR_CACHE.get(processorType);
            // 缓存中存在对应的方法处理器
            if (processor != null) {
                return processor;
            }
        }

        // 如果指定的处理器类是接口或者抽象类
        if (processorType.isInterface() || Modifier.isAbstract(processorType.getModifiers())) {
            processorType = MismatchMethodAspectProcessor.class;
        }

        // 通过反射新建一个对应的方法处理器
        MethodAspectProcessor processor = processorType.newInstance();

        // 处理器可共享
        if (sharableAnno != null) {
            // 对 方法处理器 进行缓存
            PROCESSOR_CACHE.put(processorType, processor);
        }

        return processor;
    }

}

