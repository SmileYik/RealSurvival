package miskyle.realsurvival.util.watermaker;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.miskyle.mcpt.i18n.I18N;

import miskyle.realsurvival.data.ItemManager;
import miskyle.realsurvival.data.item.RSItem;
import miskyle.realsurvival.data.item.RSItemData;

public class WaterMakerVer {
	public static void makeUnknownWater() {
		ItemStack item = new ItemStack(Material.POTION);
		ItemMeta im = item.getItemMeta();
		RSItemData data = new RSItemData();
		im.setDisplayName(I18N.tr("status.thirst.water.unknown-biome"));
		im.setLore(Arrays.asList(
				I18N.tr("status.thirst.water.unknown-biome.des").split(";")));
		item.setItemMeta(im);
		data.setStatusValue("THIRST", 10, 20);
		data.save("water/unkonwn");
		ItemStack is = ItemManager.getNBT().setNBT(item, "RSNBT", "water/unkonwn");
		new RSItem(is).save("water/unkonwn");
	}
}
