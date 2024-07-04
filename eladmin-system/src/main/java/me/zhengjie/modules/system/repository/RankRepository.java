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

import com.sun.istack.Nullable;
import me.zhengjie.modules.system.domain.Rank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-07-03
**/
public interface RankRepository extends JpaRepository<Rank, Integer>, JpaSpecificationExecutor<Rank> {
    @EntityGraph(value = "Rank.Graph",type = EntityGraph.EntityGraphType.FETCH)
    List<Rank> findAll(@Nullable Specification<Rank> specification);

    @EntityGraph(value = "Rank.Graph",type = EntityGraph.EntityGraphType.FETCH)
    Page<Rank> findAll(@Nullable Specification<Rank> specification, Pageable pageable);
}
