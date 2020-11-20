package com.garm.modules.exam.dto.test.official;

import com.garm.modules.exam.dto.paper.PaperDTO;
import com.garm.modules.exam.dto.paper.PaperDetailDTO;
import com.garm.modules.exam.dto.paper.PaperParagraphDTO;
import com.garm.modules.exam.entity.OfficialTestPaperEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author ldx
 * @Date 2020/4/27 16:59
 * @Description
 * @Version 1.0.0
 */
@Data
public class ExamOfficialTestPaperDTO implements Serializable {

    @ApiModelProperty("考试试卷关联信息")
    private OfficialTestPaperEntity officialTestPaperEntity;

    @ApiModelProperty("总分")
    private BigDecimal totalScore;

    @ApiModelProperty("试卷Id")
    private Long paperId;

    @ApiModelProperty("试卷数据")
    private PaperDetailDTO paperDatas;

}
