package com.imooc.example.axon.account;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mavlarn on 2018/5/22.
 */
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
}
