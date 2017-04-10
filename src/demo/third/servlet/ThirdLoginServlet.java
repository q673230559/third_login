package demo.third.servlet;

import java.io.File;
import java.io.FileOutputStream;
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
 * 第三方授权后回调的路径，完成后续信息获取
 */
@WebServlet("/third_login/*")
public class ThirdLoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String plat = getPlatName(request);
        String code = request.getParameter("code");
        
        System.out.println(plat);
        System.out.println(code);
        
        User user = OAuthHelper.getUserInfo(plat, code);
        //System.out.println(user.toString());
        
/*        if (user != null) {
        	System.out.println(user.toString());
        	User persistenceUser = user;
            // 写头像文件
            persistenceUser.setPortraitData(user.getPortraitData());
            String basePath = request.getServletContext().getRealPath("/portrait");
            savePortrait(basePath + File.separator + persistenceUser.getId(), persistenceUser);
            System.out.println(basePath);
            
            // 将用户对象存入session，后结操作可根据session中user信息判断是否登录及登录人信息
            request.getSession().setAttribute("loginUser", persistenceUser);

            // 用户信息存入session，跳转到用户页
            toSuccessPage(plat, request, response);
            
            User persistenceUser = UserDao.findUserByThirdId(user.getThirdId());
            // 先判断third_id对应用户是否已存在
            if (persistenceUser != null) {
                // 存在表示曾经登录过，更新信息
                persistenceUser.setName(user.getName());
                UserDao.updateName(persistenceUser);// 更新姓名（昵称）
            } else {
                // 不存在表示实效使用此第三方账号登录本系统，先将用户信息写入，相当于注册的过程
                persistenceUser = UserDao.insertUser(user);
            }

            // 写头像文件
            persistenceUser.setPortraitData(user.getPortraitData());
            String basePath = request.getServletContext().getRealPath("/portrait");
            savePortrait(basePath + File.separator + persistenceUser.getId(), persistenceUser);

            // 将用户对象存入session，后结操作可根据session中user信息判断是否登录及登录人信息
            request.getSession().setAttribute("loginUser", persistenceUser);

            // 用户信息存入session，跳转到用户页
            toSuccessPage(plat, request, response);
        } else {
            // 授权过程出现错误，进入错误页
            toErrorPage(plat, request, response);
        }*/
    }

    // 授权成功后的跳转，因为演示程序第三方授权页有几种不同打开方式，成功页也不同，所以在这里根据不同平台分别处理
    private void toSuccessPage(String plat, HttpServletRequest request, HttpServletResponse response) throws IOException {
        switch (plat) {
        case "baidu": // 百度登录使用的是本页面跳转方式，授权成功后直接跳入user页
            response.sendRedirect(request.getServletContext().getContextPath() + "/user_page");
            break;
        case "renren": // 人人使用弹窗，授权成功后跳入临时页，临时页通过脚本与主页面通信
            response.sendRedirect(request.getServletContext().getContextPath() + "/auth_success?type=window");
            break;
        }
    }
    
    // 授权过程出现错误时的跳转
    private void toErrorPage(String plat, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        switch (plat) {
        case "baidu":
            request.setAttribute("type", "self");
            break;
        case "renren": 
            request.setAttribute("type", "window");
            break;
        }
        request.setAttribute("errMsg", "授权时发生系统错误或操作被取消，请稍候再试！");
        request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
    }

    // 写头像文件
    private void savePortrait(String fileName, User persistenceUser) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            fos.write(persistenceUser.getPortraitData());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    // 解析uri获取当前平台名
    private String getPlatName(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.substring(uri.lastIndexOf("/") + 1);
    }

}
