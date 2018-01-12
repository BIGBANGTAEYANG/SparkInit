package app.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.annotation.Auto;
import app.bean.InfoBean;
import app.bean.user.UserBean;
import app.common.Constant;
import app.common.DateUtil;
import app.common.StringUtil;
import app.entity.SysUser;
import app.security.MD5;
import app.service.LoginService;
import app.service.RoleAuthService;
import app.service.UserService;

public class LoginServiceImpl implements LoginService {
	private final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
//	@Auto(name = UserDao.class)
//    private UserDao<?> userDao;
	@Auto(name = UserServiceImpl.class)
    private UserService userService;
	@Auto(name = RoleAuthServiceImpl.class)
	private RoleAuthService roleAuthService;
	
	
	/** 随机数生成器定义 */
	private static final Random random = new Random();
	/** 字体定义 */
	private static final Font[] CODEFONT = { new Font("宋体", Font.BOLD | Font.ITALIC, 25), new Font("隶书", Font.BOLD | Font.ITALIC, 25),
			new Font("黑体", Font.BOLD | Font.ITALIC, 25), new Font("楷体", Font.BOLD | Font.ITALIC, 25) };
	/** 字体颜色定义 */
	private static final Color[] FONTCOLOR = { Color.RED, Color.BLACK, Color.BLUE };
	/** 背景颜色定义 */
	private static final Color[] BGCOLOR = { Color.cyan, Color.WHITE, Color.lightGray, Color.green };
	/** 字符串buffer */
	private static StringBuffer CONDENUMBER = null;
	/** 生成图片的宽高 */
	private static final int WIDTH = 140, HEIGHT = 23;

	/**
	 * 获取生成的验证码
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@Override
	public void image(HttpServletRequest request, HttpServletResponse response) {
		CONDENUMBER = new StringBuffer();
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(new Color(195, 212, 238));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		drawCode(g, 0);
		drawNoise(g, 10);
		g.setColor(Color.gray);
		g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
		g.dispose();
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/png");
		ServletOutputStream sos = null;
		try {
			sos = response.getOutputStream();
			ImageIO.write(image, "png", sos);
			sos.flush();
			request.getSession().setAttribute("IMAGE_CODE_ID", CONDENUMBER.toString());
		} catch (Exception e) {
			logger.error("获取生成的验证码错误！", e);
//			e.printStackTrace();
		} finally {
			if (sos != null) {
				try {
					sos.close();
				} catch (IOException ex) {
					logger.error("获取生成的验证码错误！", ex);
//					ex.printStackTrace();
				}
				
			}
		}

	}

	/**
	 * 绘制验证码
	 * 
	 * @param graphics
	 * @param i
	 */
	private final void drawCode(Graphics graphics, int i) {
		StringBuffer sb = new StringBuffer();
		int num1 = random.nextInt(10);
		int num2 = random.nextInt(10);
		sb.append(num1).append(" + ").append(num2).append(" = ?");
		graphics.setFont(CODEFONT[random.nextInt(CODEFONT.length)]);
		graphics.setColor(FONTCOLOR[random.nextInt(FONTCOLOR.length)]);
		graphics.drawString(sb.toString(), 10 + i * 20, 20);
		CONDENUMBER.append(num1 + num2);
	}

	/**
	 * 绘制干扰纹
	 * 
	 * @param graphics
	 * @param i
	 */
	private final void drawNoise(Graphics graphics, int lineNumber) {
		graphics.setColor(BGCOLOR[random.nextInt(BGCOLOR.length)]);
		int pointX1, pointY1, pointX2, pointY2;
		for (int i = 0; i < lineNumber; i++) {
			pointX1 = 1 + (int) (Math.random() * WIDTH);
			pointY1 = 1 + (int) (Math.random() * HEIGHT);
			pointX2 = 1 + (int) (Math.random() * WIDTH);
			pointY2 = 1 + (int) (Math.random() * HEIGHT);
			graphics.drawLine(pointX1, pointY1, pointX2, pointY2);
		}
	}
	
	@Override
	public InfoBean regedit(String usercode, String pwd, String des) {
		InfoBean info = new InfoBean();
		if (StringUtil.isEmpty(usercode) || StringUtil.isEmpty(pwd)) {
			info.setCode(InfoBean.FAILURE);
			info.setMessage("用户名或密码不能为空！");
			return info;
		}
		// 判断usercode是否已被占用
		SysUser tempuser = userService.getUserByCode(usercode);
		if(tempuser != null){
			info.setCode(InfoBean.FAILURE);
			info.setMessage("用户账号已存在！");
			return info;
		}
		//long userid = PkServiceImpl.getInstance().getIdentity(Constant.DATA_TABLE_SYS_USER);
		SysUser user = new SysUser();
		//user.setUserid(userid);
		user.setUsercode(usercode);
		user.setUsername(usercode);
		user.setUserpwd(MD5.toMd5(pwd));
		user.setStatus(1);
		user.setCreatetime(DateUtil.formatTime(System.currentTimeMillis(), DateUtil.DEF_DATETIME_FORMAT));
		user.setDes(des);
		int r = userService.saveUser(user);
		if (r != 1) {
			info.setCode(InfoBean.FAILURE);
			info.setMessage("注册失败！");
			return info;
		}
		info.setMessage("注册成功！");
		return info;
	}
	
	@Override
	public InfoBean login(Request req, String userCode, String passWord) {
		InfoBean info = new InfoBean();
		if (StringUtil.isEmpty(userCode) || StringUtil.isEmpty(passWord)) {
			info.setCode(InfoBean.FAILURE);
			info.setMessage("用户名或者密码错误！");
			return info;
		}
		// 判断用户名及密码
		//long time = System.currentTimeMillis();
		UserBean userBean = userService.getUserBeanByCode(userCode);
//		System.out.println("判断用户名及密码 " + (System.currentTimeMillis() - time));
		if (userBean == null) {
			info.setCode(InfoBean.FAILURE);
			info.setMessage("用户不存在！");
			return info;
		} else {
			if (userBean.getStatus() == 0) {
				info.setCode(InfoBean.FAILURE);
				info.setMessage("用户无效！");
				return info;
			}
			if (!MD5.toMd5(passWord).equals(userBean.getUserpwd())) {
				info.setCode(InfoBean.FAILURE);
				info.setMessage("用户名或者密码错误！");
				return info;
			}
		}
		// 更新最后登录时间
		String lastlogintime = DateUtil.formatTime(System.currentTimeMillis(), DateUtil.DEF_DATETIME_FORMAT);
		SysUser user = userBean.getSysUser();
		user.setLastlogintime(lastlogintime);
		//time = System.currentTimeMillis();
		userService.updateUser(user, userCode, true);
//		System.out.println("更新登录时间 " + (System.currentTimeMillis() - time));
		req.session().attribute(Constant.USERID, user.getUserid());
		req.session().attribute(Constant.USERCODE, user.getUsercode());
		req.session().attribute(Constant.USERNAME, user.getUsername());
		req.session().attribute(Constant.USERORGNAME, userBean.getOrgName());
		req.session().attribute(Constant.USERORGCODE, user.getOrgCode());
		//加载用户权限列表
		req.session().attribute(Constant.AUTHS, roleAuthService.reloadAuth(user.getUsercode()));
		//time = System.currentTimeMillis();
		req.session().attribute(Constant.USERROLES, userService.getUserRoles(userCode));
//		System.out.println("获取角色信息 " + (System.currentTimeMillis() - time));
		req.session().attribute(Constant.LOGINDATE, lastlogintime);
		//加载用户需要跟踪的操作日志
//		if(this.logTraceService.queryInterceptSetting() != null)
//		sessionUser.setValue(SessionUser.TRACELOG, this.logTraceService.queryInterceptSetting());
		info.setMessage("登陆成功!");
		return info;
	}
}
