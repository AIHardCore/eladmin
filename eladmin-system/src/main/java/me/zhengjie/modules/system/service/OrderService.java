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

import me.zhengjie.modules.system.domain.Order;
import me.zhengjie.modules.system.service.dto.OrderDto;
import me.zhengjie.modules.system.service.dto.OrderQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @website https://eladmin.vip
* @description 服务接口
* @author hardcore
* @date 2024-06-30
**/
public interface OrderService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<OrderDto> queryAll(OrderQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<OrderDto>
    */
    List<OrderDto> queryAll(OrderQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return OrderDto
     */
    OrderDto findById(String id);

    /**
    * 创建
    * @param resources /
    */
    void create(Order resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(Order resources);

    /**
     * 订单本月每日统计记录
     * @param status 订单状态
     * @return
     */
    List<Map<String,Object>> logsOfDay(int status);

    /**
     * 订单上月每日统计记录
     * @param status 订单状态
     * @return
     */
    List<Map<String,Object>> logsOfPreMonthDay(int status);

    /**
     * 订单周期每小时统计记录
     * @param status 订单状态
     * @return
     */
    List<Map<String,Object>> logsOfHour(int status);

    /**
     * 订单周期每月统计记录
     * @param status 订单状态
     * @return
     */
    List<Map<String,Object>> logsOfMonth(int status);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(String[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<OrderDto> all, HttpServletResponse response) throws IOException;
}
