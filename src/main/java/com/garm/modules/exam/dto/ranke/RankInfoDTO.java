package com.garm.modules.exam.dto.ranke;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Author liwt
 * @Date 2020/5/11 14:38
 * @Description
 * @Version 1.0.0
 */
@Data
@ApiModel("活跃排行榜数据")
public class RankInfoDTO implements Serializable {

    @ApiModelProperty("活跃TOP30")
    private List<RankingDTO> testRanking;

    @ApiModelProperty("总分Top10")
    private List<RankingDTO> socreRanking;


}
