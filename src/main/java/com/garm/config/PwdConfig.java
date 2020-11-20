package com.garm.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 	默认密码类
 *
 * @className: PwdConfig
 * @description:TODO
 * @author: jimengkeji
 * @date: 2020年1月2日 下午5:04:29
 */
@Configuration
@Data
public class PwdConfig {

	/**
	 * 	默认密码
	 */
	@Value("${exam.config.default_password}")
	private String defaultPassword;
}
