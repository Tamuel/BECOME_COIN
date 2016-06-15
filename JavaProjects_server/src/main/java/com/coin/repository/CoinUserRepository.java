package com.coin.repository;

import org.springframework.data.repository.CrudRepository;

import com.coin.domain.CoinUser;

public interface CoinUserRepository extends CrudRepository<CoinUser, String>{
	
	CoinUser findOne(String id);
	
	CoinUser findByIdAndPassword(String id, String password);
}
