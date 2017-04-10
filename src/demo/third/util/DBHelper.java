package demo.third.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据操作辅助工具
 * 
 * @author Administrator
 *
 */
public class DBHelper {

    private DBHelper() {
    }

    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/third_login?useUnicode=true&characterEncoding=utf8";
    private static final String clasName = "com.mysql.jdbc.Driver";
    private static final String username = "root";
    private static final String passwd = "test123";

    /**
     * 获取数据库连接
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(clasName);
        return DriverManager.getConnection(jdbcUrl, username, passwd);
    }

    /**
     * 查询方法
     * 
     * @param sql
     * @param params
     * @param handler
     */
    public static Object query(String sql, Object[] params, DataHandler handler) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);

            if (params != null) {
                for (int i = 0; i < params.length; ++i) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            rs = ps.executeQuery();

            return handler.handle(rs);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /**
     * 执行写操作
     * 
     * @param sql
     * @param params
     */
    public static void execute(String sql, Object[] params) {

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);

            if (params != null) {
                for (int i = 0; i < params.length; ++i) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            ps.execute();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}
