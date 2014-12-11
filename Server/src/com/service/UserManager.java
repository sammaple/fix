package com.service;

import com.dao.interfacer.IUserDao;
import com.entity.UserEntity;

public class UserManager {

	private IUserDao userDao;
	
	public IUserDao getUserDao() {
		return userDao;
	}
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}
	
	/**
	 * 用户校验
	 * @return
	 */
	public boolean userVerify(String userName, String password)
	{
		return userDao.verify(userName, password);
	}
	
	public int userRegister(UserEntity user)
	{	
		if(userDao.findByEmail(user.getEmail()) != null)
		{
			return 1;
		}
		userDao.save(user);
		
		return 0;
	}
	
	public boolean userUpdate(UserEntity user)
	{
		userDao.save(user);
		
		return true;
	}
	
	public UserEntity find(String userName){
		return userDao.findByUserName(userName);
	}
	
	public UserEntity findByEmail(String eMail){
		if(eMail == null||eMail.isEmpty()){
			return null;
		}
		return userDao.findByEmail(eMail);
	}
}
