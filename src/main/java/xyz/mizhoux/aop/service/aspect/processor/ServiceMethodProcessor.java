package xyz.mizhoux.aop.service.aspect.processor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import xyz.mizhoux.aop.aspect.processor.impl.AbstractMethodAspectProcessor;
import xyz.mizhoux.aop.domain.CommonResponse;
import xyz.mizhoux.aop.domain.base.BaseRequest;
import xyz.mizhoux.aop.domain.base.BaseResponse;

import java.util.UUID;

/**
 * 业务方法切面处理器
 *
 * @author 之叶
 * @date   2019/09/01
 */
@Component
public class ServiceMethodProcessor extends AbstractMethodAspectProcessor<BaseResponse> {

    /**
     * 是否是要处理的方法<br/>
     * 限定方法类型入参匹配 BaseRequest，返回值匹配 BaseResponse
     *
     * @param point 目标方法的连接点
     * @return 如果是要处理的方法返回 true，否则返回 false
     */
    @Override
    public boolean isMatched(ProceedingJoinPoint point) {
        MethodSignature signature  = (MethodSignature) point.getSignature();
        Class           returnType = signature.getReturnType();

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
     * 获得抛出异常时的返回值<br/>
     *
     * @param point 目标方法的连接点
     * @param e     抛出的异常
     * @return 抛出异常时的返回值
     */
    @Override
    @SuppressWarnings("unchecked")
    public BaseResponse getOnThrow(ProceedingJoinPoint point, Throwable e) {
        // 获得返回类型
        Class<? extends BaseResponse> returnType = getReturnType(point);
        // 构造抛出异常时的返回值
        BaseResponse response = newInstance(returnType);

        response.setPrompt(e.getMessage());
        response.setSuccess(false);

        return response;
    }

    /**
     * 切面完成时，执行的动作
     *
     * @param point     目标方法的连接点
     * @param startTime 执行的开始时间
     * @param result    执行获得的结果
     */
    @Override
    public void onComplete(ProceedingJoinPoint point, long startTime, boolean forbidden, boolean thrown, BaseResponse result) {
        // 设置方法调用的时间
        result.setSysTime(startTime);
        // 设置方法调用的机器
        result.setHost(getHost());
        // 设置方法调用耗时
        result.setCostTime(System.currentTimeMillis() - startTime);

        Logger logger = getLogger(point);
        // point.getArgs() 获得方法调用入参
        Object request = point.getArgs()[0];
        // 记录方法调用信息
        logger.info("{}, request={}, response={}", getLogTag(point), request, result);
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
