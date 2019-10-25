package xyz.mizhoux.aop.aspect.processor;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 方法切面处理器
 *
 * @param <R> 目标方法返回值的类型
 * @author 之叶
 * @date   2019/08/28
 */
public interface MethodAspectProcessor<R> {

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
     * 目标方法执行之前的动作
     *
     * @param point 方法的连接点
     * @return 返回 true 则表示继续向下执行；返回 false 则表示禁止调用目标方法，
     * 方法切面处理会此时会先调用 getOnForbid 方法获得被禁止执行时的返回值，然后调用 onComplete 方法结束切面
     */
    default boolean onBefore(ProceedingJoinPoint point) {
        return true;
    }

    /**
     * 禁止调用目标方法时（即 onBefore 返回 false），执行该方法获得返回值
     *
     * @param point 方法的连接点
     * @return 禁止调用目标方法时的返回值
     */
    default R getOnForbid(ProceedingJoinPoint point) {
        return null;
    }

    /**
     * 抛出异常时，执行的动作
     *
     * @param point 方法的连接点
     * @param e     抛出的异常
     */
    void onThrow(ProceedingJoinPoint point, Throwable e);

    /**
     * 获得抛出异常时的返回值
     *
     * @param point 方法的连接点
     * @param e     抛出的异常
     * @return 抛出异常时的返回值
     */
    R getOnThrow(ProceedingJoinPoint point, Throwable e);

    /**
     * 切面完成时，执行的动作
     *
     * @param point     方法的连接点
     * @param startTime 执行的开始时间
     * @param forbidden 目标方法是否被禁止执行
     * @param thrown    目标方法执行时是否抛出异常
     * @param result    执行获得的结果
     */
    default void onComplete(ProceedingJoinPoint point, long startTime, boolean forbidden, boolean thrown, R result) {

    }

}

