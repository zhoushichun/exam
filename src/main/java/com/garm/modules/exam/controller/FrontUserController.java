package com.garm.modules.exam.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.common.utils.ExcelUtil;
import com.garm.common.utils.FileUtil;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.common.validator.ValidatorUtils;
import com.garm.modules.exam.constants.DeleteConstant;
import com.garm.modules.exam.constants.DictConstant;
import com.garm.modules.exam.constants.SystemEnableType;
import com.garm.modules.exam.constants.UserType;
import com.garm.modules.exam.dto.ServiceOrderDTO;
import com.garm.modules.exam.dto.library.FileUploadFailDTO;
import com.garm.modules.exam.dto.test.official.OfficialTestUserInfoDTO;
import com.garm.modules.exam.dto.user.UserServiceUpdateDTO;
import com.garm.modules.exam.entity.LibraryEntity;
import com.garm.modules.exam.enums.ModelFileType;
import com.garm.modules.exam.service.ServiceOrderService;
import com.garm.modules.exam.service.SysUserServiceService;
import com.garm.modules.sys.controller.AbstractController;
import com.garm.modules.sys.dto.UserServiceDTO;
import com.garm.modules.sys.entity.DictItemEntity;
import com.garm.modules.sys.service.UserService;
import com.garm.modules.web.entity.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 前台用户管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-08 10:42:37
 */
@RestController
@Api(tags = "后台-用户管理")
@RequestMapping("front/user")
public class FrontUserController extends AbstractController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileUtil fileUtil;

    @GetMapping("/list")
    @ApiOperation("列表")
    @RequiresPermissions("front:user:list")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="nickname",value="用户名"),
            @ApiImplicitParam(name="username",value="用户账号"),
            @ApiImplicitParam(name="userType",value="用户类型")
    })
    public Result<PageUtils<OfficialTestUserInfoDTO>> list(@RequestParam @ApiIgnore Map<String, Object> params){
        Result result =  userService.getTestUserInfo(params);
        return result;
    }

    /**
     * 逻辑删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    @RequiresPermissions("front:user:delete")
    public Result delete(@RequestBody Long[] userIds){
        UserEntity entity = new UserEntity();
        entity.setIsDel(DeleteConstant.DELETE);
        userService.update(entity, Wrappers.<UserEntity>lambdaQuery().in(UserEntity::getUserId, Arrays.asList(userIds)));
        return Result.ok();
    }
    /**
     * 导出
     * @return
     */
    @ApiOperation("导出")
    @GetMapping("export")
//    @RequiresPermissions("front:user:export")
    public void export( HttpServletResponse response){
        String fileName = "用户列表";
        String[] header = {"用户名#nickname","用户账号#username","用户类型#serviceType","服务信息#services"};
        List<Map<String, Object>> data = userService.export();
        fileUtil.handleExcel(fileName,header,data,response);
    }

    /**
     * 模板文件下载
     * @return
     */
    @ApiOperation("模板文件下载")
    @GetMapping("model/download")
    public void downloadModel(HttpServletRequest request, HttpServletResponse response){
        fileUtil.downloadModel(ModelFileType.USER_MODEL.getDesc(),request,response);
    }


    @ApiOperation("批量导入")
    @PostMapping("upload")
//    @RequiresPermissions("front:user:upload")
    public Result<FileUploadFailDTO> uploadFile(@RequestParam("file") MultipartFile file){
        return userService.upload(file);
    }

    /**
     * 查询指定用户现有服务列表
     */
    @GetMapping("/service/{userId}")
    @ApiOperation("查询指定用户现有服务列表")
    public Result<UserServiceDTO> service(@PathVariable Long userId){
        Result result = userService.queryUserService(userId);
        return result;
    }


    /**
     * 调整用户的服务，只能单个调整
     * @param dto
     * @return
     */
    @PostMapping("service/user/update")
    @ApiOperation("调整用户的服务，只能单个调整")
    @RequiresPermissions("front:user:service")
    public Result updateUserService(@RequestBody UserServiceUpdateDTO dto){
        ValidatorUtils.validateEntity(dto);
        return userService.updateUserService(dto);
    }
}
