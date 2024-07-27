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
package me.zhengjie.repository;

import me.zhengjie.domain.SysLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Repository
public interface LogRepository extends JpaRepository<SysLog,Long>, JpaSpecificationExecutor<SysLog> {

    /**
     * 根据日志类型删除信息
     * @param logType 日志类型
     */
    @Modifying
    @Query(value = "delete from sys_log where log_type = ?1", nativeQuery = true)
    void deleteByLogType(String logType);

    @Query(value = "select count(log_id) from sys_log where description = 'APP用户登录'", nativeQuery = true)
    long countLogin();

    @Query(value = "select count(log_id) from sys_log where description = 'APP用户登录' and exception_detail is not null", nativeQuery = true)
    long countLoginFail();

    @Query(value = "select count(log_id) from sys_log where description = 'APP用户登录' and exception_detail is null", nativeQuery = true)
    long countLoginSuccess();

    @Query(value = "select count(log_id) from sys_log where description = 'APP用户登录' and create_time between date(now()) and now()", nativeQuery = true)
    long countLoginToday();

    @Query(value = "select count(log_id) from sys_log where description = 'APP用户登录' and create_time >= date(now()) and exception_detail is not null", nativeQuery = true)
    long countLoginTodayFail();

    @Query(value = "select count(log_id) from sys_log where description = 'APP用户登录' and create_time >= date(now()) and exception_detail is null", nativeQuery = true)
    long countLoginTodaySuccess();

    @Query(value = "select count(log_id) from sys_log where description = 'APP用户登录' and create_time between ?1 and ?2", nativeQuery = true)
    long countLoginOfCycle(Timestamp begin, Timestamp end);

    @Query(value = "select count(log_id) from sys_log where description = 'APP用户登录' and create_time between ?1 and ?2 and exception_detail is null", nativeQuery = true)
    long countLoginOfCycleSuccess(Timestamp begin, Timestamp end);

    @Query(value = "select count(log_id) from sys_log where description = 'APP用户登录' and create_time between ?1 and ?2 and exception_detail is not null", nativeQuery = true)
    long countLoginOfCycleFail(Timestamp begin, Timestamp end);
}
