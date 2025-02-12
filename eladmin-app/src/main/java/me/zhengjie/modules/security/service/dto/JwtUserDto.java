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
package me.zhengjie.modules.security.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.zhengjie.modules.system.service.dto.MemberDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Getter
@AllArgsConstructor
public class JwtUserDto implements UserDetails {

    private final MemberDto user;

    private final List<Long> dataScopes;

    private final List<AuthorityDto> authorities;

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return user.getNickName();
    }

    @Override
    @JSONField(serialize = false)
    public String getUsername() {
        return user.getNickName().concat("-").concat(user.getOpenId());
    }

    public String getOpenId(){
        return user.getOpenId();
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
