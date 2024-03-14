package com.manders.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manders.ecommerce.dao.MemberRepository;
import com.manders.ecommerce.entity.Member;

@RestController
@RequestMapping("/member")
public class LoginController {
  
  @Autowired
  private MemberRepository memberRepository;
  
  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @PostMapping("/register")
  public ResponseEntity<String> registerUser(@RequestBody Member member) {
    
    ResponseEntity<String> response = null;
    
    Member memberFromDB = memberRepository.findByEmail(member.getEmail());
    if (memberFromDB != null) {
      response = ResponseEntity.status(HttpStatus.CONFLICT).body("您輸入的電子郵件已經有人使用");
    }
    
    try {
      member.setRole("user");
      String hashPwd = passwordEncoder.encode(member.getPassword());
      member.setPassword(hashPwd);
      memberRepository.save(member);
      response = ResponseEntity.status(HttpStatus.CREATED).body("成功註冊");
    } catch (Exception e) {
      response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("註冊發生錯誤，由於 : " + e.getMessage());
    }
    
    return response;
  }
}


