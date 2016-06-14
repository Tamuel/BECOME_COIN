package com.coin.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.coin.domain.FloorPlan;

public interface FloorPlanRepository extends CrudRepository<FloorPlan, String>{
	
	FloorPlan findOne(String id);
	
	List<FloorPlan> findAll();
}
