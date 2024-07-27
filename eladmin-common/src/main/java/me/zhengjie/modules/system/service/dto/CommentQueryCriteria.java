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
import me.zhengjie.annotation.Query;

/**
* @website https://eladmin.vip
* @author hardcoer
* @date 2024-07-16
**/
@Data
public class CommentQueryCriteria{
    private Long articleId;

    public String openId;

    @Query(type = Query.Type.INNER_LIKE,joinName = "member",propName = "nickName")
    public String nickName;

    @Query(type = Query.Type.INNER_LIKE)
    public String message;

    public boolean queryReply;

    public void setQueryReply(boolean queryReply) {
        this.queryReply = queryReply;
        this.unreply = queryReply ? null:false;
        this.replied = queryReply ? true:null;
    }

    /**
     * 未回复
     */
    @Query(type = Query.Type.IS_NULL, propName = "reply")
    public Boolean unreply;

    /**
     * 已回复
     */
    @Query(type = Query.Type.NOT_NULL, propName = "reply")
    public Boolean replied;

    public boolean isUnreply() {
        return queryReply ? null:false;
    }

    public boolean isReplied() {
        return queryReply ? true:null;
    }
}
