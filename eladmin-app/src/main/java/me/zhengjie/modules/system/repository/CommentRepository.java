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

import me.zhengjie.modules.system.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
* @website https://eladmin.vip
* @author hardcoer
* @date 2024-07-16
**/
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    /**
     * 阅读量+1
     * @param id
     */
    @Modifying
    @Query(value = "update app_comment set `likes` = `likes` + 1 where id = ?1 and version = ?2",nativeQuery = true)
    void like(Long id, int version);

    /**
     * 阅读量-1
     * @param id
     */
    @Modifying
    @Query(value = "update app_comment set `likes` = `likes` - 1 where id = ?1 and version = ?2",nativeQuery = true)
    void unlike(Long id, int version);
}
