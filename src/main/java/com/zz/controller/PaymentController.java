package com.zz.controller;

import com.zz.model.Account;
import com.zz.model.Bill;
import com.zz.model.Member;
import com.zz.model.Payment;
import com.zz.repository.AccountRepository;
import com.zz.repository.BillRepository;
import com.zz.repository.MemberRepository;
import com.zz.repository.PaymentRepository;
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
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping(path="/payment")
public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    @PostMapping(path="/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject<Payment> addPayment(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        int accountId = (int) body.get("accountId");
        int amount = (int) body.get("amount");
        int payUserId = (int) body.get("payUserId");
        String desc = (String) body.get("desc");
        Iterable<Integer> userIds = (Iterable<Integer>) body.get("userIds");
        if (accountRepository.findByUserIdAndId((int) request.getAttribute("userId"), accountId) == null) {
            return ResponseObject.error(HttpStatus.BAD_REQUEST.getCode(), "没有权限");
        }
        Iterable<Member> members = memberRepository.findAllById(userIds);
        List<Bill> bills = new ArrayList<>();

        AtomicInteger ratioAll = new AtomicInteger();
        Payment payment = new Payment();
        payment.setPayUserId(payUserId);
        payment.setAmount(amount);
        payment.setDesc(desc);
        Account account = new Account();
        account.setId(accountId);
        payment.setAccount(account);
        Payment savedPayment = paymentRepository.save(payment);
        members.forEach(member -> {
            ratioAll.addAndGet(member.getRatio());
        });
        members.forEach(member -> {
            Bill bill = new Bill();
            bill.setUserId(member.getId());
            bill.setAmount(amount * member.getRatio() / ratioAll.get());
            bill.setPayment(savedPayment);
            bills.add(bill);
        });
        billRepository.saveAll(bills);
        return ResponseObject.success(savedPayment);
    }

    @GetMapping(path="/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject<Iterable<Payment>> getPayments(HttpServletRequest request, @RequestParam("accountId") int accountId) {

        // 验证所有权
        if (accountRepository.findByUserIdAndId((int) request.getAttribute("userId"), accountId) == null) {
            return ResponseObject.error(HttpStatus.FORBIDDEN.getCode(), "没有权限");
        }
        Iterable<Payment> payments = paymentRepository.findByAccountId(accountId);
        return ResponseObject.success(payments);
    }
}
