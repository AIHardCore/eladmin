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
package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.base.BaseDTO;

import java.io.Serializable;
import java.util.Objects;

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2024-06-19
**/
@Data
public class ArticleDto extends BaseDTO implements Serializable {

    private Integer id;

    /** 版块 */
    private SectionDto section;

    /** 标题 */
    private String title;

    /** 封面 */
    private String cover;

    /** 预览内容 */
    private String preview;

    /** 状态 */
    private Boolean enabled;

    /** 排序 */
    private Integer sort;

    /** 阅读量 */
    private Integer reading;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleDto that = (ArticleDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
