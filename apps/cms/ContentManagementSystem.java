package cms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import cms.dal.PDHistory;
import cms.dal.PDUser;

import pdstore.GUID;
import pdstore.GUIDGen;
import pdstore.PDStore;
import pdstore.dal.PDSimpleWorkingCopy;
import pdstore.dal.PDWorkingCopy;

import diagrameditor.HistoryPanel;

public class ContentManagementSystem extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final String DOCUMENT_ROOT = System.getenv("HOME")+"/www";
	
	// PDStore
	private static final boolean NETWORK_ACCESS = false;
	PDHistory history;
	PDUser user;
	
	// UI
	private JButton upButton;
	private JButton downButton;
	private JButton deleteButton;
	public JList list;
	
	public ContentManagementSystem(String username, GUID userID, PDWorkingCopy wc, GUID historyID){

		checkDocumentRoot();
		initPDObjects(username, userID, wc, historyID);
		
		setTitle(username+"'s CMS");
		setSize(150,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		// set up and populate history pane
		//JPanel historyPane = new JPanel();
		JPanel buttonPane = new JPanel(new GridLayout(1, 3));		
		JPanel buttonPane1 = new JPanel();

		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setDragEnabled(true);
		//list.setSize(new Dimension(200,500));
		JScrollPane listScrollPane = new JScrollPane(list);
		
		//buttonPane.setMinimumSize(new Dimension(200,500));
		//listScrollPane.setMinimumSize(new Dimension(200,500));
		//historyPane.setMinimumSize(new Dimension(200,500));
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

				
				buttonPane1.add(buttonPane);
				JSplitPane historyPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,buttonPane,listScrollPane);
				historyPane.setOneTouchExpandable(true);
				historyPane.setDividerSize(8);
				
				
		//set up function button pane
				
			JPanel functionalButtonPanel = new JPanel();
		   // JLabel history = new JLabel("History");
		    JButton add = new JButton("ADD");
		    JButton delete = new JButton("DELETE");
		    functionalButtonPanel.add(add);
		    functionalButtonPanel.add(delete);
		
		//set up list pane
		
		JPanel jsp2 = new JPanel();
		JPanel historyPanel = new JPanel();
		JLabel historyLabel = new JLabel("History");
		
		JTextArea displayArea = new JTextArea();
		//displayArea.setMinimumSize(new Dimension(200,500));
		
		displayArea.setText("test");
		JPanel fileOrganiserPane = new JPanel();
		JLabel l3 = new JLabel("file organiser");
		
		jsp2.add(displayArea);
		//jsp2.setMinimumSize(new Dimension(800,500));
		
		fileOrganiserPane.add(l3);
		historyPanel.add(historyLabel);
		//JPanel editTextArea = new JPanel();
		PDStoreTextPane editTextArea = new PDStoreTextPane(wc, user);
		JLabel text = new JLabel("edit Text");
		editTextArea.add(text);
		//editTextArea.setMinimumSize(new Dimension(800,500));
		
		//create split panes
		
		JSplitPane historySplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,historyPane,functionalButtonPanel);
		
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
	
	private void checkDocumentRoot(){
		File root = new File(DOCUMENT_ROOT);
		if (!root.isDirectory()){
			try {
				root.mkdir();
			} catch (SecurityException sec) {
				System.err.println("Unable to create document root '"+DOCUMENT_ROOT+"'");
			}
		}
	}
	
	private void initPDObjects(String username, GUID userID, PDWorkingCopy wc, GUID historyID){
		// init PDHistory
		history = PDHistory.load(wc, historyID);
		
		// init PDUser
		user = PDUser.load(wc, userID);
		user.setName(username);
		
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
		
	
	public static void main(String[] args){

		try {
			Class.forName("cms.dal.PDHistory");
			Class.forName("cms.dal.PDUser");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
		
		// Setup PDStore cache
		PDStore store;
		PDWorkingCopy wc1;
		PDWorkingCopy wc2;
		
		// Setup GUIDs
		GUID historyID = GUIDGen.generateGUIDs(1).remove(0);
		GUID userID1 = GUIDGen.generateGUIDs(1).remove(0);
		GUID userID2 = GUIDGen.generateGUIDs(1).remove(0);
		
		// Determine PDStore location
		if (NETWORK_ACCESS) {
			store = PDStore.connectToServer(null);
			wc1 = new PDSimpleWorkingCopy(store);
			wc2 = new PDSimpleWorkingCopy(store);
		} else {
			store = new PDStore("ContentManagementSystem");
			wc1 = new PDSimpleWorkingCopy(store);
			wc2 = wc1;
		}		

		// Create the UIs
		ContentManagementSystem cms1 = new ContentManagementSystem("Bob", userID1, wc1, historyID);
		ContentManagementSystem cms2 = new ContentManagementSystem("Alice", userID2, wc2, historyID);
		cms1.setVisible(true);
		cms2.setVisible(true);
	}
	
}
