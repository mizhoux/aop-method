package org.mizhou.aop.service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.mizhou.aop.aspect.anno.Sharable;
import org.mizhou.aop.aspect.processor.impl.AbstractMethodAspectProcessor;
import org.mizhou.aop.domain.CommonResponse;
import org.mizhou.aop.domain.base.BaseRequest;
import org.mizhou.aop.domain.base.BaseResponse;
import org.slf4j.Logger;

import java.util.UUID;

/**
 * DemoServiceMethodAspectProcessor
 *
 * @author 之叶
 * @date   2019/09/01
 */
@Sharable
public class DemoServiceMethodAspectProcessor extends AbstractMethodAspectProcessor<BaseResponse> {

    /**
     * 是否是要处理的方法<br/>
     * 限定方法类型入参匹配 BaseRequest，返回值匹配 BaseResponse
     *
     * @param point 方法的连接点
     * @return 是要处理的方法返回 true，否则返回 false
     */
    @Override
    public boolean isMatched(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class returnType = signature.getReturnType();

        // returnType 是 BaseResponse 或其子类型
        if (BaseResponse.class.isAssignableFrom(returnType)) {
            Class[] parameterTypes = signature.getParameterTypes();

            // 参数必须是 BaseRequest 或其子类型
            return parameterTypes.length == 1
                    && BaseRequest.class.isAssignableFrom(parameterTypes[0]);
        }

        return false;
    }

    /**
     * 正常返回时，执行的动作
     *
     * @param point 方法的连接点
     * @param result 方法返回的结果
     */
    @Override
    public void onReturn(ProceedingJoinPoint point, BaseResponse result) {
        String logTag = getLogTag(point);
        Logger logger = getLogger(point);

        result.setSuccess(true);
        logger.info("{} 正常调用", logTag);
    }

    /**
     * 抛出异常时，执行的动作
     *
     * @param point 方法的连接点
     * @param e 抛出的异常
     */
    @Override
    public void onThrow(ProceedingJoinPoint point, Throwable e) {
        Logger logger = getLogger(point);
        String logTag = getLogTag(point);

        logger.error("{} 调用出错", logTag, e);
    }

    /**
     * 构建抛出异常时的返回值<br/>
     * 不知道起什么名字好，如果大家有好的建议，欢迎留言
     *
     * @param point 方法的连接点
     * @param e 抛出的异常
     * @return 抛出异常时的返回值
     */
    @Override
    @SuppressWarnings("unchecked")
    public BaseResponse returnWhenThrowing(ProceedingJoinPoint point, Throwable e) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<? extends BaseResponse> returnType = signature.getReturnType();

        // 构造抛出异常时的返回值
        BaseResponse response = newInstance(returnType);

        response.setPrompt(e.getMessage());
        response.setSuccess(false);

        return response;
    }

    /**
     * 切面完成时，执行的动作
     *
     * @param point 方法的连接点
     * @param startTime 执行的开始时间
     * @param result 执行获得的结果
     */
    @Override
    public void onComplete(ProceedingJoinPoint point, long startTime, BaseResponse result) {
        BaseResponse response = (BaseResponse) result;

        // 设置方法调用的时间
        response.setSysTime(startTime);
        // 设置方法调用的机器
        response.setHost(getHost());
        // 设置方法调用耗时
        response.setCostTime(System.currentTimeMillis() - startTime);

        Logger logger = getLogger(point);
        // point.getArgs() 获得方法调用入参
        Object request = point.getArgs()[0];
        // 记录方法调用信息
        logger.info("{}, request={}, response={}", getLogTag(point), request, response);
    }

    private BaseResponse newInstance(Class<? extends BaseResponse> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return new CommonResponse();
        }
    }

    /**
     * 模拟获得服务器名称
     */
    private String getHost() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
