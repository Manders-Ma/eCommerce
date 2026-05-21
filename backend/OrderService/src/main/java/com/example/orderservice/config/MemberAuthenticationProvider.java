package com.example.orderservice.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.orderservice.entity.Member;
import com.example.orderservice.repository.MemberRepository;

@Component
public class MemberAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new BadCredentialsException("輸入的帳號不存在!");
        }

        if (!passwordEncoder.matches(pwd, member.getPassword())) {
            throw new BadCredentialsException("密碼無效!");
        }

        return new UsernamePasswordAuthenticationToken(email, pwd,
                List.of(new SimpleGrantedAuthority(member.getRole())));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
