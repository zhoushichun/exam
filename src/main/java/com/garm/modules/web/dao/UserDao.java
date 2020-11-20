

package com.garm.modules.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garm.modules.exam.dto.test.official.OfficialTestUserInfoDTO;
import com.garm.modules.web.dto.UserCountInfoDTO;
import com.garm.modules.web.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户
 *
 * @author
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {


    IPage<OfficialTestUserInfoDTO> listByServiceByServiceType(@Param("page") Page page,
                                                              @Param("serviceType") String serviceType,
                                                              @Param("username")String username,
                                                              @Param("userIds") String[] userIds,
                                                              @Param("nickname")String nickname,
                                                              @Param("userType")String userType,
                                                              @Param("userId")List<Long> userId);

    List<OfficialTestUserInfoDTO> listByServiceByServiceType(@Param("serviceType") String serviceType,
                                                             @Param("username")String username,
                                                             @Param("userIds") String[] userIds,
                                                             @Param("nickname")String nickname,
                                                             @Param("userType")String userType,
                                                             @Param("userId")List<Long> userId);

    /**
     * 根据用户id查询用户正在进行的服务
     * @param userId
     * @return
     */
    Map<String,String> queryServicePayingStrByUserId(Long userId);

    /**
     * 根据用户id查询用户已完成的服务
     * @param userId
     * @return
     */
    Map<String,String> queryServiceOverStrByUserId(Long userId);

    /**
     * 统计用户登录信息
     * @param userId
     * @return
     */
    UserCountInfoDTO selcetTestTotal(String userId);
}
