package me.zhengjie.modules.system.service.dto;

public interface ArticleProjection {
    /** 标题 */
    String getTitle();

    /** 封面 */
    String getCover();

    Integer getReading();
}
