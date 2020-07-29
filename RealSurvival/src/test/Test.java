package test;

import java.sql.SQLException;

import org.bukkit.potion.PotionEffectType;

import com.github.miskyle.mcpt.mysql.MySQLManager;

public class Test {
  static {
    System.out.println(1.6%1);
    for(PotionEffectType type : PotionEffectType.values()) {
      System.out.println(type);
    }
  }
	@org.junit.Test
	public void a() {
	  v();
		//34
		//42
		double b = 45;//Math.random()*100;
		double a = -b*Math.sin(0.85*0.15);
		System.out.println(b+"/"+a);
		for(double d = -0.5;d<=2;d+=0.05) {
			System.out.println(_2f(d)+":"+_2f(b*Math.sin(0.85*d)+a));
		}
	}
	
	private String _2f(double d){
		return String.format("%.2f", d);
	}
	
	public void v() {
	  MySQLManager.setupMySQl("127.0.0.1", 3306, "test", "root", "");
      if(MySQLManager.connect()) {
        try {
            if(!MySQLManager.execute("show tables like 'realsurvival'")
                    .executeQuery().next()) {
                System.out.println("2");
                MySQLManager.execute("create table RealSurvival(\r\n" + 
                        "Name VARCHAR(50),\r\n" + 
                        "Sleep DOUBLE,\r\n" + 
                        "Thirst DOUBLE,\r\n" + 
                        "Energy DOUBLE,\r\n" + 
                        "Weight DOUBLE,\r\n" + 
                        "ExtraSleepValue TEXT,\r\n" + 
                        "ExtraThirstValue TEXT,\r\n" + 
                        "ExtraEnergyValue TEXT,\r\n" + 
                        "ExtraWeightValue TEXT,\r\n" + 
                        "Effect TEXT,\r\n" + 
                        "TEffect TEXT,\r\n" + 
                        "Disease TEXT,\r\n" + 
                        "Temperature TEXT\r\n" + 
                        ")default charset=utf8;").execute();
            }else {
              System.out.println(1);
            }
        } catch (SQLException e) {
            MySQLManager.disconnect();
            e.printStackTrace();
        }
        MySQLManager.disconnect();
      }
	}
}
