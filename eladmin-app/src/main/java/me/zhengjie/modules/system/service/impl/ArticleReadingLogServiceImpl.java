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
package me.zhengjie.modules.system.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.domain.ArticleReadingLog;
import me.zhengjie.modules.system.repository.ArticleReadingLogRepository;
import me.zhengjie.modules.system.service.ArticleReadingLogService;
import me.zhengjie.modules.system.service.dto.ArticleReadingLogDto;
import me.zhengjie.modules.system.service.dto.ArticleReadingLogQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.ArticleReadingLogMapper;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author hardcoer
* @date 2024-07-20
**/
@Service
@RequiredArgsConstructor
public class ArticleReadingLogServiceImpl implements ArticleReadingLogService {

    private final ArticleReadingLogRepository articleReadingLogRepository;
    private final ArticleReadingLogMapper articleReadingLogMapper;

    @Override
    public PageResult<ArticleReadingLogDto> queryAll(ArticleReadingLogQueryCriteria criteria, Pageable pageable){
        Page<ArticleReadingLog> page = articleReadingLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(articleReadingLogMapper::toDto));
    }

    @Override
    public List<ArticleReadingLogDto> queryAll(ArticleReadingLogQueryCriteria criteria){
        return articleReadingLogMapper.toDto(articleReadingLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ArticleReadingLogDto findById(Long id) {
        ArticleReadingLog articleReadingLog = articleReadingLogRepository.findById(id).orElseGet(ArticleReadingLog::new);
        ValidationUtil.isNull(articleReadingLog.getId(),"ArticleReadingLog","id",id);
        return articleReadingLogMapper.toDto(articleReadingLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ArticleReadingLog resources) {
        articleReadingLogRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ArticleReadingLog resources) {
        ArticleReadingLog articleReadingLog = articleReadingLogRepository.findById(resources.getId()).orElseGet(ArticleReadingLog::new);
        ValidationUtil.isNull( articleReadingLog.getId(),"ArticleReadingLog","id",resources.getId());
        articleReadingLog.copy(resources);
        articleReadingLogRepository.save(articleReadingLog);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            articleReadingLogRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ArticleReadingLogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ArticleReadingLogDto articleReadingLog : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("文章id", articleReadingLog.getArticle());
            map.put("用户openId", articleReadingLog.getOpenId());
            map.put("类型", articleReadingLog.getType());
            map.put("源ip", articleReadingLog.getIp());
            map.put("创建者", articleReadingLog.getCreateBy());
            map.put("更新者", articleReadingLog.getUpdateBy());
            map.put("创建日期", articleReadingLog.getCreateTime());
            map.put("更新时间", articleReadingLog.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
