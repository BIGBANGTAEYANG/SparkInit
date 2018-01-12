package app.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spark.Request;
import app.bean.InfoBean;

public interface LoginService {

	/**
	 * 绘制验证码
	 * @param request
	 * @param response
	 */
	public void image(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 注册
	 * @param username 用户名
	 * @param pwd 密码
	 * @param des 描述
	 * @return
	 */
	public InfoBean regedit(String username, String pwd, String des);
	
	/**
	 * 登录
	 * @param req
	 * @param userCode 用户账号
	 * @param passWord 用户密码
	 * @return
	 */
	public InfoBean login(Request req, String userName, String passWord);
	
}
