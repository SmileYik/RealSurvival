package test;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.github.miskyle.mcpt.mysql.MySQLManager;

public class TTest {
	@Test
	public void a() {
		MySQLManager.setupMySQl("127.0.0.1", 3306, "test", "root", "");
		System.out.println(MySQLManager.connect());
		try {
			System.out.println(
					MySQLManager.execute("show tables like 'realsurvival'").executeQuery().next()
					);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
