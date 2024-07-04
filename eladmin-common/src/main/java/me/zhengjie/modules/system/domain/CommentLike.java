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
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import me.zhengjie.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2024-07-17
**/
@Entity
@Data
@Table(name="app_comment_like")
public class CommentLike extends BaseEntity implements Serializable {
    @EmbeddedId
    private CompositeKey key;

    @Getter
    @Setter
    @Embeddable
    @NoArgsConstructor
    public static class CompositeKey implements Serializable {
        @Column(name = "`id`",nullable = false)
        @NotNull
        @ApiModelProperty(value = "留言id")
        private Long id;

        @Column(name = "`to`",nullable = false)
        @NotBlank
        @ApiModelProperty(value = "发表用户的openId")
        private String to;

        @Column(name = "`from`",nullable = false)
        @NotBlank
        @ApiModelProperty(value = "点赞用户的openId")
        private String from;

        public CompositeKey(@NotNull Long id, @NotBlank String from, @NotBlank String to) {
            this.id = id;
            this.from = from;
            this.to = to;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id,to,from);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            CompositeKey that = (CompositeKey) obj;
            return Objects.equals(id, that.id) &&
                    Objects.equals(to, that.to) &&
                    Objects.equals(from, that.from);
        }
    }

    public void copy(CommentLike source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
