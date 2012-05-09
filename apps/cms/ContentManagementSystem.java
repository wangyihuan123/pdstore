package cms;

import java.awt.BorderLayout;

import java.awt.Color;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;

import java.util.Collections;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import cms.dal.PDDocument;
import cms.dal.PDDocumentOperation;
import cms.dal.PDFileOperation;
import cms.dal.PDHistory;
import cms.dal.PDUser;

import pdstore.GUID;
import pdstore.GUIDGen;
import pdstore.dal.PDWorkingCopy;
import diagrameditor.HistoryPanel;

public class ContentManagementSystem extends JFrame implements KeyListener   {

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

	private JLabel theLabel;
	private JTextPane htmlTextArea;
	JTextPane editTextArea;

	File dir = new File(System.getenv("HOME")+"/www");
	DefaultMutableTreeNode node;
	protected PDFileBrowser tree;

	JSplitPane fileOrganiserSplitPane;
	// JSplitPane splitPane;
	JPanel fileOrganiserPane;
	JTextField folderName;
	
	public ContentManagementSystem(GUID userID, GUID historyID, final PDWorkingCopy wc){


		// Setup PDStore Objects
		this.wc = wc;
		initPDObjects(userID, historyID);

		// Setup common CMS properties
		checkDocumentRoot();
		setTitle(user.getName()+"'s CMS");
		setSize(1000,1000);
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
		folderName = new JTextField("test");

		// folderName.enableInputMethods(true);
		folderName.setEditable(true);
		//folderName.setSize(1000, 1000);
		add.addActionListener(new ActionListener()  
		{  
			public void actionPerformed(ActionEvent e)  
			{  
				//add new node into the file system
				String filename = folderName.getText();	  
				File s = new File(node.toString()+ "/"+filename);
				if (s.mkdir()){
					
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
					tree.addNode(node, filename);
					// inform others via PDStore
					//TODO: get the filename from DOCUMENT_ROOT
					tree.alertPDFileOperation(PDFileBrowser.ADD, filename, null);	
				}

			}  
		});

		JButton delete = new JButton("DELETE");
		delete.addActionListener(new ActionListener()  
		{  
			public void actionPerformed(ActionEvent e)  
			{  



				//add new node into the file system
				//  String filename = folderName.getText();	  
				//  File s = new File(node.toString()+ "/"+filename);
				//  s.mkdir();


				// refresh tree
				DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); 
				DefaultTreeModel m_model =(DefaultTreeModel) (tree.getModel()); 
				if (selNode != null) 
				{ 
					// add new node as a child of a selected node at the end 

					m_model.removeNodeFromParent(selNode); 

				} 

				File deletefile = new File(selNode.toString());
				deletefile.delete();
				
				// inform others via PDStore
				tree.alertPDFileOperation(PDFileBrowser.DELETE, selNode.toString(), null); //TODO: get the filename from DOCUMENT_ROOT
			}  
		});




		JButton move = new JButton("MOVE");

		JButton load = new JButton("LOAD");



		functionalButtonPanel.add(add,gbc);
		functionalButtonPanel.add(folderName,gbc);
		functionalButtonPanel.add(delete,gbc);
		functionalButtonPanel.add(move,gbc);
		functionalButtonPanel.add(load,gbc);

		//set up display area pane

		//JPanel displayAreaPanel = new JPanel();
		htmlTextArea = new JTextPane();

		//PDStoreTextPane editTextArea = new PDStoreTextPane(wc, user, history);
		//editTextArea.addKeyListener(this);

		htmlTextArea.setContentType("text/html");
		//editTextArea.setText("<span style='font-size: 20pt'>Big</span>");
		//htmlTextArea.setText(editTextArea.getText());


		// set up the folder tree view.

		// fileOrganiserPane = new JPanel();
		initFileBrowser();

		// fileOrganiserPane.add(tree);


		//create split panes

		JSplitPane historySplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,historyPane,functionalButtonPanel);

		historySplitPane.setDividerSize(8);
		historySplitPane.setContinuousLayout(true);

		JSplitPane editTextSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,htmlTextArea,textEditor);

		//JSplitPane editTextSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,jsp2,textEditor);


		editTextSplitPane.setDividerSize(8);
		editTextSplitPane.setContinuousLayout(true);

		JSplitPane	splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,historySplitPane,editTextSplitPane);


		splitPane.setContinuousLayout(false);
		splitPane.setOneTouchExpandable(true);

		historySplitPane.setOneTouchExpandable(true);
		editTextSplitPane.setOneTouchExpandable(true);

		fileOrganiserSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,splitPane,tree);

		fileOrganiserSplitPane.setContinuousLayout(false);
		fileOrganiserSplitPane.setOneTouchExpandable(true);

		//fileOrganiserSplitPane.repaint();

		getContentPane().add(fileOrganiserSplitPane,BorderLayout.CENTER);

	}

	private void initPDObjects(GUID userID, GUID historyID){
		user = PDUser.load(wc, userID);
		history = PDHistory.load(wc, historyID);
	}

	private void initFileBrowser(){
		DefaultMutableTreeNode defaultTreeNode = initDocumentTree(null, new File(DOCUMENT_ROOT));
		tree = new PDFileBrowser(defaultTreeNode, DOCUMENT_ROOT, user, history, wc);
		
		// Setup PDFileOperation listener
		GUID role2 = PDFileOperation.roleOpTypeId;
		wc.getStore().getDetachedListenerList().add(new PDFileBrowserListener(this, role2));
		tree.setEditable(true);

		// Add selection listener
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();

				// if node is a file, set current user document in pdstore
				if (node.isLeaf() && !node.getAllowsChildren()) {
					System.out.println("User selected file: " + node);
					// TODO: set in PDStore - fname is path from DOCUMENT_ROOT
				}
			}
		});

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

		// Set up mulitiple carets
		CMSCaret caret = new CMSCaret(wc, user);
		caret.setUpdatePolicy(CMSCaret.ALWAYS_UPDATE);
		textEditor.setCaret(caret);	

		// Editor label
		JLabel text = new JLabel("Text Editor");
		textEditor.add(text);

		// Setup PDDocumentOperation listener
		GUID role2 = PDDocumentOperation.roleOpTypeId;
		wc.getStore().getDetachedListenerList().add(new PDDocumentOperationListener(this, role2));

		// Set key listener to notify html view
		textEditor.addKeyListener(this);

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

	/** Add nodes from under "dir" into curTop. Highly recursive. */
	private DefaultMutableTreeNode initDocumentTree(DefaultMutableTreeNode curTop, File dir) {
		dir = new File(DOCUMENT_ROOT);
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
				initDocumentTree(curDir, f);
			else
				files.addElement(thisObject);
		}
		// Pass two: for files.
		for (int fnum = 0; fnum < files.size(); fnum++)
			curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
		return curDir;
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
		htmlTextArea.setText(textEditor.getText());
	}

	/** Handle the key released event from the text field. */
	public void keyReleased(KeyEvent e) {

	}

	public void createTree(){

	}



}