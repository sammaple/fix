package com.dao.interfacer;

import java.util.List;

import com.entity.PmEntity;


public interface IPmDao {
	public boolean save(PmEntity entity);

	public boolean deleteAll();
	
	
	public long getInfoByCompareId(String deviceid,List<PmEntity> list) ;
	
}