/** * FileName: TestController * Author:   SAMSUNG-PC 孙中军 * Date:     2018/10/06 11:13 * Description: */package com.qst.goldenarches.controller;import com.fasterxml.jackson.databind.ser.impl.FailingSerializer;import com.github.pagehelper.PageHelper;import com.qst.goldenarches.pojo.Role;import com.qst.goldenarches.pojo.Msg;import com.qst.goldenarches.pojo.Role;import com.qst.goldenarches.service.RoleService;import jdk.nashorn.internal.ir.IfNode;import org.apache.ibatis.annotations.Insert;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Controller;import org.springframework.ui.Model;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RequestParam;import org.springframework.web.bind.annotation.ResponseBody;import javax.servlet.http.HttpSession;import java.util.HashMap;import java.util.List;import java.util.Map;@Controller@RequestMapping("/role")public class RoleController {	@Autowired	private RoleService roleService;	/**	 * 角色：执行删除角色	 * 会删除和这个角色相关的所有信息	 * @param roleId	 * @return	 */	@ResponseBody	@RequestMapping("/deletes")	public Msg deletes( Integer[] roleId ) {		for (Integer i:roleId){			System.out.println("====>"+i);		}		try {			if (roleId.length>0){				Map<String, Object> map = new HashMap<String, Object>();				map.put("roleIds", roleId);				roleService.removeRoles(map);				return Msg.success();			}			return  Msg.fail();		} catch ( Exception e ) {			e.printStackTrace();			return Msg.fail();		}	}	/**	 * 角色：执行角色信息修改方法	 * @param role	 * @return	 */	@ResponseBody	@RequestMapping("doEdit")	public Msg doEdit(Role role){		try {			roleService.editRole(role);			return Msg.success();		}catch (Exception e){			e.printStackTrace();			return Msg.fail();		}	}	/**	 * 角色：跳转方法	 * 跳转到角色修改界面	 * @return	 */	@RequestMapping("edit")	public String toEdit(String id, Model model){		Role dbrole =roleService.getRoleById(id);		model.addAttribute("role", dbrole);		return "role/edit";	}	/**	 * 角色：添加角色	 * @param role	 * @return	 */	@ResponseBody	@RequestMapping("doAdd")	public Msg doAdd(Role role,HttpSession session){		try {			Boolean isUniqueRoleName =(Boolean) session.getAttribute("isUniqueRoleName");			if (isUniqueRoleName!=null&&isUniqueRoleName==true){				session.removeAttribute("isUniqueRoleName");				roleService.addRole(role);				return Msg.success().add("role",role);			}			return Msg.fail();		}catch (Exception e){			e.printStackTrace();			return Msg.fail();		}	}	/**	 * 检验角色名的唯一性	 * @param name	 * @return	 */	@RequestMapping("uniqueName")	@ResponseBody	public Msg validateNameUnique(String name, HttpSession session){		try {			if(name!=null) {				if (roleService.validateNameUnique(name)){					session.setAttribute("isUniqueRoleName",true);					return Msg.success();				}			}			session.setAttribute("isUniqueRoleName", false);			return Msg.fail().add("va_msg","角色已存在");		}catch (Exception e){			e.printStackTrace();			session.setAttribute("isUniqueRoleName",false);			return Msg.fail().add("va_msg","服务异常,稍后重试");		}	}	/***	 * 角色：跳转方法	 * 跳转到角色添加界面	 * @return	 */	@RequestMapping("add")	public String toAdd(){		return "role/add";	}	/**	 * 为角色分配许可	 * @param roleId	 * @param permissionIds	 * @return	 */	@ResponseBody	@RequestMapping("/doAssign")	public Object doAssign( Integer roleId, Integer[] permissionIds ) {		try {			Map<String, Object> paramMap = new HashMap<String, Object>();			paramMap.put("roleId", roleId);			paramMap.put("permissionIds", permissionIds);			roleService.addRolePermissions(paramMap);			return Msg.success();		} catch ( Exception e ) {			e.printStackTrace();			return Msg.fail();		}	}	/**	 * 页面跳转：	 * 跳转到角色许可项目分配主界面	 * @return	 */	@RequestMapping("/assign")	public String assign() {		return "role/assign";	}	/**	 * 角色维护主页面，分页实现查询全部角色	 * @param queryText	 * @param pn	 * @param pagesize	 * @return	 */	@ResponseBody	@RequestMapping("/pageQuery")	public Msg pageQuery( @RequestParam(value = "pageno",defaultValue ="1") Integer pn,						  @RequestParam(value = "pagesize",defaultValue ="5")Integer pagesize , String queryText) {		try {			PageHelper.startPage(pn,pagesize);			List<Role> roles =roleService.getAllRoles(queryText);			com.github.pagehelper.PageInfo<Role> rolePageInfo =					new com.github.pagehelper.PageInfo<Role>(roles,5);			return Msg.success().add("pageInfo",rolePageInfo);		} catch ( Exception e ) {			e.printStackTrace();			return Msg.fail();		}	}	/**	 * 页面跳转：	 * 跳转到角色维护主页面	 * @return	 */	@RequestMapping("/index")	public String index() {		return "role/index";	}}