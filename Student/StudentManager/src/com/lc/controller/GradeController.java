package com.lc.controller;

import java.util.Arrays;
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

import com.lc.entity.Grade;
import com.lc.page.PageInfo;
import com.lc.service.GradeService;
import com.lc.util.StringUtil;

/**
 * 年纪信息管理
 * @author Administrator
 *
 */
@RequestMapping("/grade")
@Controller
public class GradeController {
	
	@Autowired
	private GradeService gradeService;
	/**
	 * 年纪列表页面
	 * @param mode
	 * @return
	 */
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public ModelAndView list(ModelAndView mode) {
		mode.setViewName("grade/grade_list");
		return mode;
	}
	
	/**
	 * 获取年纪列表
	 * @param name
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/get_list",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getlist(
			@RequestParam(value = "name",required = false,defaultValue = "") String name,
			PageInfo page
			){
		Map<String, Object> ret =new HashMap<String, Object>();
		Map<String, Object> queryMap =new HashMap<String, Object>();
		queryMap.put("name", "%"+name+"%"); 
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", gradeService.findList(queryMap));
		ret.put("total", gradeService.getTotal(queryMap));
		return ret;
		
	}
	/**
	 * 添加年级信息
	 * @param grade
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Grade grade){
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(grade.getName())){
			ret.put("type", "error");
			ret.put("msg", "年级名称不能为空！");
			return ret;
		}
		Grade name =gradeService.findName(grade.getName());
		if (name!=null) {
			ret.put("type", "error");
			ret.put("msg", "改年纪已存在");
			return ret;
		}
		if(gradeService.add(grade) <= 0){
			ret.put("type", "error");
			ret.put("msg", "年级添加失败！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "年级添加成功！");
		return ret;
	}
	/**
	 * 编辑年纪信息
	 * @param grade
	 * @return
	 */
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(Grade grade){
		
		Map<String, String> ret =new HashMap<String, String>();
		if (StringUtils.isEmpty(grade.getName())) {
			ret.put("type", "error");
			ret.put("msg", "年纪名称不能为空");
			return ret;
		}
	
		if (gradeService.edit(grade)<=0) {
			ret.put("type", "error");
			ret.put("msg", "年纪修改失败");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "年纪修改成功");
		return ret;
		
	}
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(
			@RequestParam(value = "ids[]",required = true) Long[] ids
			){
		Map<String, String> ret=new HashMap<String, String>();
		if (ids==null|| ids.length==0) {
			ret.put("type", "error");
			ret.put("msg", "请选择删除的数据");
			return ret;
		}
		try {
			if (gradeService.delete(StringUtil.joinString(Arrays.asList(ids), ","))<=0) {
				ret.put("type", "error");
				ret.put("msg", "删除失败");
				return ret;
			}
		} catch (Exception e) {
			ret.put("type", "error");
			ret.put("msg", "改年纪下有班级信息，请勿冲动！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;
	}
}
