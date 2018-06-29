package app;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.item.RSItem;
import com.outlook.schooluniformsama.util.Msg;
import com.outlook.schooluniformsama.util.Util;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RealSurvivalItemMaker extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField item_name;
	private JTextField item_label_data;
	private JFrame window;
	
	public static void showGUI(){
		new RealSurvivalItemMaker().setVisible(true);
		RealSurvivalRecipeMaker.showGUI();
	}

	/**
	 * Create the application.
	 */
	public RealSurvivalItemMaker() {
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
	
	private DefaultComboBoxModel<String> getLabels(){
		LinkedList<String> list = new LinkedList<>();
		list.add("DIY");
		list.addAll(Data.label.keySet());
		return new DefaultComboBoxModel<>(list.toArray(new String[list.size()]));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setResizable(false);
		this.setTitle("RealSurvivalItemMaker");
		//window.setBounds(100, 100, 675, 416);
		//this.setBounds(100, 100, 155, 416);
		this.setBounds(100, 100, 155, 416);
		this.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 131, 345);
		this.getContentPane().add(scrollPane);
		
		JTree tree = new JTree();
		tree.setToolTipText(Msg.getMsg("real-survival-maker.tool-tip-text.tree", false));
		tree.setShowsRootHandles(true);
		tree.setModel(new DefaultTreeModel(getItemsDir(new File(Data.DATAFOLDER+"/items"))));
		scrollPane.setViewportView(tree);
		
		JButton create_new_item = new JButton(Msg.getMsg("real-survival-maker.button.create", false));
		create_new_item.setBounds(10, 358, 131, 23);
		this.getContentPane().add(create_new_item);
		
		JPanel create_new_item_panel = new JPanel();
		create_new_item_panel.setBounds(152, 10, 507, 371);
		this.getContentPane().add(create_new_item_panel);
		create_new_item_panel.setLayout(null);
		create_new_item_panel.setVisible(false);
		
		JLabel lblNewLabel = new JLabel(Msg.getMsg("real-survival-maker.label.item-name", false)+": ");
		lblNewLabel.setBounds(22, 10, 54, 15);
		create_new_item_panel.add(lblNewLabel);
		
		item_name = new JTextField();
		item_name.setBounds(70, 7, 100, 21);
		create_new_item_panel.add(item_name);
		item_name.setColumns(10);
		
		JLabel label = new JLabel(Msg.getMsg("real-survival-maker.label.item-type", false)+": ");
		label.setBounds(180, 10, 54, 15);
		create_new_item_panel.add(label);
		
		JComboBox<Material> item_type = new JComboBox<Material>();
		item_type.setEditable(true);
		item_type.setModel(getItemType());
		item_type.setBounds(236, 7, 155, 21);
		create_new_item_panel.add(item_type);
		
		JLabel label_1 = new JLabel(Msg.getMsg("real-survival-maker.label.item-label", false)+": ");
		label_1.setBounds(10, 39, 54, 15);
		create_new_item_panel.add(label_1);
		
		JComboBox<String> item_label = new JComboBox<String>();
		item_label.setModel(getLabels());
		item_label.setBounds(70, 36, 100, 21);
		create_new_item_panel.add(item_label);
		
		JLabel label_2 = new JLabel(Msg.getMsg("real-survival-maker.label.item-label-data", false)+": ");
		label_2.setBounds(180, 39, 54, 15);
		create_new_item_panel.add(label_2);
		
		item_label_data = new JTextField();
		item_label_data.setBounds(236, 36, 155, 21);
		create_new_item_panel.add(item_label_data);
		item_label_data.setColumns(10);
		
		JButton add_label = new JButton(Msg.getMsg("real-survival-maker.button.add-label", false));
		add_label.setBounds(397, 35, 100, 23);
		create_new_item_panel.add(add_label);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 64, 487, 266);
		create_new_item_panel.add(scrollPane_1);
		
		JEditorPane lore_editer = new JEditorPane();
		lore_editer.setEditable(false);
		scrollPane_1.setViewportView(lore_editer);
		
		JButton save_item = new JButton(Msg.getMsg("real-survival-maker.button.save", false));
		save_item.setBounds(397, 338, 100, 23);
		create_new_item_panel.add(save_item);
		
		JLabel file_name = new JLabel("");
		file_name.setBounds(401, 10, 96, 15);
		create_new_item_panel.add(file_name);
		
		JRadioButton edit_check = new JRadioButton(Msg.getMsg("real-survival-maker.radio-button.edit", false));
		edit_check.setBounds(220, 338, 73, 23);
		create_new_item_panel.add(edit_check);
		
		JButton closeWindow = new JButton(Msg.getMsg("real-survival-maker.button.close", false));
		closeWindow.setBounds(293, 338, 100, 23);
		create_new_item_panel.add(closeWindow);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				dispose();
			}
		});
		
		closeWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				create_new_item_panel.setVisible(false);
				window.setBounds(100, 100, 155, 416);
			}
		});
		
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
				DefaultMutableTreeNode note = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if(note == null || note.getChildCount()>0) return;
				String name = "";
				for(Object temp:tree.getSelectionPath().getPath())
					name+=temp.toString()+"/";
				name = name.substring(0, name.length()-1).replace("items/", "");
				ItemStack item = RSItem.loadItem(name).getItem();
				item_type.setSelectedItem(item.getType());
				
				if(item.hasItemMeta()){
					ItemMeta itemM = item.getItemMeta();
					if(itemM.hasDisplayName())
						item_name.setText(itemM.getDisplayName());
					if(itemM.hasLore()){
						String lore = "";
						for(String line : itemM.getLore())
							lore += line+"\n";
						lore.substring(0,lore.length()-2);
						lore_editer.setText(lore);
					}
						
				}
				
				file_name.setText(""+name);
				create_new_item_panel.setVisible(true);
				window.setBounds(100, 100, 675, 416);
			}
		});
		
		create_new_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog(window, Msg.getMsg("real-survival-maker.input-dialog.create", false) );
				if(name==null||name.replace(" ", "").equals(""))return;
				if(new File(Data.DATAFOLDER+"/items/"+name+".yml").exists()){
					JOptionPane.showMessageDialog(window, Msg.getMsg("real-survival-maker.error-message.item-exists", false));
					return;
				}
				file_name.setText(name);
				item_name.setText(name);
				create_new_item_panel.setVisible(true);
				window.setBounds(100, 100, 675, 416);
			}
		});
		
		add_label.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(((String)item_label.getSelectedItem()).equalsIgnoreCase("DIY")){
					lore_editer.setText(lore_editer.getText()+item_label_data.getText()+"\n");
				}else{
					if(item_label_data.getText()==null || item_label_data.getText().replace(" ", "").equals("")){
						JOptionPane.showMessageDialog(window,  Msg.getMsg("real-survival-maker.error-message.item-label-data", false));
						return;
					}
					lore_editer.setText(lore_editer.getText()+Data.label.get(((String)item_label.getSelectedItem()))+Data.split+item_label_data.getText()+"\n");
				}
			}
		});
		
		save_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(item_name.getText() == null || item_name.getText().replace(" ", "").equals("")){
					JOptionPane.showMessageDialog(window,  Msg.getMsg("real-survival-maker.error-message.item-name", false));
					return;
				}
				
				ItemStack item = new ItemStack(getItem(item_type));
				ItemMeta itemM = item.getItemMeta();
				itemM.setDisplayName(item_name.getText());
				itemM.setLore(Arrays.asList(Util.setColor(lore_editer.getText()).split("\n")));
				item.setItemMeta(itemM);
				new RSItem(item).save(file_name.getText());
				tree.setModel(new DefaultTreeModel(getItemsDir(new File(Data.DATAFOLDER+"/items"))));
				create_new_item_panel.setVisible(false);
				window.setBounds(100, 100, 155, 416);
			}
		});
		
		
	}
	
	@SuppressWarnings("deprecation")
	private Material getItem(JComboBox<Material> item_type){
		Material item;
		if(!(item_type.getSelectedItem() instanceof Material)){
			String data = item_type.getSelectedItem().toString();
			Integer data2 = null;
			try {
				data2 = Integer.parseInt(data);
			} catch (Exception e1) {
				
			}
			if(data2==null){
				item = (Material.getMaterial(data.toUpperCase()));
			}else item = (Material.getMaterial(data2));
		}else item = (Material) item_type.getSelectedItem();
		if(item==null) item = Material.WOOD_SWORD;
		return item;
	}
	
}
