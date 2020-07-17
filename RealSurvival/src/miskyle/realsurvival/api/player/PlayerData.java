package miskyle.realsurvival.api.player;

import org.bukkit.plugin.Plugin;

import miskyle.realsurvival.api.status.StatusType;

public interface PlayerData {
	/**
	 * \u4fee\u6539\u4e00\u4e2a\u5c5e\u6027\u7684\u503c
	 * @param type \u5c5e\u6027\u7c7b\u578b
	 * @param value \u503c(\u53ef\u4ee5\u4e3a\u8d1f\u6570)
	 */
	public void modify(StatusType type,double value);
	/**
	 * \u4fee\u6539\u4e00\u4e2a\u5c5e\u6027\u7684\u503c,\u4fee\u6539\u7684\u503c\u53d7\u73a9\u5bb6\u73b0\u6709\u6548\u679c\u5f71\u54cd
	 * @param type \u5c5e\u6027\u7c7b\u578b
	 * @param value \u503c(\u53ef\u4ee5\u4e3a\u8d1f\u6570)
	 */
	public void modifyWithEffect(StatusType type,double value);
	/**
	 * \u8bbe\u5b9a\u73a9\u5bb6\u67d0\u4e2a\u5c5e\u6027\u7684\u503c
	 * @param type \u5c5e\u6027\u7c7b\u578b
	 * @param value \u503c(\u4e0d\u53ef\u4e3a\u8d1f\u6570)
	 */
	public void setStatus(StatusType type,double value);
	/**
	 * \u83b7\u53d6\u67d0\u4e00\u5c5e\u6027\u5f53\u524d\u7684\u503c
	 * @param type \u5c5e\u6027\u7c7b\u578b
	 * @return \u5f53\u524d\u6b64\u5c5e\u6027\u5bf9\u5e94\u7684\u503c
	 */
	public double getStatusValue(StatusType type);
	/**
	 * \u83b7\u53d6\u67d0\u4e00\u5c5e\u6027\u6700\u5927\u503c(\u6709\u4e2a\u4f53\u5dee\u5f02)
	 * @param type \u5c5e\u6027\u7c7b\u578b
	 * @return \u5bf9\u5e94\u5c5e\u6027\u6700\u5927\u503c
	 */
	public double getStatusMaxValue(StatusType type);
	/**
	 * \u589e\u52a0(\u6216\u91cd\u7f6e)\u4e00\u4e2a\u6548\u679c
	 * @param type \u5c5e\u6027\u7c7b\u578b
	 * @param pluginName \u6548\u679c\u540d(\u5efa\u8bae\u4f7f\u7528\u76f8\u5bf9\u5e94\u7c7b\u540d\u505a\u6548\u679c\u540d)
	 * @param value \u6548\u679c\u503c(\u8303\u56f40~1)
	 * @param duration \u6301\u7eed\u65f6\u95f4(\u5355\u4f4d: \u79d2)
	 */
	public void addEffect(StatusType type, String pluginName,double value,int duration);
	/**
	 * 增加玩家属性最大值(可为负值)<p>
	 * 每个插件对每个属性最大值添加效果仅最后一次有效
	 * @param type
	 * @param plugin
	 * @param value
	 */
	public void addStatusMaxValue(StatusType type,Plugin plugin,double value);
	
	/**
	 * 检测插件添加的属性是否有效
	 * @param type
	 * @param plugin
	 * @return 有效返回true
	 */
	public boolean isValidAddMaxValue(StatusType type,Plugin plugin);
	
	/**
	 * 获取插件所添加的属性最大值
	 * @param type
	 * @param plugin
	 * @return 对应属性最大值
	 */
	public double getAddMaxValue(StatusType type,Plugin plugin);
}
