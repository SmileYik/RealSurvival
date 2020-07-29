package app.miskyle.realsurvival;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import org.bukkit.Material;

import miskyle.realsurvival.data.ConfigManager;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

public class MinecraftItemModifier {

  private JFrame frmRealsurvivalMinecraft;
  private JTextField maxValue;
  private JTextField minValue;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          MinecraftItemModifier window = new MinecraftItemModifier();
          window.frmRealsurvivalMinecraft.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public MinecraftItemModifier() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frmRealsurvivalMinecraft = new JFrame();
    frmRealsurvivalMinecraft.setTitle("RealSurvival - Minecraft Item Modifier");
    frmRealsurvivalMinecraft.setBounds(100, 100, 565, 765);
    frmRealsurvivalMinecraft.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frmRealsurvivalMinecraft.getContentPane().setLayout(null);
    
    JComboBox<String> item = new JComboBox<>();
    setUpItemList(item);
    item.setBounds(14, 13, 519, 38);
    frmRealsurvivalMinecraft.getContentPane().add(item);
    
    JPanel statusPanel = new JPanel();
    statusPanel.setBounds(14, 79, 519, 81);
    frmRealsurvivalMinecraft.getContentPane().add(statusPanel);
    statusPanel.setLayout(null);
    
    JLabel lblNewLabel = new JLabel("Status: ");
    lblNewLabel.setBounds(14, 13, 72, 18);
    statusPanel.add(lblNewLabel);
    
    JComboBox<String> statusComboBox = new JComboBox<>();
    statusComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"sleep", "thirst", "energy", "hunger", "health", "temperature", "weight"}));
    statusComboBox.setBounds(84, 10, 161, 24);
    statusPanel.add(statusComboBox);
    
    JCheckBox maxCheckBox = new JCheckBox("是否百分比");
    maxCheckBox.setBounds(255, 9, 133, 27);
    statusPanel.add(maxCheckBox);
    
    JLabel lblNewLabel_1 = new JLabel("Max-Value: ");
    lblNewLabel_1.setBounds(14, 44, 88, 18);
    statusPanel.add(lblNewLabel_1);
    
    maxValue = new JTextField();
    maxValue.setBounds(105, 41, 140, 24);
    statusPanel.add(maxValue);
    maxValue.setColumns(10);
    
    minValue = new JTextField();
    minValue.setColumns(10);
    minValue.setBounds(342, 41, 163, 24);
    statusPanel.add(minValue);
    
    JLabel lblNewLabel_1_1 = new JLabel("Min-Value: ");
    lblNewLabel_1_1.setBounds(255, 44, 88, 18);
    statusPanel.add(lblNewLabel_1_1);
  }
  
  public void setUpItemList(JComboBox<String> box) {
    String[] ITEM_LIST;
      if(ConfigManager.getBukkitVersion()>=13) {
          ITEM_LIST =
                  ("POTION,MILK_BUCKET,APPLE,MUSHROOM_STEW,"
                  + "BREAD,PORKCHOP,COOKED_PORKCHOP,GOLDEN_APPLE,"
                  + "ENCHANTED_GOLDEN_APPLE,COD,SALMON,BEEF,DRIED_KELP,"
                  + "MELON_SLICE,COOKIE,POISONOUS_POTATO,COOKED_SALMON,"
                  + "COOKED_COD,PUFFERFISH,TROPICAL_FISH,BAKED_POTATO,"
                  + "POTATO,CARROT,SPIDER_EYE,ROTTEN_FLESH,COOKED_CHICKEN,"
                  + "CHICKEN,COOKED_BEEF,PUMPKIN_PIE,COOKED_RABBIT,RABBIT_STEW,"
                  + "MUTTON,COOKED_MUTTON,BEETROOT,BEETROOT_SOUP,"
                  + "GOLDEN_CARROT,RABBIT").split(",");
      }else {
          ITEM_LIST = 
                  ("SPECKLED_MELON FERMENTED_SPIDER_EYE RABBIT RABBIT_FOOT "
                  + "POISONOUS_POTATO BAKED_POTATO POTATO_ITEM CARROT_ITEM "
                  + "ROTTEN_FLESH SPIDER_EYE GOLDEN_APPLE GOLDEN_APPLE RAW_FISH "
                  + "RAW_FISH GRILLED_PORK PORK BREAD MUSHROOM_SOUP APPLE "
                  + "RAW_FISH RAW_FISH COOKED_FISH COOKED_FISH MELON COOKIE "
                  + "COOKED_BEEF RAW_CHICKEN RAW_BEEF PUMPKIN_PIE COOKED_RABBIT "
                  + "RABBIT_STEW MUTTON BEETROOT BEETROOT_SOUP MILK_BUCKET "
                  + "POTION COOKED_CHICKEN").split(" ");
      }
      box.setModel(new DefaultComboBoxModel<String>(ITEM_LIST));
  }
}
