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

import me.zhengjie.modules.system.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-07-17
**/
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>, JpaSpecificationExecutor<CommentLike> {

    CommentLike findCommentLikeByKey(CommentLike.CompositeKey key);

    @Modifying
    @Query(value = "insert into app_comment_like(id,`from`,`to`,create_by,create_time) values(?1,?2,?3,?4,now()) on duplicate key update update_time = now(),update_by = ?5",nativeQuery = true)
    void like(Long id, String from, String to, String createBy,String updateBy);

    @Modifying
    @Query(value = "delete from app_comment_like where id = ?1 and `from` = ?2 and `to`= ?3",nativeQuery = true)
    void unlike(Long id, String from, String to);
}
