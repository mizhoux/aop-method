package org.mizhou.aop.service.impl;

import org.mizhou.aop.aspect.anno.MethodAspectAnno;
import org.mizhou.aop.service.NumberService;
import org.mizhou.aop.service.aspect.DemoServiceMethodAspectProcessor;
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
    @MethodAspectAnno(DemoServiceMethodAspectProcessor.class)
    public int divide(int dividend, int divisor) throws Exception {
        // 模拟检查业务参数
        // ...检查业务参数...
        TimeUnit.MILLISECONDS.sleep(300);

        // 模拟执行业务
        int result = dividend / divisor;

        return result;
    }

}
