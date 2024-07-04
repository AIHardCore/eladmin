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
package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.Member;
import me.zhengjie.modules.system.service.dto.MemberDto;
import me.zhengjie.modules.system.service.dto.MemberQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;

/**
* @website https://eladmin.vip
* @description 服务接口
* @author hardcore
* @date 2024-06-19
**/
public interface MemberService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<MemberDto> queryAll(MemberQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<MemberDto>
    */
    List<MemberDto> queryAll(MemberQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param openId ID
     * @return MemberDto
     */
    MemberDto findByOpenId(String openId);

    /**
     * 根据ID查询
     * @param nickName
     * @return MemberDto
     */
    MemberDto findByNickName(String nickName);

    /**
    * 创建
    * @param resources /
    */
    void create(Member resources);

    /**
     * 微信授权
     * @param code
     */
    Member login(String code);

    /**
     * 修改手机号
     * @param phone
     */
    void updatePhone(String phone);
}
