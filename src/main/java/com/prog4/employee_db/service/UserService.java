package com.prog4.employee_db.service;

import com.prog4.employee_db.entity.Member;
import com.prog4.employee_db.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository memberRepository;
    public Member findById(Long id){
        return memberRepository.getById(id);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member user = this.findMemberByUsername(username);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("ADMIN")
                .build();
    }

    public Member save(Member user){
        return this.findById(memberRepository.save(user).getId());
    }

    public Member findMemberByUsername(String username){
        return memberRepository.findMemberByUsername(username);
    }
}