package app.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spark.page.easyui.TreeBean;

/**
 * 
 * 功能描述 : jquery EasyUi tree,combotree工具类
 * @author : YH
 * @version:
 */
public class TreeFactory {
	/**
	 * 使用递归算法生成树节点
	 * @param parent
	 * @param parentBean
	 * @param all
	 * @return List<TreeBean>
	 */
	public List<TreeBean> createTrees(Map<String, Object> parent, TreeBean parentBean, List<Map<String, Object>> all,String[]checkArrs) {
		List<TreeBean> list = new ArrayList<TreeBean>();
		parentBean.setId(StringUtil.getString(parent.get("id")));
		parentBean.setText(StringUtil.getString(parent.get("text")));
		if (checkArrs != null && checkArrs.length > 0) {
			if (in(parentBean.getId(),checkArrs))
				parentBean.setChecked(true);
			else
				parentBean.setChecked(false);
		}
			
		
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("parentid", StringUtil.getString(parent.get("parentid")));
		attributes.put("level", StringUtil.getString(parent.get("level")));
		parentBean.setAttributes(attributes);
		List<Map<String, Object>> childs = queryChildNode(StringUtil.getString(parent.get("id")), all);

		List<TreeBean> children = null;
		if (childs != null && !childs.isEmpty())
			children = new ArrayList<TreeBean>();
		for (Map<String, Object> i : childs) {
			TreeBean childBean = new TreeBean();
			children.add(childBean);
			createTrees(i, childBean, all,checkArrs);
		}
		parentBean.setChildren(children);
		list.add(parentBean);
		return list;
	}

	
	private boolean in(String id,String []arrs) {
		for (String i : arrs) {
			if (id.equals(i)) return true;
		}
		return false;
	}
	
	/**
	 * 描述：取子节点
	 * @param parentId
	 * @param all
	 * @return List<Map<String, Object>> 
	 */ 
	private List<Map<String, Object>> queryChildNode(String parentId, List<Map<String, Object>> all) {
		List<Map<String, Object>> childs = new ArrayList<Map<String, Object>>();
		String tparentid = "";
		for (Map<String, Object> i : all) {
			tparentid = StringUtil.getString(i.get("parentid"));
			if (tparentid.equals(parentId))
				childs.add(i);
		}
		return childs;
	}
}
