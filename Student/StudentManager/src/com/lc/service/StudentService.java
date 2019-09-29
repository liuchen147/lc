package com.lc.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lc.entity.Student;

/**
 * ѧ��Service
 * @author Administrator
 *
 */
@Service
public interface StudentService {

	public int add(Student student);
	
	
	public int edit(Student student);
	
	public int delete(String ids);
	
	public List<Student> findList(Map<String,Object> queryMap);
	
	public List<Student> findAll();
	
	public int getTotal(Map<String,Object> queryMap);
	
	public Student findByUserName(String username);
	
}
