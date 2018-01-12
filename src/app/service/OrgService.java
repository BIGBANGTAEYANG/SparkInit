package app.service;

import java.util.List;
import java.util.Map;

import app.bean.InfoBean;
import spark.page.easyui.Page;
import spark.page.easyui.TreeBean;

/**
 * 
 * 功能描述 : 机构服务类 
 * @author : YS
 * @version:
 */
public interface OrgService {
	/**
	 * 查询树形机构列表
	 * @param p 分页对象
	 * @param empNo 当前操作员号
	 * @return
	 */
	public Page findByPage(Page p, String empNo);
	
	/**
	 * 查询机构树
	 * @param checkedArrs
	 * @param empNo 
	 * @return
	 */
	public List<TreeBean> findByOrgCombotree(String []checkedArrs, String empNo);
	
	/**
	 * 获取机构所有角色信息
	 * @param orgcode
	 * @return
	 */
	public List<Map<String,Object>> getAllRolesByOrgcode(String orgcode);
	
	/**
	 * 更新机构关联的角色信息
	 * @param orgcode 机构代码
	 * @param roles 角色代码，以","分隔
	 * @param _usercode 操作员
	 * @return
	 */
	public InfoBean updateOrgRoles(String orgcode, String roles, String _usercode);
}
