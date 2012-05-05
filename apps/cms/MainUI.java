package cms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.*;

import diagrameditor.HistoryPanel;

public class MainUI extends JFrame {
	private JButton upButton;
	private JButton downButton;
	private JButton deleteButton;
	public JList list;
	public MainUI(){

		setTitle("Collaboration Content Management System");
		//setSize(700,700);
		
		// set up history pane
		JPanel historyPane = new JPanel();
		JPanel buttonPane = new JPanel(new GridLayout(1, 3));
		list = new JList();
		JScrollPane listScrollPane = new JScrollPane(list);
		buttonPane.setMinimumSize(new Dimension(200,500));
		
		//add history buttons
				//up button
				ImageIcon icon = createImageIcon("up");
				
				if (icon != null) {
					this.upButton = new JButton(icon);
					this.upButton.setSize(5, 5);
					this.upButton.setMargin(new Insets(0, 0, 0, 0));
				} else {
					this.upButton = new JButton("UP");
				}
				this.upButton.setToolTipText("Move the currently selected operation higher.");
				//this.upButton.addActionListener(upButton(editor, list));
				//this.upButton.setActionCommand(upString);
				buttonPane.add(this.upButton);
				
				//down button
				icon = createImageIcon("down");
				if (icon != null) {
					this.downButton = new JButton(icon);
					this.downButton.setMargin(new Insets(0, 0, 0, 0));
				} else {
					this.downButton = new JButton("Down");
				}
				this.downButton.setToolTipText("Move the currently selected operation lower.");
				//this.downButton.addActionListener(downButton(editor, list));
				//this.downButton.setActionCommand(downString);
				buttonPane.add(this.downButton);
				
				// delete button
				
				icon = createImageIcon("delete");
				if (icon != null) {
					this.deleteButton = new JButton(icon);
					this.deleteButton.setMargin(new Insets(0, 0, 0, 0));
				} else {
					this.deleteButton = new JButton("Delete");
				}
				this.deleteButton.setToolTipText("Delete the selected operation.");
				//this.deleteButton.addActionListener(deleteButton(editor, list));
				//this.deleteButton.setActionCommand("Delete");
				buttonPane.add(this.deleteButton);
				historyPane.add(buttonPane,BorderLayout.NORTH);
				historyPane.add(listScrollPane,BorderLayout.CENTER);
				
				
		
		
		//set up list pane
		
		
		
		JPanel jsp2 = new JPanel();
		JPanel historyPanel = new JPanel();
		JLabel history = new JLabel("Hostory");
		
		
		JTextArea displayArea = new JTextArea();
		displayArea.setMinimumSize(new Dimension(200,500));
		
		displayArea.setText("test");
		JPanel fileOrganiserPane = new JPanel();
		JLabel l3 = new JLabel("file organiser");
		
		jsp2.add(displayArea);
		jsp2.setMinimumSize(new Dimension(800,500));
		
		fileOrganiserPane.add(l3);
		historyPanel.add(history);
		JPanel editTextArea = new JPanel();
		JLabel text = new JLabel("edit Text");
		editTextArea.add(text);
		//editTextArea.setMinimumSize(new Dimension(800,500));
		
		
		
		
		//create split panes
		
		JSplitPane historySplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,historyPane,historyPanel);
		
		historySplitPane.setDividerSize(8);
		historySplitPane.setContinuousLayout(true);
		    
		JSplitPane editTextSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,jsp2,editTextArea);
		
		
		editTextSplitPane.setDividerSize(8);
		editTextSplitPane.setContinuousLayout(true);
		
		
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,historySplitPane,editTextSplitPane);
		//JSplitPane fileOrganiserSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,splitPane,fileOrganiserPane);
		
		
		splitPane.setContinuousLayout(false);
	

	 

		
		splitPane.setOneTouchExpandable(true);
		
		historySplitPane.setOneTouchExpandable(true);
		editTextSplitPane.setOneTouchExpandable(true);
		
		JSplitPane fileOrganiserSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,splitPane,fileOrganiserPane);
		
		fileOrganiserSplitPane.setContinuousLayout(false);
		fileOrganiserSplitPane.setOneTouchExpandable(true);
		
		getContentPane().add(fileOrganiserSplitPane,BorderLayout.CENTER);
	
		
		
	}
	
	public static void main(String[] args){
		
		MainUI ui = new MainUI();
		ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui.setVisible(true);
		ui.setSize(1500, 1000);
		
		
	}
	
	/**
	 * Method to create an image
	 * @param imageName
	 * @return an ImageIcon of the image
	 */
	private static ImageIcon createImageIcon(String imageName) {
		String imgLocation = imageName + ".jpg";
		java.net.URL imageURL = HistoryPanel.class.getResource(imgLocation);
		if (imageURL == null) {
			System.err.println("Resource not found: " + imgLocation);
			return null;
		} else {
			return new ImageIcon(imageURL);
		}
	}
	
	
	
	
	
	
}
