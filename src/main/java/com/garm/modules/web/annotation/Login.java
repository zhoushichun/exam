

package com.garm.modules.web.annotation;

import java.lang.annotation.*;

/**
 * 前台登录效验
 *
 * @author
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
}
