

package com.garm.common.validator;

import com.garm.common.exception.ResultException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * hibernate-validator校验工具类
 *
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 *
 * @author
 */
public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws ResultException  校验不通过，则报ResultException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws ResultException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            for (ConstraintViolation<Object> violation : constraintViolations) {
                System.out.println(violation.getMessage());
                throw new ResultException(violation.getMessage());
            }
        }
    }
}
