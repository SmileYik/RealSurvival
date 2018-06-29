package app;

import javax.swing.JFrame;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RealSurvivalMaker extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private JFrame window;
	private JFrame item=null,recipe=null;
	
	public static void showGUI(){
		new RealSurvivalMaker().setVisible(true);
	}

	/**
	 * Create the application.
	 */
	public RealSurvivalMaker() {
		//window = this;
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setResizable(false);
		this.setTitle("RealSurvivalItemMaker");
		//window.setBounds(100, 100, 675, 416);
		//this.setBounds(100, 100, 155, 416);
		this.setBounds(100, 100, 288, 147);
		this.getContentPane().setLayout(null);
		
		JButton btnRealsurvivalitemmaker = new JButton("RealSurvivalItemMaker");
		btnRealsurvivalitemmaker.setBounds(10, 29, 262, 23);
		getContentPane().add(btnRealsurvivalitemmaker);
		
		JButton btnRealsurvivalrecipemaker = new JButton("RealSurvivalRecipeMaker");
		btnRealsurvivalrecipemaker.setBounds(10, 62, 262, 23);
		getContentPane().add(btnRealsurvivalrecipemaker);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				dispose();
			}
		});
		
		btnRealsurvivalitemmaker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(item==null){
					item = new RealSurvivalItemMaker();
					item.setVisible(true);
					item.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent arg0) {
							item = null;
						}
					});
				}
			}
		});
		
		btnRealsurvivalrecipemaker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(recipe==null){
					recipe = new RealSurvivalRecipeMaker();
					recipe.setVisible(true);
					recipe.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent arg0) {
							recipe = null;
						}
					});
				}
			}
		});
		
		
	}
}
