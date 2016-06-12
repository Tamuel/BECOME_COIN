package com.coin.repository;

import org.springframework.data.repository.CrudRepository;

public interface ObjectRepository extends CrudRepository<Object, String>{

	Object findOne(String toolMode);
}
