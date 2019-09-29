package com.lc.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lc.entity.Clazz;

/**
 * �༶dao
 * @author Administrator
 *
 */
@Repository
public interface ClazzDao {
	
	public int add(Clazz clazz);

	public List<Clazz> findList(Map<String, Object> queryMap);
	
	public List<Clazz> findAll();
	
	public int getTotal(Map<String, Object> queryMap);

	public int edit(Clazz clazz);
	

	public int delete(String ids);

	
	
}
