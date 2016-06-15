package com.coin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coin.domain.CoinUser;
import com.coin.domain.FloorObject;
import com.coin.domain.FloorPlan;
import com.coin.repository.CoinUserRepository;

@RestController
@RequestMapping("/")
public class Controller {
	
	@Autowired
	CoinUserRepository userRepository;
	
	@RequestMapping(value = "/", produces="application/json; charset=UTF-8")
	public String index() {
		return "CoinServer!!!!! MadeBy BECOME";
	}
	
	/*User*/
	@RequestMapping(value = "/addUser", produces="application/json; charset=UTF-8")
	public String addUser(CoinUser newUser) {
		if(userRepository.findOne(newUser.getId()) == null) {
			userRepository.save(newUser);
			return "JOIN_SUCCESS";
		}
		else {
			return "JOIN_REJECT";
		}
	}
	
	//위험 -> 실행 시 서버 터짐
	/*@RequestMapping("/userList")
	public Iterable<CoinUser> userList() {
		Iterable<CoinUser> user = userRepository.findAll();
		return user;
	}*/
	
	@RequestMapping(value = "/userSearch/{id}/{password}", produces="application/json; charset=UTF-8")
	public String userSearch(@PathVariable String id, @PathVariable String password) {
		CoinUser user = userRepository.findByIdAndPassword(id, password);
		if(user!=null) {
			String temp = user.getName()+"\n";
			for(FloorPlan planTemp : user.getPlans()) {
				temp += planTemp.toString()+"\n";
			}
			return temp;
		}
		else {
			return "LOGIN_REJECT";
		}
	}
	
	/*FloorPlan*/
	@RequestMapping(value = "/addFloorPlan", produces="application/json; charset=UTF-8")
	public String addFloorPlan(FloorPlan plan) {
		CoinUser user = userRepository.findOne(plan.getOwnerId());
		if(user != null) {
			user.addFloorPlan(plan);
			plan.setUser(user);
			String temp = "ADD_SUCCESS\n";
			for(FloorPlan planTemp : user.getPlans()) {
				temp += planTemp.toString()+"\n";
			}
			userRepository.save(user);
			return temp;
		}
		else {
			return "ADD_REJECT";
		}
	}
	
	@RequestMapping(value = "/floorPlanList/{id}", produces="application/json; charset=UTF-8")
	public String planList(@PathVariable String id) {
		CoinUser user = userRepository.findOne(id);
		String temp = "";
		for(FloorPlan planTemp : user.getPlans())
			temp += planTemp.toString()+"\n";
		return temp;
	}
	
	@RequestMapping(value = "/deleteFloorPlan/{userId}/{floorId}", produces="application/json; charset=UTF-8")
	public String delFloorPlan(@PathVariable String userId, @PathVariable int floorId) {
		CoinUser user = userRepository.findOne(userId);
		if(user != null) {
			for(FloorPlan plan : user.getPlans()) {
				if(plan.getId() == floorId) {
					user.removePlan(plan);
					userRepository.save(user);
					return "DELETE_SUCCESS";
				}
			}
			return "DELETE_REJECT";
		}
		else {
			return "DELETE_REJECT";
		}
	}
	
	/*Object*/
	@RequestMapping(value = "/addFloorObject/{userId}/{floorId}/{temp}", produces="application/json; charset=UTF-8")
	public String addFloorObject(@PathVariable String userId, @PathVariable int floorId, @PathVariable String temp) {
		CoinUser user = userRepository.findOne(userId);
		if(user != null) {
			for(FloorPlan plan : user.getPlans()) {
				if(plan.getId() == floorId) {
					FloorObject fObject = new FloorObject();
					plan.addFloorObject(fObject.getObject(temp).get(0));
					fObject.setFloorPlan(plan);
					userRepository.save(user);
					return "ADD_SUCCESS" + fObject.toString();
				}
			}
			return "ADD_REJECT";
		}
		else {
			return "ADD_REJECT";
		}
	}
	
	@RequestMapping(value = "/floorObjectList/{userId}/{floorId}", produces="application/json; charset=UTF-8")
	public String floorObjectList(@PathVariable String userId, @PathVariable int floorId) {
		CoinUser user = userRepository.findOne(userId);
		if(user != null) {
			for(FloorPlan plan : user.getPlans()) {
				if(plan.getId() == floorId) {
					String temp = "";
					for(FloorObject obj : plan.getObjects())
						temp += obj.toString()+"\n";
					
					return temp;
				}
			}
			return "LOAD_REJECT";
		}
		else
			return "LOAD_REJECT";
	}
}
