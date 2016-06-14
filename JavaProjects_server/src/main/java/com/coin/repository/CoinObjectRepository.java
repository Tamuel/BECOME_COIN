package com.coin.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.coin.domain.CoinObject;

public interface CoinObjectRepository extends CrudRepository<CoinObject, String>{

	CoinObject findOne(String toolMode);
	
	List<CoinObject> findAll();
}
