package miskyle.realsurvival.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.configuration.ConfigurationSection;
import com.github.miskyle.mcpt.i18n.I18N;
import miskyle.realsurvival.RealSurvival;
import miskyle.realsurvival.util.MySqlConnection;

public class MySqlManager {
  private static String tableName;
  
  /**
   * 初始化MySQLManager.

   * @param config 配置片段.
   */
  public MySqlManager(ConfigurationSection config) {
    MySqlManager.tableName = config.getString("table-name");
    MySqlConnection.init(
        config.getString("url"),
        config.getString("username"),
        config.getString("password"));
  }
  
  /**
   * 判断表是否存在.

   * @return 
   */
  public static boolean isTableExist() {
    String sql = String.format("SHOW TABLES LIKE '%s'", tableName);
    MySqlConnection connection = MySqlConnection.getConnection();
    if (!connection.isValid()) {
      RealSurvival.logger().severe(I18N.tr("log.severe.mysql.not-valid"));
      return false;
    }
    try {
      return connection.execute(sql).executeQuery().next();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      connection.free(null, null);
    }
    return false;
  }
  
  /**
   * 执行无返回ResultSet的sql语句.

   * @param sql sql语句
   * @return 执行成功或者失败.
   */
  public static boolean execute(String sql) {
    MySqlConnection connection = MySqlConnection.getConnection();
    if (!connection.isValid()) {
      RealSurvival.logger().severe(I18N.tr("log.severe.mysql.not-valid"));
      return false;
    }
    try {
      return connection.execute(sql).execute();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      connection.free(null, null);
    }
    return false;
  }
  
  /**
   * 执行返回ResultSet的sql语句.

   * @param connection 对应连接.
   * @param sql sql语句.
   * @return 返回的ResultSet, 失败返回null
   */
  private static ResultSet execute(MySqlConnection connection, String sql) {
    if (!connection.isValid()) {
      RealSurvival.logger().severe(I18N.tr("log.severe.mysql.not-valid"));
      return null;
    }
    try {
      return connection.execute(sql).executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * 创建一个默认空表.
   */
  public static boolean createDefaultTable() {
    String sql = "CREATE TABLE %s ("
            + "player VARCHAR(50), "
            + "data LONGTEXT)DEFAULT CHARSET=UTF8;";
    sql = String.format(sql, tableName);
    return execute(sql);
  }
  
  /**
   * 向表中插入某条玩家数据.

   * @param playerName 玩家名
   * @param data 玩家数据
   */
  public static void insertItem(String playerName, String data) {
    String sql = "INSERT INTO `%s` (`player`, `data`) VALUES ('%s', \"%s\")";
    sql = String.format(sql, tableName, playerName, data.replaceAll("\"", "\\\""));
    execute(sql);
  }

  /**
   * 更新表中某个玩家的数据.

   * @param playerName 玩家名
   * @param data 玩家数据
   */
  public static void updateItem(String playerName, String data) {
    String sql = "UPDATE `%s` SET `data`=\"%s\" WHERE `player`='%s'";
    sql = String.format(sql, tableName, data.replaceAll("\"", "\\\""), playerName);
    execute(sql);
  }
  
  /**
   * 获取玩家数据.

   * @param playerName 玩家名
   * @return 若玩家数据存在则返回储存的数据, 否则返回false
   */
  public static String getPlayerData(String playerName) {
    String sql = "SELECT * FROM `%s` WHERE `player`='%s'";
    sql = String.format(sql, tableName, playerName);
    MySqlConnection connection = MySqlConnection.getConnection();
    if (!connection.isValid()) {
      RealSurvival.logger().severe(I18N.tr("log.severe.mysql.not-valid"));
      return null;
    }
    
    ResultSet rs = null;
    try {
      rs = execute(connection, sql);
      if (rs.next()) {
        return rs.getString("data");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      connection.free(null, rs);
    }
    return null;
  }
}
