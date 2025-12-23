package org.j2os.repository.spec;

import org.j2os.entity.Account;
import org.springframework.data.jpa.domain.Specification;

public final class AccountSpecifications {
    private AccountSpecifications(){}

    public static Specification<Account> hasMinBalance(Long min){
        return (root, query, cb) ->
                min == null ? null :
                cb.greaterThan(root.get("accountBalance"), min);
    }

    public static Specification<Account> hasEmail(String email){
        return (root, query, cb) ->
                email == null ? null:
                cb.equal(root.get("accountBalance"), email);
    }
}
