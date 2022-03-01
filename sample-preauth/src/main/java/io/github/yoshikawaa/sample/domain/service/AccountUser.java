package io.github.yoshikawaa.sample.domain.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import io.github.yoshikawaa.sample.domain.model.Account;
import lombok.Getter;

public class AccountUser extends User {

    @Getter
    private Account account;

    public AccountUser(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getName(), "", authorities);
        this.account = account;
    }
    
}
