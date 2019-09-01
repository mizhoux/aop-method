package org.mizhou.aop.aspect.processor.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.mizhou.aop.aspect.anno.Sharable;

/**
 * 方法不匹配时的方法切面处理器<br/>
 * isMatched 方法返回 false，即不会对任何方法做处理<br/>
 * 方法执行之前，会调用 onMismatch 方法，该方法在 AbstractMethodAspectProcessor 提供默认实现
 *
 * @author 之叶
 * @date   2019/08/28
 */
@Sharable
public class MismatchMethodAspectProcessor<R> extends AbstractMethodAspectProcessor<R> {

    @Override
    public boolean isMatched(ProceedingJoinPoint point) {
        return false;
    }

    @Override
    public R returnWhenThrowing(ProceedingJoinPoint point, Throwable e) {
        // 不会被调用
        return null;
    }

    @Override
    public void onComplete(ProceedingJoinPoint point, long startTime, R result) {
        // 不会被调用
    }
}
