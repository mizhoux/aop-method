package org.mizhou.aop.service;

import org.mizhou.aop.domain.DivisionRequest;
import org.mizhou.aop.domain.DivisionResponse;

/**
 * DemoService
 *
 * @author 之叶
 * @date   2019/09/01
 */
public interface DemoService {

    /**
     * 除法运算
     *
     * @param request 除法运算请求
     * @return 除法运算结果
     * @throws Exception 可能产生的异常（切面会捕获）
     */
    DivisionResponse divide(DivisionRequest request) throws Exception;

}
