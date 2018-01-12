package app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import app.bean.auth.AccessControlBean;
import app.common.Constant;
import app.common.StringUtil;
import app.common.UrlParser;
import app.dao.AuthDao;
import app.dao.MenuAuthDao;
import app.dao.RoleMenuAuthDao;
import app.service.RoleAuthService;
import spark.Request;
import spark.annotation.Auto;

public class RoleAuthServiceImpl implements RoleAuthService {

	@Auto(name = AuthDao.class)
	private AuthDao<?> authDao;
	
	@Auto(name = RoleMenuAuthDao.class)
	private RoleMenuAuthDao<?> roleMenuAuthDao;
	
	@Auto(name = MenuAuthDao.class)
	private MenuAuthDao<?> menuAuthDao;
	@Override
	public String reloadAuth(String usercode) {
		if (StringUtil.isNotEmpty(usercode)){
			Map<String,Map<String,AccessControlBean>> auths =  this.findAuthsByUserCode(usercode);
			return new Gson().toJson(auths);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Map<String,AccessControlBean>> reloadAuthBySession(Request req) {
		Map<String,Map<String,AccessControlBean>> auths = null;
		if (req.session() != null) {
			String strAuths = req.session().attribute(Constant.AUTHS);
			auths = new Gson().fromJson(strAuths, Map.class);
		}
		return auths;
	}
	
	/**
	 * 查询用户所拥有的角色、权限及能访问的菜单功能
	 * @param usercode
	 */
	@SuppressWarnings("unchecked")
	private Map<String,Map<String,AccessControlBean>> findAuthsByUserCode(String usercode) {
		List<AccessControlBean> beans = (List<AccessControlBean>) this.authDao.findAuthsByUserCode(usercode,AccessControlBean.class);
		Map<String,Map<String,AccessControlBean>> authMaps = new HashMap<String,Map<String,AccessControlBean>>();
		String url = "";
		Map <String,AccessControlBean> map = null;
		for (AccessControlBean bean : beans) {
			url = "/"+UrlParser.parserUrlPage(bean.getUrl(),Constant.PATSERURL);
			if (authMaps.containsKey(url)){
				map = authMaps.get(url);
                if (!map.containsKey(bean.getAuthCode())){
					map.put(bean.getAuthCode(), bean);
				}
			} else {
				map = new HashMap<String,AccessControlBean>();
				map.put(bean.getAuthCode(), bean);
				authMaps.put(url, map);
			}
		}
		return authMaps;
	}
}
