package com.hikmetsuicmez.minicuzdanservisi.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hikmetsuicmez.minicuzdanservisi.account.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

}
