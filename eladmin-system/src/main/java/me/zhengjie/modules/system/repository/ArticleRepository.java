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

import me.zhengjie.modules.system.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-06-29
**/
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    Page<Article> findAll(Specification<Article> specification, Pageable pageable);

    @Query(value = "select * from app_article a where NOT EXISTS(select * from app_articles_specials s where a.id = s.article_id and special_id = ?1)", nativeQuery = true)
    List<Article> queryAllUnSelectedWithSpecial(Long specialId);

    @Query(value = "select * from app_article a where NOT EXISTS(select * from app_rank s where a.id = s.article and s.type = ?1)", nativeQuery = true)
    List<Article> queryAllUnSelectedWithRank(int type);
}
