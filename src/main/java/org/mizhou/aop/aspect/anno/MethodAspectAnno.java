package org.mizhou.aop.aspect.anno;

import org.mizhou.aop.aspect.processor.MethodAspectProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MethodAspectAnno
 *
 * @author 之叶
 * @date   2019/09/01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodAspectAnno {

    Class<? extends MethodAspectProcessor> value();

}
