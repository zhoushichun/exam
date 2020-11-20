

package com.garm.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.http.HttpStatus;

import java.io.Serializable;
import java.util.Map;

/**
 * 返回数据
 *
 * @author
 */
@Data
@ApiModel(value = "返回数据")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T>  {

	public final static Integer SUCESS_CODE = 0;

	public final static String MSG_SUCCESS = "sucess";

	public final static String MSG_FAIl = "fail";

	@ApiModelProperty(value = "状态值 0-sucess  ")
	private Integer code;
	@ApiModelProperty(value = "返回信息")
	private String msg;

	@ApiModelProperty(value = "请求返回信息")
	private T data;

	public Result(Integer code, String message, T data) {
		this.code = code;
		this.msg = message;
		this.data = data;
	}

	public static Result successPage(PageUtils page){
		return new Result(SUCESS_CODE,MSG_SUCCESS,page);
	}



	public static Result error() {

		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}

	public static Result error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}

	public static Result error(int code, String msg) {
		return new Result(code,msg,null);
	}

	public static Result ok(String msg) {
		return new Result(SUCESS_CODE,msg,null);
	}

	public static Result ok(Map<String, Object> map) {
		return new Result(SUCESS_CODE,MSG_SUCCESS,map);
	}

	public static Result ok() {
		return new Result(SUCESS_CODE,MSG_SUCCESS,null);
	}

	public static Result ok(Object data) {
		Result result = new Result(SUCESS_CODE,MSG_SUCCESS,data);
		return result;
	}
}
