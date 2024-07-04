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
import java.sql.Timestamp;
import java.util.List;
import me.zhengjie.annotation.Query;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-06-19
**/
@Data
public class MemberQueryCriteria{

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String openId;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String nickName;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String phone;

    /** 模糊 */
    @Query(type = Query.Type.EQUAL)
    private Boolean type;

    /** 精确 */
    @Query
    private String enabled;
    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> vipExpiration;

    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;

    /** 创建者 */
    @Query(type = Query.Type.EQUAL)
    private String createBy;

    /** 排除创建者 */
    @Query(type = Query.Type.NOT_EQUAL,propName = "createBy")
    private String notCreateBy;
}
