package com.zz.controller;

import com.zz.model.Account;
import com.zz.repository.AccountRepository;
import com.zz.response.HttpStatus;
import com.zz.response.ResponseObject;
import com.zz.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path="/account")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;


    @PostMapping(path="/addAccount", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject<Account> addAccount (@RequestBody Account account, HttpServletRequest request) {
        int userId = (int) request.getAttribute("userId");
        account.setUserId(userId);
        Account account1 = accountRepository.save(account);
        return ResponseObject.success(account1);
    }

    @GetMapping(path="/getList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject<Iterable<Account>> getAccountList (HttpServletRequest request) {
        Iterable<Account> accounts = accountRepository.findAllByUserId((int) request.getAttribute("userId"));
        return ResponseObject.success(accounts);
    }

    @Transactional
    @DeleteMapping(path="/deleteAccount")
    public ResponseObject<String> deleteAccount (@RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        accountRepository.deleteByUserIdAndId((int) request.getAttribute("userId"), (int) requestBody.get("id"));
        return ResponseObject.success("删除成功");
    }
}