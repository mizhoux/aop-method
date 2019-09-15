package xyz.mizhoux.aop.domain;

import lombok.ToString;
import xyz.mizhoux.aop.domain.base.BaseResponse;

/**
 * 除法响应
 *
 * @author 之叶
 * @date   2019/09/01
 */
@ToString(callSuper = true)
public class DivisionResponse extends BaseResponse<Integer> {

    private static final long serialVersionUID = 47500367433423L;

}
