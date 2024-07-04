/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.system.domain;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.Set;

/**
 * @website https://eladmin.vip
 * @description /
 * @author hardcore
 * @date 2024-06-29
 **/
@Entity
@Getter
@Setter
@NamedEntityGraph(
        name = "Article.withSpecials",
        attributeNodes = {
                @NamedAttributeNode("specials")
        },
        subgraphs = {@NamedSubgraph(
                name = "special-article",
                attributeNodes = {
                        @NamedAttributeNode(value = "Special.withArticles")
                }
        )}
)
@Table(name="app_article")
public class Article extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @ApiModelProperty(value = "文章专栏")
    @JoinTable(name = "app_articles_specials",
            joinColumns = {@JoinColumn(name = "article_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "special_id",referencedColumnName = "id")})
    private Set<Special> specials;

    @Column(name = "`title`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "标题")
    private String title;

    @Column(name = "`cover`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "封面")
    private String cover;

    @Column(name = "`preview`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "预览内容")
    private String preview;

    @Transient
    @ApiModelProperty(value = "内容")
    private String body = "";

    @Version
    @Column(name = "`version`")
    @ApiModelProperty(value = "乐观锁")
    private int version;

    @Column(name = "`enabled`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "状态")
    private Boolean enabled;

    @Column(name = "`sort`")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @Column(name = "`reading`")
    @ApiModelProperty(value = "阅读量")
    private Integer reading;

    public void copy(Article source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
