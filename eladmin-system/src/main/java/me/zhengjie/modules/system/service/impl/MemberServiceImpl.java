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

import me.zhengjie.modules.system.domain.Member;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.MemberRepository;
import me.zhengjie.modules.system.service.MemberService;
import me.zhengjie.modules.system.service.dto.MemberDto;
import me.zhengjie.modules.system.service.dto.MemberQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.MemberMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import me.zhengjie.utils.PageResult;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author hardcore
* @date 2024-06-19
**/
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public PageResult<MemberDto> queryAll(MemberQueryCriteria criteria, Pageable pageable){
        Page<Member> page = memberRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(memberMapper::toDto));
    }

    @Override
    public List<MemberDto> queryAll(MemberQueryCriteria criteria){
        return memberMapper.toDto(memberRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public MemberDto findById(Integer id) {
        Member member = memberRepository.findById(id).orElseGet(Member::new);
        ValidationUtil.isNull(member.getId(),"Member","id",id);
        return memberMapper.toDto(member);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Member resources) {
        memberRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Member resources) {
        Member member = memberRepository.findById(resources.getId()).orElseGet(Member::new);
        ValidationUtil.isNull( member.getId(),"Member","id",resources.getId());
        member.copy(resources);
        memberRepository.save(member);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            memberRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<MemberDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MemberDto member : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("appid", member.getAppid());
            map.put("昵称", member.getNickName());
            map.put("头像", member.getAvater());
            map.put("用户类型", member.getType());
            map.put("VIP到期时间", member.getVipExpiration());
            map.put("状态", member.getEnabled());
            map.put("创建时间", member.getCreateTime());
            map.put("更新时间", member.getUpdateTime());
            map.put("更新人", member.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
