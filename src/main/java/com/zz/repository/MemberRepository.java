package com.zz.repository;

import com.zz.model.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Integer> {

    Iterable<Member> findByAccountId(int accountId);

    Member findByAccountIdAndId(int accountId, int memberId);
}