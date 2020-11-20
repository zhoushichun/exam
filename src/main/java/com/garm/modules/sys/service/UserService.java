

package com.garm.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.dto.library.FileUploadFailDTO;
import com.garm.modules.exam.dto.test.official.OfficialTestUserInfoDTO;
import com.garm.modules.exam.dto.user.UserServiceUpdateDTO;
import com.garm.modules.sys.dto.UserServiceDTO;
import com.garm.modules.web.dto.login.LoginDTO;
import com.garm.modules.web.dto.UserCountInfoDTO;
import com.garm.modules.web.entity.UserEntity;
import com.garm.modules.web.model.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**
 * 用户
 *
 * @author
 */
public interface UserService extends IService<UserEntity> {


	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	PageUtils<UserEntity> queryPage(Map<String, Object> params);

	/**
	 * 用户登录
	 * @param dto    登录表单
	 * @return        返回用户ID
	 */
	Result login(LoginDTO dto);

	/**
	 * 获取考试用户信息
	 * @return
	 */
	Result getTestUserInfo(Map<String, Object> params);

	/**
	 * 获取考试用户信息
	 * @return
	 */
	List<OfficialTestUserInfoDTO> getTestUserInfo(List<Long> userId);



	/**
	 * 获取导出数据
	 * @return
	 */
	List<Map<String, Object>> export();

	/**
	 * 批量导入
	 * @param file
	 * @return
	 */
	Result<FileUploadFailDTO> upload(MultipartFile file);

	/**
	 *查询指定用户现拥有的服务
	 */
	Result<UserServiceDTO> queryUserService(Long userId);


	/**
	 * 调整用户服务
	 * @param dto
	 * @return
	 */
	Result updateUserService(UserServiceUpdateDTO dto);

	/**
	 * 获取用户考试统计信息
	 */
	Result<UserCountInfoDTO> getUserTestCount(UserDTO user);

	/**
	 * 校验验证码
	 * @param phone
	 * @param code
	 * @param type
	 * @return
	 */
	boolean checkCode(String phone,String code,String type);

}
