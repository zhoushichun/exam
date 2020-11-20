

package com.garm.common.validator;

import com.garm.common.exception.ResultException;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 *
 * @author
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new ResultException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new ResultException(message);
        }
    }
}
