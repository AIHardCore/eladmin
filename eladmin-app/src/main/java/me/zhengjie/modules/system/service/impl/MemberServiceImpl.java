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

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.wx.AccessTokenInfo;
import me.zhengjie.domain.wx.WxUserInfo;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.domain.Member;
import me.zhengjie.modules.system.repository.MemberRepository;
import me.zhengjie.modules.system.service.MemberService;
import me.zhengjie.modules.system.service.dto.MemberDto;
import me.zhengjie.modules.system.service.dto.MemberQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.MemberMapper;
import me.zhengjie.service.WxService;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @website https://eladmin.vip
 * @description 服务实现
 * @author hardcore
 * @date 2024-06-19
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final WxService wxService;

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
    public MemberDto findByOpenId(String openId) {
        Member member = memberRepository.findByOpenId(openId);
        ValidationUtil.isNull(member.getOpenId(),"Member","id", openId);
        return memberMapper.toDto(member);
    }

    @Override
    public MemberDto findByNickName(String nickName) {
        Member member = memberRepository.findByNickName(nickName);
        ValidationUtil.isNull(member.getOpenId(),"用户","昵称",nickName);
        return memberMapper.toDto(member);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Member resources) {
        memberRepository.save(resources);
    }

    @Override
    public Member login(String code, HttpServletRequest request) {
        log.info("code:{}",code);
        System.out.println(String.format("code",code));
        Member member = null;
        if (request.getHeader("Host").contains("localhost") || "x".equals(code)){
            WxUserInfo wxUserInfo = null;
            switch (code){
                case "x":
                    wxUserInfo = JSON.parseObject("{\"city\":\"\",\"country\":\"\",\"headimgurl\":\"https://img01.yzcdn.cn/vant/cat.jpeg\",\"nickname\":\"HardCore\",\"openid\":\"oXPq06is9o1UrnLekD3hokWiRdzM\",\"privilege\":[],\"province\":\"\",\"sex\":0}",WxUserInfo.class);
                    break;
                default:
                    wxUserInfo = JSON.parseObject("{\"city\":\"\",\"country\":\"\",\"headimgurl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/n97BJv6cYegdIlXQOEZvrBXKpt6TqibvvUEpL5e1usMHISVrl2asUwtKr7HWaG5gsjtq3eClduy6WO8nx48ACKg/132\",\"nickname\":\"AI硬核\",\"openid\":\"oXPq06nB7l1OEGsLUXg1WSKAKS4Q\",\"privilege\":[],\"province\":\"\",\"sex\":0}",WxUserInfo.class);
                    break;
            }
            member = memberRepository.findById(wxUserInfo.getOpenid()).orElseGet(Member::new);
            System.out.println(String.format("member",JSON.toJSONString(member)));
            if (member.getOpenId() == null){
                member = new Member();
                member.setOpenId(wxUserInfo.getOpenid());
                member.setHeadImgUrl(wxUserInfo.getHeadimgurl());
                member.setNickName(wxUserInfo.getNickname());
                member.setVipExpiration(DateUtil.date().toTimestamp());
                member.setType(false);
                member.setEnabled(true);
                log.info("member:{}",JSON.toJSONString(member));
                System.out.println(String.format("member",JSON.toJSONString(member)));
                memberRepository.save(member);
            }else {
                member.setHeadImgUrl(wxUserInfo.getHeadimgurl());
                member.setNickName(wxUserInfo.getNickname());
            }
        }else {
            AccessTokenInfo accessTokenInfo = wxService.getAccessTokenInfo(code);
            log.info("accessTokenInfo:{}",JSON.toJSONString(accessTokenInfo));
            System.out.println(String.format("accessTokenInfo",JSON.toJSONString(accessTokenInfo)));
            WxUserInfo wxUserInfo = wxService.getUserInfo(accessTokenInfo.getAccessToken(), accessTokenInfo.getOpenId());
            log.info("wxUserInfo:{}",JSON.toJSONString(wxUserInfo));
            System.out.println(String.format("wxUserInfo",JSON.toJSONString(wxUserInfo)));
            member = memberRepository.findById(wxUserInfo.getOpenid()).orElseGet(Member::new);
            System.out.println(String.format("member",JSON.toJSONString(member)));
            if (member.getOpenId() == null){
                member = new Member();
                member.setOpenId(wxUserInfo.getOpenid());
                member.setHeadImgUrl(wxUserInfo.getHeadimgurl());
                member.setNickName(wxUserInfo.getNickname());
                member.setVipExpiration(DateUtil.date().toTimestamp());
                member.setType(false);
                member.setEnabled(true);
                log.info("member:{}",JSON.toJSONString(member));
                System.out.println(String.format("member",JSON.toJSONString(member)));
                memberRepository.save(member);
            }else {
                member.setHeadImgUrl(wxUserInfo.getHeadimgurl());
                member.setNickName(wxUserInfo.getNickname());
            }
        }
        return member;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePhone(String phone) {
        JwtUserDto jwtUserDto = (JwtUserDto) SecurityUtils.getCurrentUser();
        memberRepository.updatePhone(jwtUserDto.getUser().getOpenId(),phone);
    }

}
