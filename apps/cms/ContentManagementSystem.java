package cms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;


import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import apple.laf.JRSUIUtils.Tree;

import cms.dal.PDHistory;
import cms.dal.PDUser;

import pdstore.GUID;
import pdstore.GUIDGen;
import pdstore.PDStore;
import pdstore.dal.PDSimpleWorkingCopy;
import pdstore.dal.PDWorkingCopy;

import diagrameditor.HistoryPanel;

public class ContentManagementSystem extends JFrame implements KeyListener   {

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
	private JLabel theLabel;
    private JTextPane htmlTextArea;
	JTextPane editTextArea;
	File dir = new File("/Users/eason36/www/");
	 DefaultMutableTreeNode node;
	  JTree tree;
	  
	 JSplitPane fileOrganiserSplitPane;
	 // JSplitPane splitPane;
	 JPanel fileOrganiserPane;
	public ContentManagementSystem(String username, GUID userID, PDWorkingCopy wc, GUID historyID){

		checkDocumentRoot();
		initPDObjects(username, userID, wc, historyID);
		
		setTitle(username+"'s CMS");
		setSize(1000,1000);
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
			functionalButtonPanel.setLayout( new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints ();

			// next two lines will place the components top-to-bottom, rather than left-to-right
			gbc.gridx = 0;
			gbc.gridy = GridBagConstraints.RELATIVE;

		   // JLabel history = new JLabel("History");
		    JButton add = new JButton("ADD");
		    add.addActionListener(new ActionListener()  
			{  
				   public void actionPerformed(ActionEvent e)  
				   {  
				  File s = new File(node.toString()+ "/newfolder");
				  s.mkdir();
				  // refresh tree
				  //DefaultMutableTreeNode top= new DefaultMutableTreeNode(s);
				 
				 
				  DefaultTreeModel model = (DefaultTreeModel) tree.getModel(); 
				  
				// Find node to which new node is to be added
				//  int startRow = 0;
				  //String prefix = "J";
				 // TreePath path = tree.getNextMatch(prefix, startRow, Position.Bias.Forward);
				//  MutableTreeNode node = node;

				// Create new node
				  MutableTreeNode newNode = new DefaultMutableTreeNode("newfolder");

				  // Insert new node as last child of node
				  model.insertNodeInto(newNode, node, node.getChildCount());
				  
				  model.reload();
				  fileOrganiserPane.repaint();
				   }  
				});
		    
		    JButton delete = new JButton("DELETE");
		    JButton move = new JButton("MOVE");
		  
		    functionalButtonPanel.add(add,gbc);
		    functionalButtonPanel.add(delete,gbc);
		    functionalButtonPanel.add(move,gbc);
		    
		    
		//set up display area pane
		
		JPanel displayAreaPanel = new JPanel();
		htmlTextArea = new JTextPane();
		
		PDStoreTextPane editTextArea = new PDStoreTextPane(wc, user);
		editTextArea.addKeyListener(this);
		
		htmlTextArea.setContentType("text/html");
		editTextArea.setText("<span style='font-size: 20pt'>Big</span>");
		htmlTextArea.setText(editTextArea.getText());
		
		
		// set up the folder tree view.
		
		 fileOrganiserPane = new JPanel();
	

		  
		   tree = new JTree(addNodes(null, dir));
		  

		    // Add a listener
		    tree.addTreeSelectionListener(new TreeSelectionListener() {
		      public void valueChanged(TreeSelectionEvent e) {
		         node = (DefaultMutableTreeNode) e
		            .getPath().getLastPathComponent();
		        System.out.println("You selected " + node);
		      }
		    });
		    
		    
		    

		   fileOrganiserPane.add(tree);
		
		
		
		//create split panes
		
		JSplitPane historySplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,historyPane,functionalButtonPanel);
		
		historySplitPane.setDividerSize(8);
		historySplitPane.setContinuousLayout(true);
		    
		JSplitPane editTextSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,htmlTextArea,editTextArea);
		
		editTextSplitPane.setDividerSize(8);
		editTextSplitPane.setContinuousLayout(true);
		
		JSplitPane	splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,historySplitPane,editTextSplitPane);
		
		
		splitPane.setContinuousLayout(false);
		splitPane.setOneTouchExpandable(true);
		
		historySplitPane.setOneTouchExpandable(true);
		editTextSplitPane.setOneTouchExpandable(true);
		
		 fileOrganiserSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,splitPane,fileOrganiserPane);
		
		fileOrganiserSplitPane.setContinuousLayout(false);
		fileOrganiserSplitPane.setOneTouchExpandable(true);
		
		//fileOrganiserSplitPane.repaint();
		
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
		
	
	
	 /** Handle the key typed event from the text field. */
    public void keyTyped(KeyEvent e) {
      
    }
     
    /** Handle the key pressed event from the text field. */
    public void keyPressed(KeyEvent e) {
    	htmlTextArea.setText(editTextArea.getText());
    }
     
    /** Handle the key released event from the text field. */
    public void keyReleased(KeyEvent e) {
        
    }
    
    public void createTree(){
    	
    }
    
    
    /** Add nodes from under "dir" into curTop. Highly recursive. */
    DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
      String curPath = dir.getPath();
      DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
      if (curTop != null) { // should only be null at root
        curTop.add(curDir);
      }
      Vector ol = new Vector();
      String[] tmp = dir.list();
      for (int i = 0; i < tmp.length; i++)
        ol.addElement(tmp[i]);
      Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
      File f;
      Vector files = new Vector();
      // Make two passes, one for Dirs and one for Files. This is #1.
      for (int i = 0; i < ol.size(); i++) {
        String thisObject = (String) ol.elementAt(i);
        String newPath;
        if (curPath.equals("."))
          newPath = thisObject;
        else
          newPath = curPath + File.separator + thisObject;
        if ((f = new File(newPath)).isDirectory())
          addNodes(curDir, f);
        else
          files.addElement(thisObject);
      }
      // Pass two: for files.
      for (int fnum = 0; fnum < files.size(); fnum++)
        curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
      return curDir;
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
