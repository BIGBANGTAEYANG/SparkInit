package app.dao;

import java.util.List;
import java.util.Map;

import app.common.Constant;
import app.entity.SysOrgRole;
import spark.db.ObjectDao;
import spark.page.easyui.Page;

/**
 * 
 * 功能描述 : 机构类数据访问 
 * @author : YS
 * @version: 
 * @param <T>
 */
public class OrgDao<T> extends ObjectDao<T> {
	
	private final static String FIND_ORG_TREE="SELECT a.orgcode orgcode, b.pcode pcode, orgname, c.roles,a.pcode _parentId, org_level, update_date FROM SYS_ORG a LEFT JOIN (SELECT DISTINCT pcode FROM SYS_ORG) b ON a.orgcode=b.pcode LEFT JOIN (SELECT b.orgcode,GROUP_CONCAT(a.rolename) AS roles FROM SYS_ROLE a,SYS_ORG_ROLE b WHERE a.rolecode = b.rolecode GROUP BY b.orgcode) c ON a.orgcode=c.orgcode ORDER BY org_level, orgcode";
	/**
	 * 菜单树分页检索
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public Page findByPage(Page p) throws Exception{
		return super.findPageByMysql(Constant.SOURCEID, FIND_ORG_TREE, p, null);
	}
	
	/**下拉树机构查询SQL*/
	private static final String FINDBYORGCOMBOTREE_SQL = "SELECT * FROM (SELECT orgcode AS id, orgname AS TEXT, pcode AS parentid, org_level AS LEVEL FROM SYS_ORG ORDER BY org_level, orgcode) t";
	/**
	 * 描述：查询下拉菜单树
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> findByOrgCombotree(){
		return super.findMapListBysql(Constant.SOURCEID, FINDBYORGCOMBOTREE_SQL, null);
	}
	
	private final static String ORG_ROLES_SQL = "SELECT * FROM SYS_ORG_ROLE where orgcode = ?";
	/**
	 * 根据orgcode查询机构的角色信息
	 * @param orgcode 机构代码
	 * @return 机构角色列表
	 */
	public List<?> getRolesByOrgCode(String orgcode){
		return super.findBySqlList(Constant.SOURCEID, ORG_ROLES_SQL, SysOrgRole.class, new Object[]{orgcode});
	}
	
	/** 移除机构-角色关联关系SQL*/
	private final static String DELETE_ORG_ROLES_SQL = "DELETE FROM SYS_ORG_ROLE WHERE orgcode = ?";
	/**
	 * 移除机构-角色关联关系SQL
	 * @param orgcode 机构代码
	 * @return
	 */
	public void removeOrgRoles(String orgcode) {
		super.update(Constant.SOURCEID, DELETE_ORG_ROLES_SQL,new Object[]{orgcode});
	}
	
	/** 插入机构-角色关联关系信息SQL*/
	private final static String INSERT_ORG_ROLES_SQL = "INSERT INTO SYS_ORG_ROLE(orgcode,rolecode) values(?,?)";
	/**
	 * 插入机构-角色关联关系信息
	 * @param objs 更新的数据
	 * @return
	 */
	public boolean batchInsert(Object[][] objs) {
		return super.batchUpdate(Constant.SOURCEID, INSERT_ORG_ROLES_SQL, objs);
	}
}
