package xyz.mizhoux.aop.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import xyz.mizhoux.aop.domain.base.BaseRequest;

/**
 * 除法请求
 *
 * @author 之叶
 * @date   2019/09/01
 */
@Getter
@Setter
@ToString
public class DivisionRequest extends BaseRequest {

    private static final long serialVersionUID = 259902192604927L;

    /**
     * 被除数
     */
    private int dividend;

    /**
     * 除数
     */
    private int divisor;
}
