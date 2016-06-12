package com.coin.repository;

import org.springframework.data.repository.CrudRepository;

import com.coin.domain.User;

public interface UserRepository extends CrudRepository<User, String>{

	User findOne(String id);
}
