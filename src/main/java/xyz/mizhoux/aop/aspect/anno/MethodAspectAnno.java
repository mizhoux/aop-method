package xyz.mizhoux.aop.aspect.anno;

import xyz.mizhoux.aop.aspect.processor.MethodAspectProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于方法切面的注解
 *
 * @author 之叶
 * @date   2019/09/01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodAspectAnno {

    /**
     * 获得方法切面的处理器
     *
     * @return 方法切面的处理器
     */
    Class<? extends MethodAspectProcessor> value();

}
