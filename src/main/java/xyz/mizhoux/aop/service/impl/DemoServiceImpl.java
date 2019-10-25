package xyz.mizhoux.aop.service.impl;

import xyz.mizhoux.aop.aspect.anno.MethodAspectAnno;
import xyz.mizhoux.aop.domain.DivisionRequest;
import xyz.mizhoux.aop.domain.DivisionResponse;
import xyz.mizhoux.aop.service.DemoService;
import xyz.mizhoux.aop.service.aspect.processor.ServiceMethodProcessor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * DemoServiceImpl
 *
 * @author 之叶
 * @date   2019/09/01
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Override
    @MethodAspectAnno(ServiceMethodProcessor.class)
    public DivisionResponse divide(DivisionRequest request) throws Exception {
        DivisionResponse response = new DivisionResponse();

        // 请求参数
        int dividend = request.getDividend();
        int divisor = request.getDivisor();

        // 模拟检查业务参数
        // ...检查业务参数...
        TimeUnit.MILLISECONDS.sleep(300);

        // 模拟执行业务
        int result = dividend / divisor;

        // 设置业务执行结果
        response.setData(result);

        return response;
    }

}