package demo.third.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demo.third.dao.UserDao;
import demo.third.model.User;
import demo.third.util.OAuthHelper;

/**
 * 用户使用本地账号登录
 */
@WebServlet("/login")
public class AccountLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * post请求执行登录验证
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String account = request.getParameter("account");
        String passwd = request.getParameter("passwd");

        User user = UserDao.findUserByAccount(account.trim());
        if (user == null || !passwd.equals(user.getPasswd())) {
            request.setAttribute("errMsg", "用户名或密码错误");
            request.setAttribute("account", account);
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("loginUser", user);
            response.sendRedirect("user_page"); // 跳转到用户主页
        }
    }

    /**
     * Get请求转到login.jsp
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("baiduAuthUrl", OAuthHelper.getInfo("baidu").getAuthUrl());
        request.setAttribute("renrenAuthUrl", OAuthHelper.getInfo("renren").getAuthUrl());

        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

}
