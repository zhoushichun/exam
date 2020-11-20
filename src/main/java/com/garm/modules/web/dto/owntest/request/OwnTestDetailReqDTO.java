package com.garm.modules.web.dto.owntest.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 自测数据
 */
@ApiModel(value= "新增参数数据")
public class OwnTestDetailReqDTO {

    /**
     * 自测试卷ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value= "自测试卷ID")
    private Long ownTestId;

    @ApiModelProperty(value= "自测试题数据")
    private List<Library> datas;


    @ApiModelProperty(value= "考试已用时间，单位：秒")
    @NotNull(message = "考试已用时间不能为空")
    private Long stateTime;

    /**
     * 是否考试结束
     */
    @NotNull(message = "考试结束标识不能为空")
    @ApiModelProperty(value= "是否考试结束;1,已结束,2,未结束")
    private Integer isEnd;


    public Long getOwnTestId() {
        return ownTestId;
    }

    public void setOwnTestId(Long ownTestId) {
        this.ownTestId = ownTestId;
    }

    public List<Library> getDatas() {
        return datas;
    }

    public void setDatas(List<Library> datas) {
        this.datas = datas;
    }

    public Long getStateTime() {
        return stateTime;
    }

    public void setStateTime(Long stateTime) {
        this.stateTime = stateTime;
    }

    public Integer getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(Integer isEnd) {
        this.isEnd = isEnd;
    }

}
