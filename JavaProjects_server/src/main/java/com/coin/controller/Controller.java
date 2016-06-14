package com.coin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coin.domain.CoinObject;
import com.coin.domain.FloorPlan;
import com.coin.repository.CoinObjectRepository;
import com.coin.repository.FloorDataRepository;
import com.coin.repository.FloorPlanRepository;
import com.coin.repository.UserRepository;

@RestController
@RequestMapping("/")
public class Controller {

	@Autowired
	CoinObjectRepository objectRepository;
	
	@Autowired
	FloorDataRepository floorDataRepository;
	
	@Autowired
	FloorPlanRepository floorPlanRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping("/")
	public String index() {
		return "CoinServer";
	}
	
	@RequestMapping("/{name}")
	public FloorPlan floorPlanDetail(@PathVariable String id) {
		FloorPlan floorPlan = floorPlanRepository.findOne(id);
		return floorPlan;
	}
	
	@RequestMapping("/addObject")
	public CoinObject addObject(CoinObject object) {
		CoinObject objData = objectRepository.save(object);
		return objData;
	}
	
	@RequestMapping("/objectList")
	public String objectList() {
		List<CoinObject> coinObject = objectRepository.findAll();
		return coinObject.toString().replace(',', '\n');
	}
}
