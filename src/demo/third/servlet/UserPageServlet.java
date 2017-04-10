package demo.third.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 跳转到user页
 */
@WebServlet("/user_page")
public class UserPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 判断用户是否登录，未登录用户不允许进入，跳回登录页
	    if (request.getSession().getAttribute("loginUser") == null) {
	        response.sendRedirect("login");
	    } else {
	        request.getRequestDispatcher("/WEB-INF/jsp/user.jsp").forward(request, response);
	    }
	}

}
