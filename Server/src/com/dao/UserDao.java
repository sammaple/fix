
package com.dao;

import com.dao.interfacer.IUserDao;
import com.entity.UserEntity;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;

public class UserDao implements IUserDao {

    private Datastore ds;

    public UserDao(Mongo mongo, Morphia morphia, String dbName,
            String userName, String pw) {
        ds = morphia.createDatastore(mongo, dbName);
    }

    /*@Override
    public boolean verify(String userName, String password) {
        Query<UserEntity> query = ds.find(UserEntity.class, "userName",
                userName);
        query.filter("password", password);
        if (query.get() != null)
            return true;

        return false;
    }

    public UserEntity findByUserName(String userName) {
        Query<UserEntity> query = ds.find(UserEntity.class, "userName",
                userName);

        return query.get();
    }

    public UserEntity findByEmail(String email) {
        Query<UserEntity> query = ds.find(UserEntity.class, "email", email);
        return query.get();
    }*/

    public boolean save(UserEntity entity) {
        ds.save(entity);
        return true;
    }

	@Override
	public boolean deleteAll() {
		
        Query<UserEntity> query = ds.find(UserEntity.class);
        ds.delete(query.get());
		return true;
	}

	/*@Override
	public boolean deleteAllById(Object id) {

        Query<SnapEntity> query = ds.find(SnapEntity.class, "_id", id);
        ds.delete(query);
        
		return true;
	}*/
	

}
