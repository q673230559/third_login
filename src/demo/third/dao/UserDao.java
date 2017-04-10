package demo.third.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import demo.third.model.User;
import demo.third.util.DBHelper;
import demo.third.util.DataHandler;

/**
 * 用户数据操作工具
 * 
 * @author Administrator
 *
 */
public class UserDao {

    private UserDao() {
    }

    /**
     * 从结果集中取出第一个user的处理器
     * 
     * @author Administrator
     *
     */
    private static class GetFirstUserDataHandler implements DataHandler {

        @Override
        public Object handle(ResultSet rs) throws SQLException {
            User user = null;
            if (rs.next()) {
                user = dataToUser(rs);
            }
            return user;
        }

    }

    /*
     * resultset 到 user映射
     * 
     * @param rs
     * 
     * @return
     * 
     * @throws SQLException
     */
    private static User dataToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setAccount(rs.getString("account"));
        user.setPasswd(rs.getString("passwd"));
        user.setName(rs.getString("name"));
        return user;
    }

    /**
     * 按account查询user
     * 
     * @param account
     * @return
     */
    public static User findUserByAccount(String account) {

        String sql = "select * from user where account=?";
        Object[] params = new Object[] { account };

        return (User) DBHelper.query(sql, params, new UserDao.GetFirstUserDataHandler());
    }

    /**
     * 按third_id查找user
     * 
     * @param thirdId
     * @return
     */
    public static User findUserByThirdId(String thirdId) {
        String sql = "select * from user where thirdId=?";
        Object[] params = new Object[] { thirdId };

        return (User) DBHelper.query(sql, params, new UserDao.GetFirstUserDataHandler());
    }

    /**
     * 更新user name字段
     * 
     * @param persistenceUser
     */
    public static void updateName(User user) {
        String sql = "update user set name=? where id=?";
        Object[] params = new Object[] { user.getName(), user.getId() };

        DBHelper.execute(sql, params);
    }

    public static User insertUser(User user) {
        String sql = "insert into user (account, thirdId, passwd, name) values(?,?,?,?)";
        Object[] params = new Object[] { user.getAccount(), user.getThirdId(), user.getPasswd(), user.getName() };

        DBHelper.execute(sql, params);

        // 取回已插入user，这个user会包含数据库自增生成的id值
        if (user.getAccount() != null) {
            user = findUserByAccount(user.getAccount());
        } else if (user.getThirdId() != null) {
            user = findUserByThirdId(user.getThirdId());
        }

        return user;
    }

}
