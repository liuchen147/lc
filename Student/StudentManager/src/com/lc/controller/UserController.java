package com.lc.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lc.entity.User;
import com.lc.page.PageInfo;
import com.lc.service.UserService;

/**
 * �û�(����Ա)������
 * @author Administrator
 *
 */
@RequestMapping("/user")
@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * �û������б�
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(ModelAndView model) {
		model.setViewName("user/user_list");
		return model;
		
	}
	/**
	 * ��ʾ�û��б�
	 * @return
	 */
	@RequestMapping(value = "/get_list",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> get_list(
			@RequestParam(value = "username",required = false,defaultValue = "") String username,
			PageInfo page
			){
		Map<String, Object> ret=new HashMap<String, Object>();
		Map<String, Object> queryMap =new HashMap<String, Object>();
		queryMap.put("username", "%"+username+"%");
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", userService.findList(queryMap));
		ret.put("total", userService.getTotal(queryMap));
		return ret;
		
		
	}
	
	
	/**
	 * ����û�����
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(User user){
		Map<String, String> ret = new HashMap<String, String>();
		if(user == null){
			ret.put("type", "error");
			ret.put("msg", "���ݰ󶨳�������ϵ��������!");
			return ret;
		}
		if(StringUtils.isEmpty(user.getUsername())){
			ret.put("type", "error");
			ret.put("msg", "�û�������Ϊ��!");
			return ret;
		}
		if(StringUtils.isEmpty(user.getPassword())){
			ret.put("type", "error");
			ret.put("msg", "���벻��Ϊ��!");
			return ret;
		}
		User existUser = userService.findByUserName(user.getUsername());
		if(existUser != null){
			ret.put("type", "error");
			ret.put("msg", "���û����Ѿ�����!");
			return ret;
		}
		if(userService.add(user) <= 0){
			ret.put("type", "error");
			ret.put("msg", "���ʧ��!");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "��ӳɹ�!");
		return ret;
	}
	
	/**
	 * �༭�û�
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/edit")
	@ResponseBody
	public Map<String, String> edit(User user){
		Map<String, String> ret=new HashMap<String, String>();
		if (user == null) {
			ret.put("type", "error");
			ret.put("msg", "���ݰ󶨳�������ϵ��������!");
			return ret;
		}
		if (StringUtils.isEmpty(user.getUsername())) {
			ret.put("type", "error");
			ret.put("msg", "�û�������Ϊ��");
			return ret;
		}
		if (StringUtils.isEmpty(user.getPassword())) {
			ret.put("type", "error");
			ret.put("msg", "���벻��Ϊ��");
			return ret;
		}
		User exisUser =userService.findByUserName(user.getUsername());
		if (exisUser!=null) {
			if (user.getId()!=exisUser.getId()) {
				ret.put("type", "error");
				ret.put("msg", "���û����Ѵ���");
				return ret;
			}
		}
		if (userService.edit(user)<=0) {
			ret.put("type", "error");
			ret.put("msg", "�޸�ʧ��");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "�޸ĳɹ�");
		return ret;
		
	}
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(
			@RequestParam(value="ids[]",required=true) Long[] ids
			){
		Map<String, String> ret=new HashMap<String, String>();
		if (ids==null) {
			ret.put("type", "error");
			ret.put("msg", "��ѡ��Ҫɾ��������");
			return ret;
		}
		String idString="";
		for (Long id : ids) {
			idString+=id+",";
			
		}
		idString=idString.substring(0,idString.length()-1);
		if (userService.delete(idString)<=0) {
			ret.put("type", "error");
			ret.put("msg", "ɾ��ʧ��");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "ɾ���ɹ�");
		return ret;
		
		
	}
}
