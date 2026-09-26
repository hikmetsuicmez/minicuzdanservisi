package com.hikmetsuicmez.minicuzdanservisi.ledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hikmetsuicmez.minicuzdanservisi.ledger.entity.LedgerEntry;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long>{

}
