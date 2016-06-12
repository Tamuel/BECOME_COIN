package com.coin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coin.domain.FloorPlan;
import com.coin.repository.FloorDataRepository;
import com.coin.repository.FloorPlanRepository;
import com.coin.repository.ObjectRepository;
import com.coin.repository.UserRepository;

@RestController
@RequestMapping("/")
public class Controller {

	@Autowired
	ObjectRepository objectRepository;
	
	@Autowired
	FloorDataRepository floorDataRepository;
	
	@Autowired
	FloorPlanRepository floorPlanRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping("/")
	public String startMenu() {
		return "CoinServer";
	}
	
	@RequestMapping("/{name}")
	public FloorPlan floorPlanDetail(@PathVariable String id) {
		FloorPlan floorPlan = floorPlanRepository.findOne(id);
		return floorPlan;
	}
}
