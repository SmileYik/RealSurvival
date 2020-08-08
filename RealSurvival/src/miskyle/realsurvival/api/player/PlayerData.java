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
     * \u589E\u52A0\u73A9\u5BB6\u5C5E\u6027\u6700\u5927\u503C(\u53EF\u4E3A\u8D1F\u503C)
     * <p>
     * \u6BCF\u4E2A\u63D2\u4EF6\u5BF9\u6BCF\u4E2A\u5C5E\u6027\u6700\u5927\u503C\u6DFB\u52A0\u6548\u679C\u4EC5\u6700\u540E\u4E00\u6B21\u6709\u6548
	 * @param type
	 * @param plugin
	 * @param value
	 */
	public void addStatusMaxValue(StatusType type,Plugin plugin,double value);
	
	/**
     * \u68C0\u6D4B\u63D2\u4EF6\u6DFB\u52A0\u7684\u5C5E\u6027\u662F\u5426\u6709\u6548
	 * @param type
	 * @param plugin
     * @return \u6709\u6548\u8FD4\u56DEtrue
	 */
	public boolean isValidAddMaxValue(StatusType type,Plugin plugin);
	
	/**
     * \u83B7\u53D6\u63D2\u4EF6\u6240\u6DFB\u52A0\u7684\u5C5E\u6027\u6700\u5927\u503C
	 * @param type
	 * @param plugin
     * @return \u5BF9\u5E94\u5C5E\u6027\u6700\u5927\u503C
	 */
	public double getAddMaxValue(StatusType type,Plugin plugin);
}
