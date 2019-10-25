package xyz.mizhoux.aop.service.impl;

import xyz.mizhoux.aop.aspect.anno.MethodAspectAnno;
import xyz.mizhoux.aop.service.NumberService;
import xyz.mizhoux.aop.service.aspect.processor.ServiceMethodProcessor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * NumberServiceImpl
 *
 * @author 之叶
 * @date   2019/09/01
 */
@Service
public class NumberServiceImpl implements NumberService {

    @Override
    @MethodAspectAnno(ServiceMethodProcessor.class)
    public int divide(int dividend, int divisor) throws Exception {
        // 模拟检查业务参数
        // ...检查业务参数...
        TimeUnit.MILLISECONDS.sleep(300);

        // 模拟执行业务
        int result = dividend / divisor;

        return result;
    }

}
