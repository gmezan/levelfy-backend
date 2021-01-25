package com.uc.backend.security;

import com.uc.backend.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class PrincipalUserFactory {

    public static PrincipalUser build(User user) {
        List<GrantedAuthority> authorities =
                user.getRole().stream().map(
                        rol -> new SimpleGrantedAuthority(rol.getName().name())
                ).collect(Collectors.toList());

        return new PrincipalUser(user.getEmail(), user.getPassword(), authorities);
    }

}
