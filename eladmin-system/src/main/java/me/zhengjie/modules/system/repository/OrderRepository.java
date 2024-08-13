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

import me.zhengjie.modules.system.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-06-30
**/
public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
    @Query(value = "select ifnull(sum(amount),0) / 100 from app_order where status = 0",nativeQuery = true)
    long sum();

    @Query(value = "select ifnull(sum(amount),0) / 100 from app_order where status = 0 and create_time >= now()",nativeQuery = true)
    long sumToday();

    @Query(value = "select ifnull(sum(amount),0) / 100 from app_order where status = 0 and create_time >= date_format(now(),'%Y-%m-01')",nativeQuery = true)
    long sumMonth();
}
