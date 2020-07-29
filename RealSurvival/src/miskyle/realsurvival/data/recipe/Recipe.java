package miskyle.realsurvival.data.recipe;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public class Recipe {
  
  private String recipeName;
  private String machineName;
  private String materialShape;
  private RecipeType type;
  private int forgeTime;
  
  private HashMap<Character, ItemStack> materials;
  private ArrayList<ItemStack> products;
  
  public Recipe(String recipeName, String machineName, String materialShape, RecipeType type, int forgeTime,
      HashMap<Character, ItemStack> materials, ArrayList<ItemStack> products) {
    this.recipeName = recipeName;
    this.machineName = machineName;
    this.materialShape = materialShape;
    this.type = type;
    this.forgeTime = forgeTime;
    this.materials = materials;
    this.products = products;
  }
  
  public String getRecipeName() {
    return recipeName;
  }
  public void setRecipeName(String recipeName) {
    this.recipeName = recipeName;
  }
  public String getMachineName() {
    return machineName;
  }
  public void setMachineName(String machineName) {
    this.machineName = machineName;
  }
  public String getMaterialShape() {
    return materialShape;
  }
  public void setMaterialShape(String materialShape) {
    this.materialShape = materialShape;
  }
  public RecipeType getType() {
    return type;
  }
  public void setType(RecipeType type) {
    this.type = type;
  }
  public int getForgeTime() {
    return forgeTime;
  }
  public void setForgeTime(int forgeTime) {
    this.forgeTime = forgeTime;
  }
  public HashMap<Character, ItemStack> getMaterials() {
    return materials;
  }
  public void setMaterials(HashMap<Character, ItemStack> materials) {
    this.materials = materials;
  }
  public ArrayList<ItemStack> getProducts() {
    return products;
  }
  public void setProducts(ArrayList<ItemStack> products) {
    this.products = products;
  }
}
