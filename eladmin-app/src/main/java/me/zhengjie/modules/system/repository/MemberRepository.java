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
package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-06-19
**/
public interface MemberRepository extends JpaRepository<Member, String>, JpaSpecificationExecutor<Member> {
    Member findByNickName(String nickName);

    @Modifying
    @Query(value = "update app_member set phone = ?2 where open_id = ?1 ",nativeQuery = true)
    void updatePhone(String openId, String phone);

    Member findByOpenId(String openId);
}
