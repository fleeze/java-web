package com.zz.controller;

import com.zz.model.Account;
import com.zz.model.Member;
import com.zz.repository.AccountRepository;
import com.zz.repository.MemberRepository;
import com.zz.response.HttpStatus;
import com.zz.response.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/member")
public class MemberController {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @PostMapping(path="/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject<Iterable<Member>> addMember(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        int accountId = (int) body.get("accountId");
        Object membersObj = body.get("members");
        if (accountRepository.findByUserIdAndId((int) request.getAttribute("userId"), accountId) == null) {
            return ResponseObject.error(HttpStatus.BAD_REQUEST.getCode(), "没有权限");
        }
        if (membersObj instanceof Iterable) {
            Iterable<String> members = (Iterable<String>) membersObj;
            List<Member> savedMembers = new ArrayList<>();
            members.forEach(member -> {
                Member savedMember = new Member();
                Account account = new Account();
                account.setId(accountId);
                savedMember.setAccount(account);
                savedMember.setName(member);
                savedMembers.add(savedMember);
            });
            Iterable<Member> members1 = memberRepository.saveAll(savedMembers);
            return ResponseObject.success(members1);
        } else {
            return ResponseObject.error(HttpStatus.BAD_REQUEST.getCode(), "members参数错误");
        }
    }

    @GetMapping(path="/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject<Iterable<Member>> getMembers(HttpServletRequest request, @RequestParam("accountId") int accountId) {
        // 验证所有权
        if (accountRepository.findByUserIdAndId((int) request.getAttribute("userId"), accountId) == null) {
            return ResponseObject.error(HttpStatus.FORBIDDEN.getCode(), "没有权限");
        }
        Iterable<Member> members = memberRepository.findByAccountId(accountId);
        return ResponseObject.success(members);
    }

    // 更改成员的ratio
    @Transactional
    @PostMapping(path="/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject<Member> updateMember(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        int accountId = (int) body.get("accountId");
        int memberId = (int) body.get("memberId");
        int ratio = (int) body.get("ratio");
        if (accountRepository.findByUserIdAndId((int) request.getAttribute("userId"), accountId) == null) {
            return ResponseObject.error(HttpStatus.BAD_REQUEST.getCode(), "没有权限");
        }
        Member member = memberRepository.findByAccountIdAndId(accountId, memberId);
        member.setRatio(ratio);
        memberRepository.save(member);
        return ResponseObject.success(member);
    }
}
