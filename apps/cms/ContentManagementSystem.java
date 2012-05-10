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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.Collections;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
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
	DefaultMutableTreeNode moveOrgNode;
	DefaultMutableTreeNode moveDestNode;
	DefaultMutableTreeNode copyOrgNode;
	DefaultMutableTreeNode copyDestNode;
	
	static JTextField folderName;
	
	

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
		initHTMLViewer();
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
				
			
				String str = JOptionPane.showInputDialog(null, "Please enter the file or folder name : ", 
						"Collaborative Content Management System", 1);
						  if(str != null){
							  
							//add new node into the file system
								String filename = str;	  
								File s = new File(node.toString()+ "/"+filename);
								String pdfname = s.getAbsolutePath().replace(DOCUMENT_ROOT, "");
								if (s.mkdir()){

									DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
									tree.addNodeToTree(node, node.toString()+ "/"+filename);
									// inform others via PDStore after the local user has performed the file operation
									tree.alertPDFileOperation(PDFileBrowser.ADD, pdfname, null);	

								}
								
							  
						  }

			}  
		});

		JButton delete = new JButton("DELETE");
		delete.addActionListener(new ActionListener()  
		{  
			public void actionPerformed(ActionEvent e)  
			{  

				// refresh the tree node after delete
				DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); 
				tree.deleteNodeFromTree(selNode);
				
				//delete the file from file system.
				deleteFiles(selNode.toString());

				// inform others via PDStore after the local user has performed the file operation
				String pdfname = selNode.toString().replace(DOCUMENT_ROOT, "");
				tree.alertPDFileOperation(PDFileBrowser.DELETE, pdfname, null); //TODO: get the filename from DOCUMENT_ROOT

			}  
		});



		//add move from button
		JButton moveFrom = new JButton("MOVE FROM");
		moveFrom.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e)
			{
				//get the orginal node
				moveOrgNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); 
			}
		});
		
		//add move to button
				JButton moveTo = new JButton("MOVE TO");
				moveTo.addActionListener(new ActionListener(){
					public void actionPerformed (ActionEvent e)
					{
						//get the orginal node
						moveDestNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); 
						tree.moveNodeToTree(moveOrgNode, moveDestNode);
					}
				});
				
				
		//add copyFrom button
		JButton copyFrom = new JButton("COPY FROM");
		
		copyFrom.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e)
			{
				//get the orginal node
				copyOrgNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); 
			}
		});
		
		//add copyTo button
		JButton copyTo = new JButton("COPY TO");
		
		copyTo.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e)
			{
				//get the orginal node
				copyDestNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); 
				tree.copyNodeToTree(copyOrgNode, copyDestNode);
			}
		});
		
		

//		JButton load = new JButton("LOAD");



		functionalButtonPanel.add(add,gbc);
		//functionalButtonPanel.add(folderName,gbc);
		functionalButtonPanel.add(delete,gbc);
		functionalButtonPanel.add(moveFrom,gbc);
		functionalButtonPanel.add(moveTo,gbc);
		functionalButtonPanel.add(copyFrom,gbc);
		functionalButtonPanel.add(copyTo,gbc);
	//	functionalButtonPanel.add(load,gbc);

		//set up display area pane

		//JPanel displayAreaPanel = new JPanel();


		//PDStoreTextPane editTextArea = new PDStoreTextPane(wc, user, history);
		//editTextArea.addKeyListener(this);

		
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

		//david new added 9/5/2012
		DefaultTreeCellRenderer renderer =
                (DefaultTreeCellRenderer) tree.getCellRenderer();
        renderer.setTextSelectionColor(Color.white);
        renderer.setBackgroundSelectionColor(Color.blue);
        renderer.setBorderSelectionColor(Color.black);
        
        
        
		// Setup PDFileOperation listener
		GUID role2 = PDFileOperation.roleOpTypeId;
		wc.getStore().getDetachedListenerList().add(new PDFileBrowserListener(this, role2));
		tree.setEditable(true);

		// Add selection listener
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				
				if (node.toString().contains(".html")){
					// David 10.5.2012 display the file contents
				String filepath = node.getParent().toString()+"/"+node.toString();
				textEditor.setText(readFiles(new File(filepath)));
				
				//htmlTextArea.setText("hello");
				//htmlTextArea.setText(textEditor.getText());
				//htmlTextArea.repaint();
				}
				// if node is a file, set current user document in pdstore
				if (node.isLeaf() && !node.getAllowsChildren()) {
					String pdfname = node.toString().replace(DOCUMENT_ROOT, "");
					tree.alertPDFileOperation(PDFileBrowser.SELECT, pdfname, null); //TODO: get the filename from DOCUMENT_ROOT	
										
				}
			}
		});

	}

	private void initHTMLViewer(){
		htmlTextArea = new JTextPane();
		htmlTextArea.setContentType("text/html");
	}
	
	private void initTextEditor(){

		// Setup editor
		textEditor = new PDStoreTextPane(wc, user, history, htmlTextArea);

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
				//newPath = File.separator+thisObject;
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
	 * node refresh method
	 */

	
	
	private static void refreshTree(DefaultMutableTreeNode curTop, File dir){
		
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
	
	/**
	 * Method to delete the file from file system
	 * @param filename
	 */
	private static void deleteFiles(String filename){
		File deletefile = new File(filename);
		deletefile.delete();
	}

	/**
	 * Method to delete the file from file system
	 * @param filename
	 */
	private static void addFiles(String filename){
		File newfile = new File(filename);
		newfile.mkdir();
		
	}
	
	/**
	 * Method to move the file from original position to destination position
	 * @param original
	 * @param destination
	 */
	private static void moveFiles(String original,String destination){
		addFiles(destination);
		deleteFiles(original);
	}
	
	/**
	 * Method to copy the file from original position to destination position
	 * @param original
	 * @param destination
	 */
	private static void copyFiles(String original,String destination){
		addFiles(destination);
		
	}
	
	/**
	 * Method to show the dialog box
	 * 
	 */
	
	private static String readFiles(File inputFile){
		
		StringBuffer fileData = new StringBuffer(1000);
		try {
			BufferedReader reader = new BufferedReader( new FileReader(inputFile));
			char[] buf = new char[1024];
			int numRead=0;
			while((numRead=reader.read(buf)) != -1){
				String readData = String.valueOf(buf,0,numRead);
				fileData.append(readData);
				buf=new char[1024];
			}
			reader.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileData.toString();
		
	}
	
	
	

}