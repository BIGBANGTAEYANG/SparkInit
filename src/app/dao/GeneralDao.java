package app.dao;


import spark.db.ObjectDao;
import spark.page.easyui.Page;

import java.util.List;
import java.util.Map;

import app.common.Constant;

public class GeneralDao<T> extends ObjectDao<T> {
	
	/**
	 * 分页查询
	 * @param c
	 * @param p
	 * @return
	 */
	public Page findPageAllDatas(Class<?> c, Page p) {
		return (Page) super.queryPagination(Constant.SOURCEID, c, p);
	}
	
	/**
	 * 根据查询SQL分页查询
	 * @param p 分页对象
	 * @param sql 查询SQL
	 * @param params 查询条件
	 * @return
	 */
	public Page getDataByPage(Page p, String sql, Object[] params) {
		return super.findPageByMysql(Constant.SOURCEID, sql, p, params);
	}
	
	/**
	 * 通过SQL查询列表
	 * @param sql 查询sql
	 * @param params sql参数
	 * @return
	 */
	public List<Map<String, Object>> getListDataBySql(String sql, Object[] params) {
		return super.findMapListBysql(Constant.SOURCEID, sql, params);
	}
	
	/**
	 * 获取最大id
	 * @param tablename 表名
	 * @param pkField 主键
	 * @return
	public Map<String, Object>getMaxIdentity(String tablename, String pkField) {
		return super.findMapBysql(Constant.SOURCEID, "select max("+pkField+") as id from "+ tablename +"", null);
	}
	 */
	
	/**
	 * 锁表
	 * @param tablename
	public void lockTable() {
		super.findMapBysql(Constant.SOURCEID, "select * from sys_tablepk for update", null);
	}
	 */
	
	/** 主键生成表查询SQL
	private final static String FINDTABLEPK_SQL = "select * from sys_tablepk where tableName = ?";
	/**
	 * 查询主键生成表
	 * @param tableName 表名
	 * @return
	public SysTablepk findTablePkById(String tableName) {
		return (SysTablepk) super.findEntityBySql(Constant.SOURCEID, FINDTABLEPK_SQL, SysTablepk.class, new Object[]{tableName});
	}
	 */
	
	/**新增主键生成表数据SQL
	private final static String ADDTABLEPK_SQL = "insert into sys_tablepk values(?,?)";
	/**
	 * 新增主键生成表数据
	 * @param stpk
	 * @return
	public int saveTablepk(SysTablepk stpk) {
		return super.update(Constant.SOURCEID, ADDTABLEPK_SQL, new Object[]{stpk.getTableName(), stpk.getMaxValue()});
	}
	 */
	
	/**更新主键生成表数据SQL
	private final static String UPDATETABLEPK_SQL = "update sys_tablepk t set t.maxValue = ? where t.tableName = ?";
	/**
	 * 更新主键生成表
	 * @param stpk
	 * @return
	public int updateTablepk(SysTablepk stpk) {
		return super.update(Constant.SOURCEID, UPDATETABLEPK_SQL, new Object[]{stpk.getMaxValue(), stpk.getTableName()});
	}
	 */

}
