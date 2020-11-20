

package com.garm.modules.web.controller;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

/**
 * 前台测试接口
 *
 * @author
 */
@RestController
@RequestMapping("/api")
@Api(tags = "前台-测试接口")
public class AppTestController {

//    @Login
//    @GetMapping("userInfo")
//    @ApiOperation("获取用户信息")
//    public Result<Map> userInfo(@LoginUser UserEntity user){
//        Map<String, Object> map = new HashMap<>();
//        map.put("user", user);
//        return Result.ok(map);
//    }
//
//    @Login
//    @GetMapping("userId")
//    @ApiOperation("获取用户ID")
//    public Result<Map> userInfo(@RequestAttribute("userId") Integer userId){
//        Map<String, Object> map = new HashMap<>();
//        map.put("userId", userId);
//        return Result.ok(map);
//    }
//
//    @GetMapping("notToken")
//    @ApiOperation("忽略Token验证测试")
//    public Result<Map> notToken(){
//        return Result.ok("无需token也能访问。。。");
//    }

}
