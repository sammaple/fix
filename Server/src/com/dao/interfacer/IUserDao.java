package com.dao.interfacer;

import com.entity.UserEntity;


public interface IUserDao {
	public boolean save(UserEntity entity);

	public boolean deleteAll();
	
	
}