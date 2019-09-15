package xyz.mizhoux.aop.service;

/**
 * NumberService
 *
 * @author 之叶
 * @date   2019/09/01
 */
public interface NumberService {

    /**
     * 除法运算
     *
     * @param dividend 被除数
     * @param divisor 除数
     * @return 商
     * @throws Exception 可能产生的异常（切面会捕获）
     */
    int divide(int dividend, int divisor) throws Exception;

}
