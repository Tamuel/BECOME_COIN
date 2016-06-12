package com.coin.repository;

import org.springframework.data.repository.CrudRepository;

import com.coin.domain.FloorData;

public interface FloorDataRepository extends CrudRepository<FloorData, String>{
	
	FloorData findOne(String name);
}
