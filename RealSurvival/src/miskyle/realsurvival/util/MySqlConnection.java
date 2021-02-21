package miskyle.realsurvival.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlConnection {
  private static String url;
  private static String userName;
  private static String password;
  
  private final Connection mysql;
  
  private MySqlConnection(Connection mysql) {
    this.mysql = mysql;
  }
  
  /**
   * 初始化MySQL信息.

   * @param url mysql的url.
   * @param userName 用户名.
   * @param password 密码.
   */
  public static void init(String url, String userName, String password) {
    MySqlConnection.url = url;
    MySqlConnection.password = password;
    MySqlConnection.userName = userName;
  }
  
  /**
   * 获取MySQL连接.

   * @return 返回的实例需要用isValid方法检测是否有效.
   */
  public static MySqlConnection getConnection() {
    Connection mysql = null;
    try {
      mysql = DriverManager.getConnection(url, userName, password);  
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return new MySqlConnection(mysql);
  }
  
  /**
   * 判断该连接是否有效.

   * @return 如果有效则返回true.
   */
  public boolean isValid() {
    return mysql != null;
  }
  
  /**
   * 执行SQL语句.

   * @param sql sql语句
   * @return PreparedStatement
   */
  public PreparedStatement execute(String sql) {
    try {
      return mysql.prepareStatement(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * 释放链接.

   * @param ps PreparedStatement 可以填null
   * @param rs ResultSet 可以填null
   */
  public void free(PreparedStatement ps, ResultSet rs) {
    if (ps != null) {
      try {
        ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (mysql != null) {
      try {
        mysql.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
  
}
