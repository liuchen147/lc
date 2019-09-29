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
 * 用户(管理员)控制器
 * @author Administrator
 *
 */
@RequestMapping("/user")
@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * 用户管理列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(ModelAndView model) {
		model.setViewName("user/user_list");
		return model;
		
	}
	/**
	 * 显示用户列表
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
	 * 添加用户操作
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(User user){
		Map<String, String> ret = new HashMap<String, String>();
		if(user == null){
			ret.put("type", "error");
			ret.put("msg", "数据绑定出错，请联系开发作者!");
			return ret;
		}
		if(StringUtils.isEmpty(user.getUsername())){
			ret.put("type", "error");
			ret.put("msg", "用户名不能为空!");
			return ret;
		}
		if(StringUtils.isEmpty(user.getPassword())){
			ret.put("type", "error");
			ret.put("msg", "密码不能为空!");
			return ret;
		}
		User existUser = userService.findByUserName(user.getUsername());
		if(existUser != null){
			ret.put("type", "error");
			ret.put("msg", "该用户名已经存在!");
			return ret;
		}
		if(userService.add(user) <= 0){
			ret.put("type", "error");
			ret.put("msg", "添加失败!");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "添加成功!");
		return ret;
	}
	
	/**
	 * 编辑用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/edit")
	@ResponseBody
	public Map<String, String> edit(User user){
		Map<String, String> ret=new HashMap<String, String>();
		if (user == null) {
			ret.put("type", "error");
			ret.put("msg", "数据绑定出错，请联系开发作者!");
			return ret;
		}
		if (StringUtils.isEmpty(user.getUsername())) {
			ret.put("type", "error");
			ret.put("msg", "用户名不能为空");
			return ret;
		}
		if (StringUtils.isEmpty(user.getPassword())) {
			ret.put("type", "error");
			ret.put("msg", "密码不能为空");
			return ret;
		}
		User exisUser =userService.findByUserName(user.getUsername());
		if (exisUser!=null) {
			if (user.getId()!=exisUser.getId()) {
				ret.put("type", "error");
				ret.put("msg", "该用户名已存在");
				return ret;
			}
		}
		if (userService.edit(user)<=0) {
			ret.put("type", "error");
			ret.put("msg", "修改失败");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "修改成功");
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
			ret.put("msg", "请选择要删除的数据");
			return ret;
		}
		String idString="";
		for (Long id : ids) {
			idString+=id+",";
			
		}
		idString=idString.substring(0,idString.length()-1);
		if (userService.delete(idString)<=0) {
			ret.put("type", "error");
			ret.put("msg", "删除失败");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "删除成功");
		return ret;
		
		
	}
}
