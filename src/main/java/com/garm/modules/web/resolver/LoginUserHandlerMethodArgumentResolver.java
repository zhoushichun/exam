

package com.garm.modules.web.resolver;

import com.garm.common.utils.JmBeanUtils;
import com.garm.modules.exam.dao.UserTokenDao;
import com.garm.modules.sys.service.UserService;
import com.garm.modules.web.annotation.LoginUser;
import com.garm.modules.web.interceptor.AuthorizationInterceptor;
import com.garm.modules.web.entity.UserEntity;
import com.garm.modules.web.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 有@LoginUser注解的方法参数，注入当前登录用户
 *
 * @author
 */
@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private UserService userService;
    @Autowired
    private UserTokenDao userTokenDao;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserDTO.class) && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        //获取用户ID
        Object object = request.getAttribute(AuthorizationInterceptor.USER_KEY, RequestAttributes.SCOPE_REQUEST);
        if(object == null){
            return null;
        }

        //获取用户信息
        UserEntity user = userService.getById((Long)object);

        UserDTO dto = JmBeanUtils.entityToDto(user,UserDTO.class);
        dto.setLastLoginTime(userTokenDao.selectById(dto.getUserId()).getUpdateTime());
        return dto;
    }
}
