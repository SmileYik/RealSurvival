package com.outlook.schooluniformsama.gui.rsmaker;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.I18n;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.WorkbenchShape;
import com.outlook.schooluniformsama.data.item.RSItem;
import com.outlook.schooluniformsama.data.recipes.FurnaceRecipe;
import com.outlook.schooluniformsama.data.recipes.WorkbenchRecipe;
import com.outlook.schooluniformsama.data.recipes.WorkbenchType;
import com.outlook.schooluniformsama.gui.Furnace;
import com.outlook.schooluniformsama.gui.Workbench;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextArea;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.ImageIcon;

public class RealSurvivalRecipeMaker extends JFrame{
	
	class Item{
		String itemName;
		int amount;
		Item(String a,int b){
			itemName=a;
			amount=b;
		}
		@Override
		public String toString() {
			return itemName+";"+I18n.tr("GUIKeyWordamount")+":"+amount;
		}
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3544689930201486846L;
	private JFrame window;
	private JTextField recipe_name;
	private JTextField recipe_workbench_type;
	private JTextField recipe_need_time;
	private JTextField recipe_furnace_save_time;
	private JTextField recipe_furnace_min_tem;
	private JTextField recipe_furnace_max_tem;
	private HashMap<Integer,Item> p = new HashMap<>(),m=new HashMap<>();
	private JTextField recipe_item_amount;
	
	public static void showGUI(){
		new RealSurvivalRecipeMaker().setVisible(true);
	}

	/**
	 * Create the application.
	 */
	public RealSurvivalRecipeMaker() {
		window = this;
		initialize();
	}
	
	private DefaultMutableTreeNode getItemsDir(File items){
		DefaultMutableTreeNode dtm = new DefaultMutableTreeNode(items.getName());
		if(!items.exists()){
			items.mkdirs();
			return dtm;
		}
		for(File item:items.listFiles()){
			if(item.isDirectory())
				dtm.add(getItemsDir(item));
			if(item.isFile()&&item.getName().substring(item.getName().lastIndexOf(".")+1).equalsIgnoreCase("yml"))
				dtm.add(new DefaultMutableTreeNode(item.getName().substring(0,item.getName().lastIndexOf("."))));
		}
		return dtm;
	}
	
	private DefaultComboBoxModel<Material> getItemType(){
		return new DefaultComboBoxModel<>(Material.values());
	}
	
	private DefaultComboBoxModel<String> getWorkbenchType(){
		return new DefaultComboBoxModel<>(Data.workbenchs.keySet().toArray(new String[Data.workbenchs.size()]));
	}
	
	private void setWorkbenchSlot(JComboBox<String> box,WorkbenchType type){
		LinkedList<String> slot = new LinkedList<>();
		if(type == WorkbenchType.FURNACE){
			for(int i=0;i<Furnace.mSlot.size();i++){
				m.put(i, null);
				slot.add(I18n.tr("GUIKeyWordmaterials")+":"+(i+1));
			}
			for(int i=0;i<Furnace.pSlot.size();i++){
				p.put(i, null);
				slot.add(I18n.tr("GUIKeyWordproducts")+":"+(i+1));
			}
			
		}else{
			for(int i=0;i<Workbench.materials.size();i++){
				m.put(i, null);
				slot.add(I18n.tr("GUIKeyWordmaterials")+":"+(i+1));
			}
			for(int i=0;i<Workbench.products.size();i++){
				p.put(i, null);				
				slot.add(I18n.tr("GUIKeyWordproducts")+":"+(i+1));
			}
		}
		box.setModel( new DefaultComboBoxModel<>(slot.toArray(new String[slot.size()])));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setResizable(false);
		this.setTitle("RealSurvivalRecipeMaker");
		//window.setBounds(100, 100, 675, 416);
		//this.setBounds(100, 100, 155, 416);
		this.setBounds(100, 100, 500, 672);
		this.getContentPane().setLayout(null);
		
		JButton create_new_item = new JButton(I18n.tr("guiButtoncreate"));
		create_new_item.setBounds(10, 376, 131, 23);
		this.getContentPane().add(create_new_item);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 10, 131, 346);
		getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{186, 192, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel.add(scrollPane, gbc_scrollPane);
		
		JTree recipe_tree = new JTree();
		scrollPane.setViewportView(recipe_tree);
		recipe_tree.setModel(new DefaultTreeModel(getItemsDir(new File(Data.DATAFOLDER+"/recipe"))));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 1;
		panel.add(scrollPane_1, gbc_scrollPane_1);
		
		JTree item_tree = new JTree();
		scrollPane_1.setViewportView(item_tree);
		item_tree.setModel(new DefaultTreeModel(getItemsDir(new File(Data.DATAFOLDER+"/items"))));
		
		JComboBox<String> workbench_type = new JComboBox<String>();
		workbench_type.setBounds(10, 355, 131, 21);
		workbench_type.setModel(getWorkbenchType());
		getContentPane().add(workbench_type);
		
		JButton refresh = new JButton(I18n.tr("guiButtonrefresh"));
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scrollPane_1.setViewportView(item_tree);
				item_tree.setModel(new DefaultTreeModel(getItemsDir(new File(Data.DATAFOLDER+"/items"))));
				recipe_tree.setModel(new DefaultTreeModel(getItemsDir(new File(Data.DATAFOLDER+"/recipe"))));
			}
		});
		refresh.setBounds(10, 398, 131, 23);
		getContentPane().add(refresh);
		
		JPanel setting = new JPanel();
		setting.setVisible(false);
		setting.setBounds(151, 10, 346, 411);
		getContentPane().add(setting);
		setting.setLayout(null);
		
		JLabel label = new JLabel(I18n.tr("guiLabelmaterials-editor")+": ");
		label.setBounds(185, 10, 137, 15);
		setting.add(label);
		
		JLabel label_1 = new JLabel(I18n.tr("guiLabelproducts-editor")+": ");
		label_1.setBounds(185, 207, 137, 15);
		setting.add(label_1);
		
		JLabel label_2 = new JLabel(I18n.tr("guiLabelrecipe-name")+": ");
		label_2.setBounds(10, 10, 67, 15);
		setting.add(label_2);
		
		recipe_name = new JTextField();
		recipe_name.setBounds(91, 7, 84, 21);
		setting.add(recipe_name);
		recipe_name.setColumns(10);
		
		JLabel label_3 = new JLabel(I18n.tr("guiLabelworkbench-type")+": ");
		label_3.setBounds(10, 35, 84, 15);
		setting.add(label_3);
		
		recipe_workbench_type = new JTextField();
		recipe_workbench_type.setEditable(false);
		recipe_workbench_type.setBounds(91, 32, 84, 21);
		setting.add(recipe_workbench_type);
		recipe_workbench_type.setColumns(10);
		
		JLabel label_4 = new JLabel(I18n.tr("guiLabelneed-time")+": ");
		label_4.setBounds(10, 63, 67, 15);
		setting.add(label_4);
		
		recipe_need_time = new JTextField();
		recipe_need_time.setColumns(10);
		recipe_need_time.setBounds(91, 60, 84, 21);
		setting.add(recipe_need_time);
		
		JPanel recipe_furnace_panel = new JPanel();
		recipe_furnace_panel.setBounds(10, 84, 165, 83);
		setting.add(recipe_furnace_panel);
		recipe_furnace_panel.setLayout(null);
		
		recipe_furnace_save_time = new JTextField();
		recipe_furnace_save_time.setColumns(10);
		recipe_furnace_save_time.setBounds(81, 0, 84, 21);
		recipe_furnace_panel.add(recipe_furnace_save_time);
		
		JLabel label_5 = new JLabel(I18n.tr("guiLabelsave-time")+": ");
		label_5.setBounds(0, 3, 67, 15);
		recipe_furnace_panel.add(label_5);
		
		recipe_furnace_min_tem = new JTextField();
		recipe_furnace_min_tem.setColumns(10);
		recipe_furnace_min_tem.setBounds(81, 27, 84, 21);
		recipe_furnace_panel.add(recipe_furnace_min_tem);
		
		JLabel label_6 = new JLabel(I18n.tr("guiLabelmin-temperature")+": ");
		label_6.setBounds(0, 30, 67, 15);
		recipe_furnace_panel.add(label_6);
		
		JLabel label_7 = new JLabel(I18n.tr("guiLabelmax-temperature")+": ");
		label_7.setBounds(0, 58, 67, 15);
		recipe_furnace_panel.add(label_7);
		
		recipe_furnace_max_tem = new JTextField();
		recipe_furnace_max_tem.setColumns(10);
		recipe_furnace_max_tem.setBounds(81, 55, 84, 21);
		recipe_furnace_panel.add(recipe_furnace_max_tem);
		
		JLabel label_8 = new JLabel(I18n.tr("guiLabeloriginal-item")+": ");
		label_8.setBounds(10, 177, 73, 15);
		setting.add(label_8);
		
		JComboBox<Material> minecraft_item = new JComboBox<Material>();
		minecraft_item.setEditable(true);
		minecraft_item.setBounds(91, 171, 84, 21);
		setting.add(minecraft_item);
		minecraft_item.setModel(getItemType());
		
		JComboBox<String> item_type = new JComboBox<String>();
		item_type.setModel(new DefaultComboBoxModel<String>(new String[] {I18n.tr("GUIKeyWordoriginal-item"), I18n.tr("GUIKeyWorddiy-item"), I18n.tr("GUIKeyWordnothing")}));
		item_type.setBounds(91, 201, 84, 21);
		setting.add(item_type);
		
		JLabel label_9 = new JLabel(I18n.tr("guiLabelitem-type")+": ");
		label_9.setBounds(10, 207, 73, 15);
		setting.add(label_9);
		
		JComboBox<String> recipe_slot = new JComboBox<String>();
		recipe_slot.setBounds(91, 232, 84, 21);
		setting.add(recipe_slot);
		
		JLabel label_10 = new JLabel(I18n.tr("guiLabelitem-slot")+": ");
		label_10.setBounds(10, 238, 73, 15);
		setting.add(label_10);
		
		JButton add_item = new JButton(I18n.tr("guiButtonadd-item"));
		add_item.setBounds(39, 312, 93, 23);
		setting.add(add_item);
		
		JCheckBox recipe_p_edit = new JCheckBox(I18n.tr("guiButtonedit"));
		recipe_p_edit.setBounds(273, 203, 67, 23);
		setting.add(recipe_p_edit);
		
		JCheckBox recipe_m_edit = new JCheckBox(I18n.tr("guiButtonedit"));
		recipe_m_edit.setBounds(273, 6, 67, 23);
		setting.add(recipe_m_edit);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(185, 35, 151, 162);
		setting.add(scrollPane_2);
		
		JTextArea recipe_m_item = new JTextArea();
		scrollPane_2.setViewportView(recipe_m_item);
		recipe_m_item.setEditable(false);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(185, 232, 151, 169);
		setting.add(scrollPane_3);
		
		JTextArea recipe_p_item = new JTextArea();
		recipe_p_item.setEditable(false);
		scrollPane_3.setViewportView(recipe_p_item);
		
		JButton save = new JButton(I18n.tr("guiButtonsave"));
		save.setBounds(39, 345, 93, 23);
		setting.add(save);
		
		JButton close = new JButton(I18n.tr("guiButtonclose"));
		close.setBounds(39, 378, 93, 23);
		setting.add(close);
		
		JLabel label_11 = new JLabel(I18n.tr("guiLabelitem-amount")+": ");
		label_11.setBounds(10, 266, 67, 15);
		setting.add(label_11);
		
		recipe_item_amount = new JTextField();
		recipe_item_amount.setText("1");
		recipe_item_amount.setColumns(10);
		recipe_item_amount.setBounds(91, 263, 84, 21);
		setting.add(recipe_item_amount);
		
		JPanel pane_workbench = new JPanel();
		pane_workbench.setBounds(74, 422, 319, 214);
		//pane_workbench.setBounds(999, 999, 100, 100);
		getContentPane().add(pane_workbench);
		pane_workbench.setLayout(null);
		pane_workbench.setVisible(false);
		
		JLabel solt_w_m_4 = new JLabel("");
		solt_w_m_4.setBounds(143, 45, 30, 30);
		pane_workbench.add(solt_w_m_4);
		
		JLabel solt_w_m_10 = new JLabel("");
		solt_w_m_10.setBounds(71, 117, 30, 30);
		pane_workbench.add(solt_w_m_10);
		
		JLabel solt_w_m_15 = new JLabel("");
		solt_w_m_15.setBounds(106, 153, 30, 30);
		pane_workbench.add(solt_w_m_15);
		
		JLabel solt_w_p_1 = new JLabel("");
		solt_w_p_1.setBounds(214, 80, 30, 30);
		pane_workbench.add(solt_w_p_1);
		
		JLabel solt_w_p_4 = new JLabel("");
		solt_w_p_4.setBounds(252, 117, 30, 30);
		pane_workbench.add(solt_w_p_4);
		
		JLabel solt_w_m_12 = new JLabel("");
		solt_w_m_12.setBounds(143, 117, 30, 30);
		pane_workbench.add(solt_w_m_12);
		
		JLabel solt_w_p_3 = new JLabel("");
		solt_w_p_3.setBounds(214, 117, 30, 30);
		pane_workbench.add(solt_w_p_3);
		
		JLabel solt_w_m_3 = new JLabel("");
		solt_w_m_3.setBounds(106, 45, 30, 30);
		pane_workbench.add(solt_w_m_3);
		
		JLabel solt_w_m_8 = new JLabel("");
		solt_w_m_8.setBounds(143, 80, 30, 30);
		pane_workbench.add(solt_w_m_8);
		
		JLabel solt_w_m_13 = new JLabel("");
		solt_w_m_13.setBounds(35, 153, 30, 30);
		pane_workbench.add(solt_w_m_13);
		
		JLabel solt_w_m_11 = new JLabel("");
		solt_w_m_11.setBounds(106, 117, 30, 30);
		pane_workbench.add(solt_w_m_11);
		
		JLabel solt_w_m_2 = new JLabel("");
		solt_w_m_2.setBounds(71, 45, 30, 30);
		pane_workbench.add(solt_w_m_2);
		
		JLabel solt_w_m_7 = new JLabel("");
		solt_w_m_7.setBounds(106, 80, 30, 30);
		pane_workbench.add(solt_w_m_7);
		
		JLabel solt_w_m_9 = new JLabel("");
		solt_w_m_9.setBounds(35, 117, 30, 30);
		pane_workbench.add(solt_w_m_9);
		
		JLabel solt_w_m_6 = new JLabel("");
		solt_w_m_6.setBounds(71, 80, 30, 30);
		pane_workbench.add(solt_w_m_6);
		
		JLabel solt_w_p_2 = new JLabel("");
		solt_w_p_2.setBounds(252, 80, 30, 30);
		pane_workbench.add(solt_w_p_2);
		
		JLabel solt_w_m_14 = new JLabel("");
		solt_w_m_14.setBounds(71, 153, 30, 30);
		pane_workbench.add(solt_w_m_14);
		
		JLabel solt_w_m_5 = new JLabel("");
		solt_w_m_5.setBounds(35, 80, 30, 30);
		pane_workbench.add(solt_w_m_5);
		
		JLabel solt_w_m_16 = new JLabel("");
		solt_w_m_16.setBounds(143, 153, 30, 30);
		pane_workbench.add(solt_w_m_16);
		
		JLabel solt_w_m_1 = new JLabel("");
		solt_w_m_1.setBounds(35, 45, 30, 30);
		pane_workbench.add(solt_w_m_1);
		
		JLabel icon_w = new JLabel("New label");
		icon_w.setBounds(-4, 5, 326, 215);
		java.awt.Image img = null;
		try {
			img = javax.imageio.ImageIO.read(RealSurvivalItemMaker.class.getResourceAsStream("/resources/rsmaker/workbench.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		icon_w.setIcon(new ImageIcon(img));
		pane_workbench.add(icon_w);
		
		JPanel panel_furnace = new JPanel();
		panel_furnace.setBounds(74, 422, 319, 214);
		getContentPane().add(panel_furnace);
		panel_furnace.setLayout(null);
		panel_furnace.setVisible(false);
		
		JLabel solt_f_p_1 = new JLabel("");
		solt_f_p_1.setBounds(109, 147, 30, 30);
		panel_furnace.add(solt_f_p_1);
		
		JLabel solt_f_m_5 = new JLabel("");
		solt_f_m_5.setBounds(147, 76, 30, 30);
		panel_furnace.add(solt_f_m_5);
		
		JLabel solt_f_m_6 = new JLabel("");
		solt_f_m_6.setBounds(182, 76, 30, 30);
		panel_furnace.add(solt_f_m_6);
		
		JLabel solt_f_m_4 = new JLabel("");
		solt_f_m_4.setBounds(109, 76, 30, 30);
		panel_furnace.add(solt_f_m_4);
		
		JLabel solt_f_p_3 = new JLabel("");
		solt_f_p_3.setBounds(182, 147, 30, 30);
		panel_furnace.add(solt_f_p_3);
		
		JLabel solt_f_m_1 = new JLabel("");
		solt_f_m_1.setBounds(109, 40, 30, 30);
		panel_furnace.add(solt_f_m_1);
		
		JLabel solt_f_m_3 = new JLabel("");
		solt_f_m_3.setBounds(182, 40, 30, 30);
		panel_furnace.add(solt_f_m_3);
		
		JLabel solt_f_p_2 = new JLabel("");
		solt_f_p_2.setBounds(147, 147, 30, 30);
		panel_furnace.add(solt_f_p_2);
		
		JLabel solt_f_m_2 = new JLabel("");
		solt_f_m_2.setBounds(147, 40, 30, 30);
		panel_furnace.add(solt_f_m_2);
		
		JLabel label_32 = new JLabel("New label");
		try {
			img = javax.imageio.ImageIO.read(RealSurvivalItemMaker.class.getResourceAsStream("/resources/rsmaker/furnace.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		label_32.setIcon(new ImageIcon(img));
		label_32.setBounds(0, 0, 326, 215);
		panel_furnace.add(label_32);
		
		{
			solt_f_m_1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(0);
				}
			});
			solt_f_m_2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(1);
				}
			});
			solt_f_m_3.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(2);
				}
			});
			solt_f_m_4.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(3);
				}
			});
			solt_f_m_5.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(4);
				}
			});
			solt_f_m_6.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(5);
				}
			});
			solt_f_p_1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(6);
				}
			});
			solt_f_p_2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(7);
				}
			});
			solt_f_p_3.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(8);
				}
			});
		}
		
		{
			solt_w_m_1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(0);
				}
			});
			solt_w_m_2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(1);
				}
			});
			solt_w_m_3.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(2);
				}
			});
			solt_w_m_4.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(3);
				}
			});
			solt_w_m_5.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(4);
				}
			});
			solt_w_m_6.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(5);
				}
			});
			solt_w_m_7.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(6);
				}
			});
			solt_w_m_8.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(7);
				}
			});
			solt_w_m_9.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(8);
				}
			});
			solt_w_m_10.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(9);
				}
			});
			solt_w_m_11.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(10);
				}
			});
			solt_w_m_12.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(11);
				}
			});
			solt_w_m_13.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(12);
				}
			});
			solt_w_m_14.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(13);
				}
			});
			solt_w_m_15.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(14);
				}
			});
			solt_w_m_16.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(15);
				}
			});
			solt_w_p_1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(16);
				}
			});
			solt_w_p_2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(17);
				}
			});
			solt_w_p_3.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(18);
				}
			});
			solt_w_p_4.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					recipe_slot.setSelectedIndex(19);
				}
			});
		}
		
		recipe_p_edit.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				recipe_p_item.setEditable(recipe_p_edit.isSelected());
			}
		});
		recipe_m_edit.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				recipe_m_item.setEditable(recipe_m_edit.isSelected());
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				dispose();
			}
		});
		
		//close Setting
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setting.setVisible(false);
			}
		});
		
		//editor
		recipe_p_item.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String data[] = recipe_p_item.getText().split("\n");
				int length = data.length<p.size()?data.length:p.size();
				for(int i = 0;i<length;i++){
					try {
						if(data[i].equalsIgnoreCase(I18n.tr("GUIKeyWordnothing")))p.replace(i, null);
						else if(!data[i].contains(I18n.tr("GUIKeyWorddiy-item"))&&!data[i].contains(I18n.tr("GUIKeyWordoriginal-item"))&&!data[i].contains(I18n.tr("GUIKeyWordamount")))p.replace(i, null);
						else {
							String item = data[i];
							p.replace(i, new Item(item.split(";")[0], Integer.parseInt(item.split(";")[1].split(":")[1])));
						}
					} catch (Exception e) {
						p.replace(i, null);
					}
				}
				setItemToEditor(recipe_m_item, recipe_p_item);
			}
		});
		
		recipe_m_item.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String data[] = recipe_m_item.getText().split("\n");
				int length = data.length<m.size()?data.length:m.size();
				for(int i = 0;i<length;i++){
					try {
						if(data[i].equalsIgnoreCase(I18n.tr("GUIKeyWordnothing")))m.replace(i, null);
						else if(!data[i].contains(I18n.tr("GUIKeyWorddiy-item"))&&!data[i].contains(I18n.tr("GUIKeyWordoriginal-item")))m.replace(i, null);
						else {
							String item = data[i];
							m.replace(i, new Item(item.split(";")[0], Integer.parseInt(item.split(";")[1].split(":")[1])));
						}
					} catch (Exception e) {
						m.replace(i, null);
					}
				}
				setItemToEditor(recipe_m_item, recipe_p_item);
			}
		});
		
		//create recipe
		create_new_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(setting.isVisible()){
					JOptionPane.showMessageDialog(window, I18n.tr("guiMsgrecipe-in-doing"));
					return;
				}
				if(workbench_type.getSelectedItem() == null){
					JOptionPane.showMessageDialog(window, "Please!");
					return;
				}
				//clean
				recipe_m_item.setText("");
				recipe_p_item.setText("");
				recipe_furnace_max_tem.setText("");
				recipe_furnace_min_tem.setText("");
				recipe_furnace_save_time.setText("");
				recipe_name.setText("");
				recipe_need_time.setText("");
				recipe_furnace_panel.setVisible(false);
				p.clear();
				m.clear();
				//set
				WorkbenchShape ws = Data.workbenchs.get(workbench_type.getSelectedItem().toString());
				recipe_workbench_type.setText(ws.getName());
				setWorkbenchSlot(recipe_slot,ws.getType());
				if(ws.getType()==WorkbenchType.FURNACE)recipe_furnace_panel.setVisible(true);
				setting.setVisible(true);
				setItemToEditor(recipe_m_item, recipe_p_item);
				if(ws.getType() == WorkbenchType.FURNACE){
					pane_workbench.setVisible(false);
					panel_furnace.setVisible(true);
				}else{
					pane_workbench.setVisible(true);
					panel_furnace.setVisible(false);
				}
			}
		});
		
		//add item
		add_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(item_type.getSelectedItem().toString().equalsIgnoreCase(I18n.tr("GUIKeyWordnothing")))
					putItem(recipe_slot.getSelectedItem().toString(),null);
				else if(item_type.getSelectedItem().toString().equalsIgnoreCase(I18n.tr("GUIKeyWordoriginal-item"))){
					if(checkIsInt(recipe_item_amount.getText(), I18n.tr("guiLabelitem-amount")))return;
					putItem(recipe_slot.getSelectedItem().toString(),I18n.tr("GUIKeyWordoriginal-item")+":"+getMinecraftItem(minecraft_item).name()+";"+I18n.tr("GUIKeyWordamount")+":"+recipe_item_amount.getText());
				}else{
					DefaultMutableTreeNode note = (DefaultMutableTreeNode) item_tree.getLastSelectedPathComponent();
					if(note == null || note.getChildCount()>0){
						JOptionPane.showMessageDialog(window, I18n.tr("guiMsgno-selected-item"));
						return;
					}
					if(checkIsInt(recipe_item_amount.getText(), I18n.tr("guiLabelitem-amount")))return;
					String name = "";
					for(Object temp:item_tree.getSelectionPath().getPath())
						name+=temp.toString()+"/";
					name = name.substring(0, name.length()-1).replace("items/", "");
					putItem(recipe_slot.getSelectedItem().toString(),I18n.tr("GUIKeyWorddiy-item")+":"+name+";"+I18n.tr("GUIKeyWordamount")+":"+recipe_item_amount.getText());
				}
				setItemToEditor(recipe_m_item, recipe_p_item);
			}
		});
		//save recipe
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorkbenchShape ws = Data.workbenchs.get(recipe_workbench_type.getText());
				String name;
				int time,saveTime;
				double temMax,temMin;
				if(checkIsNull(recipe_name.getText(), I18n.tr("guiLabelrecipe-name")))return;
				if(checkIsInt(recipe_need_time.getText(), I18n.tr("guiLabelneed-time")))return;
				boolean isNull=true;
				for(Item temp:m.values())if(temp!=null&&temp.itemName!=null)isNull = false;
				if(isNull)return;
				isNull = true;
				for(Item temp:p.values())if(temp!=null&&temp.itemName!=null)isNull = false;
				if(isNull)return;
				name = recipe_name.getText();
				time = Integer.parseInt(recipe_need_time.getText());
				Inventory inv;
				if(ws.getType()==WorkbenchType.FURNACE){
					if(checkIsInt(recipe_furnace_save_time.getText(), I18n.tr("guiLabelsave-time")))return;
					if(checkIsDouble(recipe_furnace_max_tem.getText(), I18n.tr("guiLabelmax-temperature")))return;
					if(checkIsDouble(recipe_furnace_min_tem.getText(), I18n.tr("guiLabelmin-temperature")))return;	
					saveTime = Integer.parseInt(recipe_furnace_save_time.getText());
					temMax = Double.parseDouble(recipe_furnace_max_tem.getText());
					temMin = Double.parseDouble(recipe_furnace_min_tem.getText());
					FurnaceRecipe fr = new FurnaceRecipe(name, time, saveTime, temMin, temMax, ws.getName());
					inv = Furnace.furnaceMakerGUI("RSMaker");
					for(int i=0;i<m.size();i++){
						if(m.get(i)==null)continue;
						Item ri = m.get(i);
						String data[] = ri.itemName.split(":");
						if(data[0].equalsIgnoreCase(I18n.tr("GUIKeyWordoriginal-item")))
							{
							ItemStack item = new ItemStack(Material.getMaterial(data[1]));
							item.setAmount(ri.amount);
							inv.setItem(Furnace.mSlot.get(i), item);
							}
						else if(data[0].equalsIgnoreCase(I18n.tr("GUIKeyWorddiy-item"))) {
							ItemStack item = RSItem.loadItem(data[1]).getItem();
							item.setAmount(ri.amount);
							inv.setItem(Furnace.mSlot.get(i), item);
						}
						else continue;
					}
					
					for(int i=0;i<p.size();i++){
						if(p.get(i)==null)continue;
						Item ri = p.get(i);
						String data[] = ri.itemName.split(":");
						if(data[0].equalsIgnoreCase(I18n.tr("GUIKeyWordoriginal-item"))){
							ItemStack item = new ItemStack(Material.getMaterial(data[1]));
							item.setAmount(ri.amount);
							inv.setItem(Furnace.pSlot.get(i), item);
						}
						else if(data[0].equalsIgnoreCase(I18n.tr("GUIKeyWorddiy-item"))) {
							ItemStack item = RSItem.loadItem(data[1]).getItem();
							item.setAmount(ri.amount);
							inv.setItem(Furnace.pSlot.get(i), item);
						}
						else continue;
					}
					FurnaceRecipe.createFurnaceRecipe(inv, fr);
				}else{
					WorkbenchRecipe wr = new WorkbenchRecipe(name, time, ws.getName());
					inv = Workbench.createWorkbenchRecipeGUI("RSMaker");
					for(int i=0;i<m.size();i++){
						if(m.get(i)==null)continue;
						Item ri = m.get(i);
						String data[] = ri.itemName.split(":");
						if(data[0].equalsIgnoreCase(I18n.tr("GUIKeyWordoriginal-item"))){
							ItemStack item = new ItemStack(Material.getMaterial(data[1]));
							item.setAmount(ri.amount);
							inv.setItem(Workbench.materials.get(i), item);
						}
						else if(data[0].equalsIgnoreCase(I18n.tr("GUIKeyWorddiy-item"))) {
							ItemStack item = RSItem.loadItem(data[1]).getItem();
							item.setAmount(ri.amount);
							inv.setItem(Workbench.materials.get(i), item);
						}
						else continue;
					}
					
					for(int i=0;i<p.size();i++){
						if(p.get(i)==null)continue;
						Item ri = p.get(i);
						String data[] = ri.itemName.split(":");
						if(data[0].equalsIgnoreCase(I18n.tr("GUIKeyWordoriginal-item"))){
							ItemStack item = new ItemStack(Material.getMaterial(data[1]));
							item.setAmount(ri.amount);
							inv.setItem(Workbench.products.get(i), item);
						}
						else if(data[0].equalsIgnoreCase(I18n.tr("GUIKeyWorddiy-item"))) {
							ItemStack item = RSItem.loadItem(data[1]).getItem();
							item.setAmount(ri.amount);
							inv.setItem(Workbench.products.get(i), item);
						}
						else continue;
					}
					WorkbenchRecipe.createRecipe(inv, wr);
				}
				
				setting.setVisible(false);
				scrollPane_1.setViewportView(item_tree);
				item_tree.setModel(new DefaultTreeModel(getItemsDir(new File(Data.DATAFOLDER+"/items"))));
				recipe_tree.setModel(new DefaultTreeModel(getItemsDir(new File(Data.DATAFOLDER+"/recipe"))));
				pane_workbench.setVisible(false);
				panel_furnace.setVisible(false);
			}
		});
		
	}
	
	@SuppressWarnings("deprecation")
	private Material getMinecraftItem(JComboBox<Material> minecraft_item){
		Material item = Material.AIR;
		try {
			if(minecraft_item.getSelectedItem()==null)
				item = (Material.AIR);
			if(!(minecraft_item.getSelectedItem() instanceof Material)){
				String data = minecraft_item.getSelectedItem().toString();
				Integer data2 = null;
				try {
					data2 = Integer.parseInt(data);
				} catch (Exception e) {
					
				}
				if(data2==null){
					item = (Material.getMaterial(data.toUpperCase()));
				}else item = (Material.getMaterial(data2));
					
			}else{
				item = (Material) minecraft_item.getSelectedItem();
			}
		} catch (Exception e) {
			item = (Material.AIR);
		}
		return item;
	}
	
	private boolean checkIsNull(String str,String msgId){
		if(str==null || str.replace(" ", "").equals("")){
			JOptionPane.showMessageDialog(window, msgId+I18n.tr("guiMsgcan-not-be-null"));
			return true;
		}
		return false;
	}
	
	private boolean checkIsInt(String str,String msgId){
		if(checkIsNull(str, msgId))return true;
		try{
			int i = Integer.parseInt(str);
			if(i<=0){
				JOptionPane.showMessageDialog(window, msgId+I18n.tr("guiMsgnot-int-2"));
				return true;
			}
			return false;
		}catch (Exception e) {
			JOptionPane.showMessageDialog(window, msgId+I18n.tr("guiMsgnot-int"));
			return true;
		}
	}
	
	private boolean checkIsDouble(String str,String msgId){
		if(checkIsNull(str, msgId))return true;
		try{
			Double i = Double.parseDouble(str);
			if(i<=0){
				JOptionPane.showMessageDialog(window, msgId+I18n.tr("guiMsgnot-int-2"));
				return true;
			}
			return false;
		}catch (Exception e) {
			JOptionPane.showMessageDialog(window, msgId+I18n.tr("guiMsgnot-int"));
			return true;
		}
	}
	
	private void putItem(String slot,String item){
		String data[] = slot.split(":");
		if(data[0].equalsIgnoreCase(I18n.tr("GUIKeyWordmaterials"))){
			if(item==null) m.replace(Integer.parseInt(data[1])-1, null);
			else m.replace(Integer.parseInt(data[1])-1, new Item(item.split(";")[0], Integer.parseInt(item.split(";")[1].split(":")[1])));
		}else{
			if(item==null) p.replace(Integer.parseInt(data[1])-1, null);
			else p.replace(Integer.parseInt(data[1])-1, new Item(item.split(";")[0], Integer.parseInt(item.split(";")[1].split(":")[1])));
		}
	}
	
	private void setItemToEditor(JTextArea m,JTextArea p){
		String mText = "",pText="";
		for(Item str:this.m.values())
			if(str==null)mText+=I18n.tr("GUIKeyWordnothing")+"\n";
			else mText+=str.toString()+"\n";
		for(Item str:this.p.values())
			if(str==null)pText+=I18n.tr("GUIKeyWordnothing")+"\n";
			else pText+=str.toString()+"\n";
		m.setText(mText);
		p.setText(pText);
	}
}
