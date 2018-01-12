package app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.common.LogState;
import app.common.StringUtil;
import app.bean.InfoBean;
import app.common.HodsLogUtil;
import app.common.TreeFactory;
import app.dao.OrgDao;
import app.dao.RoleDao;
import app.dao.UserDao;
import app.dao.UserRoleDao;
import app.entity.SysOrgRole;
import app.entity.SysRole;
import app.exception.StatusException;
import app.service.OrgService;
import javolution.util.FastTable;
import spark.annotation.Auto;
import spark.page.easyui.Page;
import spark.page.easyui.TreeBean;

public class OrgServiceImpl implements OrgService {

	private final static Logger logger = LoggerFactory.getLogger(OrgServiceImpl.class);
	@Auto(name = OrgDao.class)
	private OrgDao<?> orgDao;
	
	@Auto(name = RoleDao.class)
	private RoleDao<?> roleDao;
	
	@Auto(name = UserDao.class)
	private UserDao<?> userDao;
	
	@Auto(name = UserRoleDao.class)
	private UserRoleDao<?> userRoleDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public Page findByPage(Page p, String empNo) {
		try {
			Map<String,String> head = new HashMap<String,String>();
			head.put("orgcode", "0");
			head.put("orgname", "机构列表");
			head.put("level", "0");
			p.setMaxNum(100000);
			
			p = orgDao.findByPage(p);
			//HodsLogUtil.getInstance().AddAuditLog(empNo, "查询", "机构管理", "SYS_ORG", LogState.成功, System.currentTimeMillis(),"查询机构树成功",null);
			
			if (p != null && p.getRows()!= null && !p.getRows().isEmpty()) {
				List<Map<String, String>> lis = new ArrayList<Map<String, String>>();
				lis.add(head);
				Map<String, Object> map = null;
				Map<String, String> mapTmp = null;
				for (int i = 0;i<p.getRows().size();i++) {
					mapTmp = new HashMap<String, String>();
					map = (Map<String, Object>) p.getRows().get(i);
					mapTmp.put("orgcode", String.valueOf(map.get("orgcode")));
					mapTmp.put("orgname", String.valueOf(map.get("orgname")==null?"":map.get("orgname")));
					mapTmp.put("_parentId", String.valueOf(map.get("_parentId")));
					mapTmp.put("level", String.valueOf(map.get("org_level")));
					mapTmp.put("update_date", String.valueOf(map.get("update_date")));
					mapTmp.put("roles", String.valueOf(map.get("roles") == null?"":map.get("roles")));
					if(!String.valueOf(map.get("org_level")).equals("1") && map.get("pcode")!=null){
						mapTmp.put("state", "closed");
					}
					lis.add(mapTmp);
				}
				p.getRows().clear();
				//p.getRows().add(0,head);
				p.getRows().addAll(lis);
				p.total++;
			}
			return p;
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("用户["+ empNo +"]查询机构信息异常！", e);
			HodsLogUtil.getInstance().AddAuditLog(empNo, "查询", "获取机构树", "SYS_ORG", LogState.失败, System.currentTimeMillis(),"用户["+ empNo +"]查询机构树失败！"+e.toString(), null);
			throw new StatusException("查询机构树失败！");
		}
	}

	@Override
	public List<TreeBean> findByOrgCombotree(String[] checkedArrs, String empNo) {
		List<Map<String, Object>> list=new ArrayList<>();
		try {
			list = orgDao.findByOrgCombotree();
			//HodsLogUtil.getInstance().AddAuditLog(empNo, "查询", "机构管理", "SYS_ORG", LogState.成功, System.currentTimeMillis(),"机构树形下拉框信息查询成功", null);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("用户["+ empNo +"]查询机构树异常！", e);
			HodsLogUtil.getInstance().AddAuditLog(empNo, "查询", "获取机构树", "SYS_ORG", LogState.失败, System.currentTimeMillis(),"用户["+ empNo +"]查询机构树失败！"+e.toString(), null);
			throw new StatusException("查询机构树失败！");
		}
		
		List<TreeBean> treeList = new ArrayList<TreeBean>();
		//加入顶级菜单，id为0
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("id", 0);
//		map.put("text","机构列表");
//		list.add(0,map);
		TreeFactory tree = new TreeFactory();
		if (list != null && !list.isEmpty())
			treeList = tree.createTrees(list.get(0), new TreeBean(), list,checkedArrs);
		return treeList;
	}
	
	public List<Map<String,Object>> getAllRolesByOrgcode(String orgcode) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		@SuppressWarnings("unchecked")
		List<SysRole> alls = (List<SysRole>) roleDao.findAllRoles();
		
		@SuppressWarnings("unchecked")
		List<SysOrgRole> res = (List<SysOrgRole>) orgDao.getRolesByOrgCode(orgcode);
		Map<String,Object> map = null;
		if (res == null || res.isEmpty()) {
			// 如果没有配置角色，则返回所有角色信息
			for (SysRole r : alls) {
				map = new HashMap<String,Object>();
				map.put("id", r.getRolecode());
				map.put("text",r.getRolename());
				list.add(map);
			}
		} else {
			// 返回机构拥有的角色信息
			if (alls != null && !alls.isEmpty()) {				
				for (SysRole r : alls) {
					map = new HashMap<String,Object>();
					map.put("id", r.getRolecode());
					map.put("text",r.getRolename());
					for (SysOrgRole i : res) {
						if(r.getRolecode().equals(i.getRolecode())) {
							map.put("checked", "true");
							break;
						}
					}
					list.add(map);
				}
			}
		}
		return list;
	}

	public InfoBean updateOrgRoles(String orgcode, String roles, String _usercode) {
		InfoBean infoBean = new InfoBean();
		if (StringUtil.isEmpty(orgcode)) {
			infoBean.setCode(InfoBean.FAILURE);
			infoBean.setMessage("请先选择机构！");
			return infoBean;
		}
		// 获取机构的原角色信息
		@SuppressWarnings("unchecked")
		List<SysOrgRole> oldData = (List<SysOrgRole>) orgDao.getRolesByOrgCode(orgcode);
		// 清理机构角色改变后，机构中移除的角色与机构下用户的关联关系
		List<String> delRoles = new FastTable<String>();
		if (oldData != null && !oldData.isEmpty()) {
			String[] aryRole = null;
			if (StringUtil.isNotEmpty(roles)) {
				aryRole = roles.split(",");
			}
			for (SysOrgRole osor : oldData) {
				if (aryRole != null) {
					boolean isExist = false;
					for (String nrole : aryRole) {
						if (nrole.equals(osor.getRolecode())) {
							isExist = true;
							break;
						}
					}
					if (!isExist) {
						delRoles.add(osor.getRolecode());
					}
				} else {
					delRoles.add(osor.getRolecode());
				}
			}
		}
		if (!delRoles.isEmpty()) {
			// 取出机构下所有用户
			List<Map<String, Object>> users = userDao.getUsercodesByOrgcode(orgcode);
			if (users != null && !users.isEmpty()) {
				Object[][] objs = new Object[delRoles.size() * users.size()][2];
				String[] obj = null;
				int i = 0;
				for (String delRole : delRoles) {
					for (Map<String, Object> usercodes : users) {
						obj = new String[2];
						obj[0] = delRole;
						obj[1] = String.valueOf(usercodes.get("usercode"));
						objs[i++] = obj;
					}
				}
				userRoleDao.batchDelete(objs);
			}
		}
		// 删除原机构-角色关联信息
		orgDao.removeOrgRoles(orgcode);
		// 写入最新关系
		if (StringUtil.isEmpty(roles)) {
			infoBean.setCode(InfoBean.SUCCESS);
			infoBean.setMessage("机构角色关联关系更新成功！");
			logger.info("用户["+ _usercode +"]更新机构-角色关联关系成功！机构代码["+ orgcode +"]，角色代码["+ roles +"]");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "机构-角色关系更新", "机构管理", "SYS_ORG_ROLE", LogState.成功, System.currentTimeMillis(), "用户["+ _usercode +"]更新机构-角色关联关系成功！机构代码["+ orgcode +"]，角色代码["+ roles +"]", null);
			return infoBean;
		}
		String[] aryRole = roles.split(",");
		Object[][] objs = new Object[aryRole.length][2];
		String[] obj = null;
		for (int i = 0;i < aryRole.length; i++) {
			obj = new String[2];
			obj[0] = orgcode;
			obj[1] = aryRole[i];
			objs[i] = obj;
		}
		boolean isSuccess = orgDao.batchInsert(objs);
		if (isSuccess) {
			infoBean.setCode(InfoBean.SUCCESS);
			infoBean.setMessage("机构角色关联关系更新成功！");
			logger.info("用户["+ _usercode +"]更新机构-角色关联关系成功！机构代码["+ orgcode +"]，角色代码["+ roles +"]");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "机构-角色关系更新", "机构管理", "SYS_ORG_ROLE", LogState.成功, System.currentTimeMillis(), "用户["+ _usercode +"]更新机构-角色关联关系成功！机构代码["+ orgcode +"]，角色代码["+ roles +"]", null);
		} else {
			infoBean.setCode(InfoBean.FAILURE);
			infoBean.setMessage("机构角色关联关系更新失败！");
			logger.info("用户["+ _usercode +"]更新机构-角色关联关系失败！机构代码["+ orgcode +"]，角色代码["+ roles +"]");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "机构-角色关系更新", "机构管理", "SYS_ORG_ROLE", LogState.失败, System.currentTimeMillis(), "用户["+ _usercode +"]更新机构-角色关联关系失败！机构代码["+ orgcode +"]，角色代码["+ roles +"]", null);
		}
		return infoBean;
	}
}
