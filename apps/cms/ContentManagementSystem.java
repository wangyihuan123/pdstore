package cms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import javax.swing.*;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import cms.dal.PDHistory;
import cms.dal.PDOperation;
import cms.dal.PDUser;

import pdstore.GUID;
import pdstore.GUIDGen;
import pdstore.PDStore;
import pdstore.dal.PDSimpleWorkingCopy;
import pdstore.dal.PDWorkingCopy;
import pdstore.generic.PDChange;

import diagrameditor.HistoryPanel;
import diagrameditor.RepaintListener;

public class ContentManagementSystem extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final String DOCUMENT_ROOT = System.getenv("HOME")+"/www";
	
	// PDStore
	PDWorkingCopy wc;
	PDHistory history;
	PDUser user;
	PDStoreTextPane textEditor;
	
	// UI
	private JButton upButton;
	private JButton downButton;
	private JButton deleteButton;
	public JList list;
	
	public ContentManagementSystem(GUID userID, GUID historyID, PDWorkingCopy wc){

		// Setup PDStore Objects
		this.wc = wc;
		initPDObjects(userID, historyID);
		
		// Setup common CMS properties
		checkDocumentRoot();
		setTitle(user.getName()+"'s CMS");
		setSize(500,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
		
		// Create history based text editor	
		initTextEditor();
		
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
		
		//create split panes
		
		JSplitPane historySplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,historyPane,functionalButtonPanel);
		
		historySplitPane.setDividerSize(8);
		historySplitPane.setContinuousLayout(true);
		    
		JSplitPane editTextSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,jsp2,textEditor);
		
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
	
	private void initPDObjects(GUID userID, GUID historyID){
		user = PDUser.load(wc, userID);
		history = PDHistory.load(wc, historyID);
	}
	
	private void initTextEditor(){
		
		// Setup editor
		textEditor = new PDStoreTextPane(wc, user, history);
		
		// RSyntax Highlighting
		JPanel cp = new JPanel(new BorderLayout());
		textEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
		textEditor.setCodeFoldingEnabled(true);
		textEditor.setAntiAliasingEnabled(true);
		RTextScrollPane sp = new RTextScrollPane(textEditor);
	    sp.setFoldIndicatorEnabled(true);
	    cp.add(sp);		
		
		CMSCaret caret = new CMSCaret(wc, user);
		textEditor.setCaret(caret);		
		
		
		// Editor label
		JLabel text = new JLabel("Text Editor");
		textEditor.add(text);
		
		// Setup listener
		GUID role2 = PDOperation.roleOpTypeId;
		wc.getStore().getDetachedListenerList().add(new PDStoreDocumentListener(this, role2));
			
	}
	
	public void setCaretColor(Color c){
		textEditor.setCaretColor(c);
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
