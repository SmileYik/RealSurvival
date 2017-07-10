package com.outlook.z815007320.commands;

public class HelpText {
	private static final String helpHead="§9===================§bRealSurvival§9===================\n%text%"
			+ "§9=================================================";
	public static String getMainHelpText(){
		return helpHead.replaceAll("%text%",
				 "§b作者: §3§lSchool_Uniform\n"
				 + "\n"
				+ "§b/rs state - 查看自身状态\n"
				+ "§b/rs reload - 重载插件配置\n"
				+ "§b/rs item help - 查看与物品有关的子指令\n"
				+ "§b/rs wb help - 查看与工作台相关的子指令\n"
				+ "§b/rs state help - 查看与状态相关的子指令\n"
				+ "§b/rs setSwitch help - 查看与插件功能相关的子指令\n"
				+ "§b/rs test help - 查看测试指令帮助\n");
	}
	
	public static String getWorkbenchHelp(){
		return helpHead.replaceAll("%text%",
				   "§b/rs wb csf <配方名> <制造时间> - 创建一个指定名称与制造时间的配方\n"
				+ "§b/rs wb csff <配方名> <制造时间> <保存时间> <温度> - 创建一个指定名称与制造时间的烧东西的配方\n"
				+ "§b/rs wb resetsf <配方名> <制造时间> - 重写指定配方及制造时间\n"
				+ "§b/rs wb openwb <ID> - 打开一个指定ID的工作台\n"
				+ "§b/rs wb openSFList - 打开所有配方的列表\n"
				+ "§b/rs wb setBlock <方位> <方块名> - 设定组件工作台所需方块①\n"
				+ "§3①: 方位为: Main, Right, UP, Down, Front, Behind\n"
				+ "§3    方块名需要全部大写 需要注意的是方位的大小写\n"
				+ "§c§l注意setBlock修改完后需要重载插件才有效哦!\n"
				+ "§c§lcsff中,保存时间要大于制造时间,否则...对了,温度可以为一切实数\n");
	}
	/**
	 * 获取SetSwitch指令的帮助文本
	 * @return SetSwitch指令的帮助文本
	 */
	public static String getSetSwitchHelp(){
		return helpHead.replaceAll("%text%", 
				"§b/rs setSwitch Sleep <true/false> - 开启/关闭睡觉功能\n"
			+ "§b/rs setSwitch Thirst <true/false> - 开启/关闭口渴功能\n"
			+ "§b/rs setSwitch PhysicalStrength <true/false> - 开启/关闭体力功能\n"
			+ "§b/rs setSwitch Temperature <true/false> - 开启/关闭体温功能\n"
			+ "§b/rs setSwitch Weight <true/false> - 开启/关闭负重功能\n"
			+ "§b/rs setSwitch Sick <true/false> - 开启/关闭生病功能\n"
			+ "§3 注意大小写!"
			+ "§c§l注意修改完后需要重载插件才有效哦!\n");
	}
	/**
	 * 获取 state指令的帮助文本
	 * @return
	 */
	public static String getStateHelp(){
		return helpHead.replaceAll("%text%", 
				"§b/rs state add <数据> <玩家名> <数值> - 增加某一数据①的数值\n"
			+ "§b/rs state set <数据> <玩家名> <数值> - 设定某一数据①的数值\n"
			+ "§b/rs state set sick <玩家名> <true/false> <病种> - 设定某玩家的生病情况\n"
			+ "§3①: 数据为: sleep, thirst, tem, ps, recovery\n"
			+ "§3   分别代表 睡眠, 口渴, 体温, 体力, 病情恢复进度\n"
			+ "§3 数值为数字,并且只能给在线玩家修改数值\n");
	}
	
	public static String getItemHelp(){
		return helpHead.replaceAll("%text%", 
			   "§b/rs item lore <描述> -  给手上的物品增加一行 <描述>\n"
			+ "§b/rs item setlore <行数> <描述> -  将手上指定行的描述修改为新<描述>\n"
			+ "§b/rs item removelore <行数> - 移除手上物品指定行的lore\n"
			+ "§b/rs item setname <name> -  将手上的物品的名字修改为指定的名字\n"
			+ "§b/rs item get <物品名> [数量] - 获取保存了的物品或内置物品①  [数量] 可选填 \n"
			+ "§b/rs item give  <玩家名> <物品名> [数量]  -  给指定玩家保存了的物品或内置物品 ①  [数量]可选填\n"
			+ "§b/rs item save <物品名>  -  将手上的物品保存为一个指定名称的本地文件\n"
			+ "§b/rs item list - 列出现有的所有物品\n");
	}
	
	public static String getTestHelp(){
		return helpHead.replaceAll("%text%", 
			   "§b/rs test open -  开启测试模式\n"
			+ "§b/rs test close -  关闭测试模式\n"
			+ "§b/rs test on - 让自身数值开始流动\n"
			+ "§b/rs test off - 让自身数值停止\n"
			+ "§3§lTips: 当使用 §copen/on §3§l指令时, 效果一直伴随到使用 §cclose/off §3§l才停止\n");
	}
}
