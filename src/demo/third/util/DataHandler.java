package demo.third.util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据处理器，处理方法中完成对数据库结果对象的逻辑处理
 * 
 * @author Administrator
 *
 */
public interface DataHandler {

    /**
     * 处理结果集
     * 
     * @param rs
     */
    public Object handle(ResultSet rs) throws SQLException;
}
