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
package me.zhengjie.modules.system.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.system.domain.Comment;
import me.zhengjie.modules.system.service.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
* @website https://eladmin.vip
* @author hardcoer
* @date 2024-07-16
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper extends BaseMapper<CommentDto, Comment> {
    @Mapping(target = "member.openId", ignore = true)
    @Mapping(target = "member.createBy", ignore = true)
    @Mapping(target = "member.updateBy", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    CommentDto toDto(Comment entity);
}
