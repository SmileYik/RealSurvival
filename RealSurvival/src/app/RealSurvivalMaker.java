package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import com.outlook.schooluniformsama.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RealSurvivalMaker {

	private JFrame frmRealsurvivalmaker;
	private JTextField item_name;
	private JTextField item_label_data;
	private HashMap<String, String> labels = new HashMap<>();
	private String split;
	private String root = "RealSurvival/items/";
	private String form = "%name%:\n  ==: org.bukkit.inventory.ItemStack\n  type: %type%\n  meta:\n    ==: ItemMeta\n    meta-type: UNSPECIFIC\n    display-name: %name2%\n    lore:\n%lore%\n";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		if(!new File("RealSurvival").exists() || !new File("RealSurvival/config.yml").exists()){
			JOptionPane.showMessageDialog(null, "请先运行一次服务器后再打开此gui界面!");
			return;
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RealSurvivalMaker window = new RealSurvivalMaker();
					window.frmRealsurvivalmaker.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RealSurvivalMaker() {
		initialize();
	}
	
	private DefaultMutableTreeNode getItemsDir(){
		DefaultMutableTreeNode dtm = new DefaultMutableTreeNode("items");
		File items = new File(root);
		if(!items.exists()){
			items.mkdirs();
			return dtm;
		}
		for(File item:items.listFiles()){
			if(!item.isFile()||!item.getName().substring(item.getName().lastIndexOf(".")+1).equalsIgnoreCase("yml"))continue;
			dtm.add(new DefaultMutableTreeNode(item.getName().substring(0,item.getName().lastIndexOf("."))));
		}
		return dtm;
	}
	
	private DefaultComboBoxModel<Material> getItemType(){
		return new DefaultComboBoxModel<>(Material.values());
	}
	
	private DefaultComboBoxModel<String> getLabels(){
		LinkedList<String> list = new LinkedList<>();
		list.add("DIY");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("RealSurvival/config.yml"));
		split = config.getString("label.split");
		for(String temp:config.getStringList("label.labels")){
			String data[] = temp.split(":");
			list.add(data[0]);
			labels.put(data[0], data[1]);
		}
		return new DefaultComboBoxModel<>(list.toArray(new String[list.size()]));
	}
	
	private YamlConfiguration getItem(String name){
		return YamlConfiguration.loadConfiguration(new File("RealSurvival/items/"+name+".yml"));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRealsurvivalmaker = new JFrame();
		frmRealsurvivalmaker.setResizable(false);
		frmRealsurvivalmaker.setTitle("RealSurvivalMaker");
		frmRealsurvivalmaker.setBounds(100, 100, 675, 416);
		frmRealsurvivalmaker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRealsurvivalmaker.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 131, 345);
		frmRealsurvivalmaker.getContentPane().add(scrollPane);
		
		JTree tree = new JTree();
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.setModel(new DefaultTreeModel(getItemsDir()));
		scrollPane.setViewportView(tree);
		
		JButton create_new_item = new JButton("创建");
		create_new_item.setBounds(10, 358, 131, 23);
		frmRealsurvivalmaker.getContentPane().add(create_new_item);
		
		JPanel create_new_item_panel = new JPanel();
		create_new_item_panel.setBounds(152, 10, 507, 371);
		frmRealsurvivalmaker.getContentPane().add(create_new_item_panel);
		create_new_item_panel.setLayout(null);
		create_new_item_panel.setVisible(false);
		
		JLabel lblNewLabel = new JLabel("物品名: ");
		lblNewLabel.setBounds(22, 10, 54, 15);
		create_new_item_panel.add(lblNewLabel);
		
		item_name = new JTextField();
		item_name.setBounds(70, 7, 100, 21);
		create_new_item_panel.add(item_name);
		item_name.setColumns(10);
		
		JLabel label = new JLabel("物品类型:");
		label.setBounds(180, 10, 54, 15);
		create_new_item_panel.add(label);
		
		JComboBox<Material> item_type = new JComboBox<Material>();
		item_type.setModel(getItemType());
		item_type.setBounds(236, 7, 155, 21);
		create_new_item_panel.add(item_type);
		
		JLabel label_1 = new JLabel("物品标签:");
		label_1.setBounds(10, 39, 54, 15);
		create_new_item_panel.add(label_1);
		
		JComboBox<String> item_label = new JComboBox<String>();
		item_label.setModel(getLabels());
		item_label.setBounds(70, 36, 100, 21);
		create_new_item_panel.add(item_label);
		
		JLabel label_2 = new JLabel("标签数值:");
		label_2.setBounds(180, 39, 54, 15);
		create_new_item_panel.add(label_2);
		
		item_label_data = new JTextField();
		item_label_data.setBounds(236, 36, 155, 21);
		create_new_item_panel.add(item_label_data);
		item_label_data.setColumns(10);
		
		JButton add_label = new JButton("添加标签");
		add_label.setBounds(397, 35, 100, 23);
		create_new_item_panel.add(add_label);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 64, 487, 266);
		create_new_item_panel.add(scrollPane_1);
		
		JEditorPane lore_editer = new JEditorPane();
		lore_editer.setEditable(false);
		scrollPane_1.setViewportView(lore_editer);
		
		JButton save_item = new JButton("保存");
		save_item.setBounds(397, 338, 100, 23);
		create_new_item_panel.add(save_item);
		
		JLabel file_name = new JLabel("File: ");
		file_name.setBounds(401, 10, 96, 15);
		create_new_item_panel.add(file_name);
		
		JRadioButton edit_check = new JRadioButton("编辑");
		edit_check.setBounds(318, 338, 73, 23);
		create_new_item_panel.add(edit_check);
		
		
		edit_check.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
					lore_editer.setEditable(edit_check.isSelected());	
			}
		});
		
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount()!=2 || create_new_item_panel.isVisible())return;
				String name = tree.getSelectionPath().getLastPathComponent().toString();
				YamlConfiguration item = getItem(name);
				item_type.setSelectedItem(Material.getMaterial(item.getString(name+".type")));
				item_name.setText(item.getString(name+".meta.display-name"));
				String lore = "";
				for(String line : item.getStringList(name+".meta.lore"))
					lore+=line+"\n";
				lore.substring(0, lore.length()-2);
				lore_editer.setText(lore);
				file_name.setText("File: "+name+".yml");
				create_new_item_panel.setVisible(true);
			}
		});
		
		create_new_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog(frmRealsurvivalmaker, "enter a name" );
				if(name==null||name.replace(" ", "").equals(""))return;
				file_name.setText("File: "+name+".yml");
				item_name.setText(name);
				create_new_item_panel.setVisible(true);
			}
		});
		
		add_label.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(((String)item_label.getSelectedItem()).equalsIgnoreCase("DIY")){
					lore_editer.setText(lore_editer.getText()+item_label_data.getText()+"\n");
				}else{
					if(item_label_data.getText()==null || item_label_data.getText().replace(" ", "").equals("")){
						JOptionPane.showMessageDialog(frmRealsurvivalmaker, "请输入一个数据!");
						return;
					}
					lore_editer.setText(lore_editer.getText()+labels.get(((String)item_label.getSelectedItem()))+split+item_label_data.getText()+"\n");
				}
			}
		});
		
		save_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(item_name.getText() == null || item_name.getText().replace(" ", "").equals("")){
					JOptionPane.showMessageDialog(frmRealsurvivalmaker, "请输入一个名字!");
					return;
				}
				String lore = "    - '"+lore_editer.getText().replace("\n", "'\n    - '");
				lore=lore.substring(0, lore.lastIndexOf("\n")-1);
				String savedate = form.replace("%name%", file_name.getText().replace("File: ", ""))
						.replace("%name2%", item_name.getText())
						.replace("%type%", ((Material) item_type.getSelectedItem()).name())
						.replace("%lore%", lore);
				
				try {
					File file = new File(root+file_name.getText().replace("File: ", ""));
					FileWriter fw = new FileWriter(file);
					fw.write(savedate);
					fw.flush();
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tree.setModel(new DefaultTreeModel(getItemsDir()));
				create_new_item_panel.setVisible(false);
			}
		});
		
		
	}
	
	
}
