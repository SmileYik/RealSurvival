package test;

import org.bukkit.potion.PotionEffectType;

public class Test {
  static {
    System.out.println(1.6%1);
    for(PotionEffectType type : PotionEffectType.values()) {
      System.out.println(type);
    }
  }
	@org.junit.Test
	public void a() {
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
}
