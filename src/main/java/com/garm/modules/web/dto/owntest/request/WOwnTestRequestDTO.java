package com.garm.modules.web.dto.owntest.request;

import com.garm.modules.exam.dto.library.LibraryDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author liwt
 * @Date 2020/4/28 11:47
 * @Description
 * @Version 1.0.0
 */
@Data
@ApiModel("生成自测数据请求参数")
public class WOwnTestRequestDTO implements Serializable {

    /**
     * 自测ID
     */
    @ApiModelProperty("自测ID")
    private Long ownTestId;

    /**
     * 自测名称
     */
    @NotBlank(message = "请输入自测名称")
    @ApiModelProperty("自测名称")
    private String ownTestName;

    /**
     * 自测时间(分钟)
     */
    @NotNull(message = "请输入自测时间")
    @ApiModelProperty("自测时间(分钟)")
    private Integer time;

    /**
     * 服务类型id
     */
    @NotNull(message = "请选择服务类型")
    @ApiModelProperty("服务类型id")
    private Long serviceType;

    /**
     * 服务类型
     */
    @ApiModelProperty("服务类型")
    private String serviceTypeName;


    /**
     * 试题类型，1.单选题 2.多选题 3.判断题 4.填空题 5.阅读理解
     */
    @NotNull(message = "请选择试题类型")
    @ApiModelProperty("试题类型，1.单选题 2.多选题 3.判断题 4.填空题 5.阅读理解")
    private Integer type;

    /**
     * 难度类型id，来自码表
     */
    @NotNull(message = "请选择难度类型")
    @ApiModelProperty("难度类型id")
    private Integer difficultyType;

    /**
     * 自测题数量
     */
    @NotNull(message = "请选择自测题数量")
    @ApiModelProperty("自测题数量")
    private Integer ownNum;

    /**
     * 题眼类型id，来自码表
     */
    @NotNull(message = "请选择题眼类型")
    @ApiModelProperty("题眼类型id")
    private Long eyeType;

    /**
     * 题眼类型
     */
    @ApiModelProperty("题眼类型")
    private String eyeTypeName;

    /**
     * 试题数据(当已通过预览按钮生成数据后)
     */
    @ApiModelProperty("试题数据(当已通过预览按钮生成数据后)")
    private List<LibraryDetailDTO> datas;

}
