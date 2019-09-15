package xyz.mizhoux.aop.controller;

import xyz.mizhoux.aop.domain.DivisionRequest;
import xyz.mizhoux.aop.domain.DivisionResponse;
import xyz.mizhoux.aop.service.NumberService;
import xyz.mizhoux.aop.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * DemoController
 *
 * @author 之叶
 * @date   2019/09/01
 */
@RestController
public class DemoController {

    @Resource
    private DemoService demoService;

    @Resource
    private NumberService numberService;

    @GetMapping("division.do")
    public DivisionResponse doDivision(@RequestParam int a,
                                       @RequestParam int b) throws Exception {
        // 构建请求
        DivisionRequest request = new DivisionRequest();
        request.setDividend(a);
        request.setDivisor(b);

        // 执行
        return demoService.divide(request);
    }

    @GetMapping("another.do")
    public Integer doAnotherDivision(@RequestParam int a,
                                     @RequestParam int b) throws Exception {
        return numberService.divide(a, b);
    }

}
