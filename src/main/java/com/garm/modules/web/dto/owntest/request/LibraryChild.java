package com.garm.modules.web.dto.owntest.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.common.validator.group.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import javax.validation.constraints.NotBlank;


/**
 * 题库公共属性类
 */
//@ApiModel(value = "子题详情")
public class LibraryChild {

    @ApiModelProperty(value= "题库id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long libraryId;

    /**
     * 试题类型，1.单选题 2.多选题 3.判断题 4.填空题
     */
    @ApiModelProperty(value= "试题类型，1.单选题 2.多选题 3.判断题 4.填空题 ")
    @NotBlank(groups = { UpdateGroup.class},message = "请选择试题类型")
    private Integer type;


    @ApiModelProperty(value= "用户答案,当类型为1单选题时，答案为1--A 2--B 3--C 4--D,当类型为2多选题时,1--A 2--B 3--C 4--D,多个答案之间用逗号,连接;当类型为3判断题时，1---正确，2---错误;当类型为4填空题时，多个答案用',@#,',即逗号加上@加上#加上逗号连接")
    private String reply;

    public Long getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Long libraryId) {
        this.libraryId = libraryId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
