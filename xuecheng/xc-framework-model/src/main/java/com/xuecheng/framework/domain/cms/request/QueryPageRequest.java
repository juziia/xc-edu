package com.xuecheng.framework.domain.cms.request;

import com.xuecheng.framework.model.request.RequestData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 *  用于封装查询请求参数
 */
@Data
public class QueryPageRequest extends RequestData {
    //站点id
     @ApiModelProperty(value = "站点id")
     private String siteId;
     //页面ID
     @ApiModelProperty(value = "页面id")
     private String pageId;
     //页面名称     
     @ApiModelProperty(value = "页面名称")
     private String pageName;
     //别名     
     @ApiModelProperty(value = "别名")
     private String pageAliase;
     //模版id     
     @ApiModelProperty(value = "模板id")
     private String templateId;

}
