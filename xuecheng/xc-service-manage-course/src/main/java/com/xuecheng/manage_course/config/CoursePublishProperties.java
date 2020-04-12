package com.xuecheng.manage_course.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ToString
@NoArgsConstructor
@ConfigurationProperties(prefix = "course-publish")
@Configuration
public class CoursePublishProperties {

    private String siteId;
    private String pageWebPath;
    private String pagePhysicalPath;
    private String pageType;
    private String templateId;
    private String dataUrl;
    private String previewUrl;


}
