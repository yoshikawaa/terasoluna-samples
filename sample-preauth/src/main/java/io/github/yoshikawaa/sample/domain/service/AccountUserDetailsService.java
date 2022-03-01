package io.github.yoshikawaa.sample.domain.service;

import javax.inject.Inject;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.yoshikawaa.sample.domain.model.Account;
import io.github.yoshikawaa.sample.domain.repository.AccountRepository;

@Service
@Transactional(readOnly = true)
public class AccountUserDetailsService implements UserDetailsService {

    @Inject
    private AccountRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = repository.findByName(username);
        if (account == null) {
            throw new UsernameNotFoundException(username + " is not found.");
        }

        return new AccountUser(account, AuthorityUtils.createAuthorityList("ROLE_USER"));
    }
    
}
