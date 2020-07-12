package test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.junit.Test;

import com.github.miskyle.mcpt.mysql.MySQLManager;
import com.sun.org.apache.xpath.internal.functions.FunctionDef1Arg;

import miskyle.realsurvival.data.effect.RSEffect;
import miskyle.realsurvival.effect.RSEffectHealthDamage;
import miskyle.realsurvival.util.RSClassLoader;

public class TTest {
	@Test
	public void a() {
		try {
			RSClassLoader loader = new RSClassLoader("E:\\Users\\MiSkYle\\Documents\\GitHub\\RealSurvival\\RealSurvival\\bin\\");
			for(Class<?> c : loader.loadAllClass(RSEffect.class).values()) {
				System.out.println(c);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
