package com.garm.modules.web.dto.owntest.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.modules.exam.dto.library.LibraryDetailDTO;
import com.garm.modules.exam.dto.library.LibraryPaperListDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 自测数据
 */
@ApiModel("新增自测返回数据")
public class OwnTestDetailRespDTO {

    /**
     * 自测试卷ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("自测试卷ID")
    private Long ownTestId;

    @ApiModelProperty("自测试题数据")
    private List<LibraryDetailDTO> datas;


    @ApiModelProperty("考试已用时间，单位：秒")
    @NotNull(message = "考试已用时间不能为空")
    private Long stateTime;

    /**
     * 是否考试结束
     */
    @NotNull(message = "考试结束标识不能为空")
    @ApiModelProperty("是否考试结束;1,已结束,2,未结束")
    private Integer isEnd;

    /**
     * 考试倒计时,单位：秒
     */
    @ApiModelProperty("考试倒计时,单位：秒")
    private Long countdown;


    public Long getOwnTestId() {
        return ownTestId;
    }

    public void setOwnTestId(Long ownTestId) {
        this.ownTestId = ownTestId;
    }

    public List<LibraryDetailDTO> getDatas() {
        return datas;
    }

    public void setDatas(List<LibraryDetailDTO> datas) {
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

    public Long getCountdown() {
        return countdown;
    }

    public void setCountdown(Long countdown) {
        this.countdown = countdown;
    }
}
