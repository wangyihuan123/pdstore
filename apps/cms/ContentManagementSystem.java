package cms;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.commons.io.FileUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.lobobrowser.html.*;
import org.lobobrowser.html.gui.*;
import org.lobobrowser.html.parser.*;
import org.lobobrowser.html.test.*;

import cms.dal.PDCMSOperation;
import cms.dal.PDDocument;
import cms.dal.PDDocumentOperation;
import cms.dal.PDFileOperation;
import cms.dal.PDHistory;
import cms.dal.PDUser;

import pdstore.GUID;
import pdstore.dal.PDInstance;
import pdstore.dal.PDWorkingCopy;
import diagrameditor.HistoryPanel;
import diagrameditor.OperationList;
import diagrameditor.dal.PDOperation;

/**
 * 
 * A prototype collaborative Content Mananagent System using PDStore.
 * 
 * Basic system architecture: 
 * 
 * Edits are made to document by multiple authors on multiple 
 * Content Management System instances. These edits are recorded in a shared history and then 
 * replayed on each instance to create a shared view of a document.
 * 
 * @author Sina Masoud-Ansari (s.ansari@auckland.ac.nz)
 * @author David Chen
 *
 */
public class ContentManagementSystem extends JFrame implements KeyListener   {

	private static final long serialVersionUID = 1L;
	protected String DOCUMENT_ROOT = System.getenv("HOME")+"/www";

	// PDStore
	PDWorkingCopy wc;
	PDHistory history;
	PDUser user;
	//PDStoreTextPane textEditor; // Useful for debugging RSyntaxArea problems
	PDStoreRTextPane textEditor;
	CMSOperationList opHistory;

	// UI
	private JButton upButton;
	private JButton downButton;
	private JButton deleteButton;
	private JLabel theLabel;
	JLabel saveStatus;
	String editingFileName;
	private PDHtmlPanel htmlTextArea;
	JTextPane editTextArea;
	// TODO: make the file view use DOCUMENT_ROOT
	File dir = new File(System.getenv("HOME")+"/www"); // used by the file view 
	DefaultMutableTreeNode node;
	protected PDFileBrowser tree;
	PDHistoryBrowser historyBrowser;
	JSplitPane fileOrganiserSplitPane;
	JPanel fileOrganiserPane;
	DefaultMutableTreeNode moveOrgNode;
	DefaultMutableTreeNode moveDestNode;
	DefaultMutableTreeNode copyOrgNode;
	DefaultMutableTreeNode copyDestNode;

	static JTextField folderName;

	/**
	 * 
	 * @param userID the user of this CMS
	 * @param historyID the shared history
	 * @param wc the shared working copy
	 */
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
		//initDefaultTextEditor();
		initRSyntaxTextEditor();
		initHistoryBrowser();

		// set up and populate history pane
		JPanel buttonPane = new JPanel(new GridLayout(1, 3));		
		JPanel buttonPane1 = new JPanel();

		// TODO: move this code out of the constructor.
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
		JSplitPane historyPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,buttonPane,new JScrollPane(historyBrowser));
		historyPane.setOneTouchExpandable(true);
		historyPane.setDividerSize(8);
		historyPane.setMinimumSize(new Dimension(250, 450));

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

		/**
		 * Responds to a user adding a file.
		 */
		add.addActionListener(new ActionListener()  
		{  
			public void actionPerformed(ActionEvent e)  
			{  

				if (node == null){
					node = (DefaultMutableTreeNode)tree.getModel().getRoot();
				} else {
					node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				}
				String str = JOptionPane.showInputDialog(null, "Please enter the file or folder name : ", "Collaborative Content Management System", 1);
				if(str != null){
					//add new node into the file system
					String filename = str;	

					File s = new File(node.toString()+ "/"+filename);
					String pdfname = s.getAbsolutePath().replace(DOCUMENT_ROOT, "");
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
					tree.createPDDocument(pdfname);
					// inform others via PDStore after the local user has performed the file operation
					tree.alertPDFileOperation(PDFileBrowser.ADD, pdfname, null);
				}

			}  
		});

		JButton delete = new JButton("DELETE");
		/**
		 * Responds to a user deleting a file
		 */
		delete.addActionListener(new ActionListener()  
		{  
			public void actionPerformed(ActionEvent e)  
			{  

				// refresh the tree node after delete
				DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); 

				// inform others via PDStore after the local user has performed the file operation
				String pdfname = selNode.toString().replace(DOCUMENT_ROOT, "");
				tree.alertPDFileOperation(PDFileBrowser.DELETE, pdfname, null); 

			}  
		});



		//add move from button
		JButton moveFrom = new JButton("MOVE FROM");

		/**
		 * Responds to a user moving a file.
		 */
		// TODO: rethink how this should be done i.e via menus
		moveFrom.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e)
			{
				//get the orginal node
				moveOrgNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); 
			}
		});

		//add move to button
		JButton moveTo = new JButton("MOVE TO");

		/**
		 * Responds to a user moving a file
		 */
		// TODO: rethink how this should be done i.e via menus
		moveTo.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e)
			{
				if (moveDestNode == null) {
					return;
				} else {						
					moveDestNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); 
					String pdOrgFname = moveOrgNode.toString().replace(DOCUMENT_ROOT, "");
					String pdDestFname = moveDestNode.toString().replace(DOCUMENT_ROOT, "");
					tree.alertPDFileOperation(PDFileBrowser.MOVE, pdOrgFname, pdDestFname);
				}
			}
		});


		//add copyFrom button
		JButton copyFrom = new JButton("COPY FROM");

		/**
		 * Responds to a user copying a file
		 */
		// TODO: rethink how this should be done i.e via menus				
		copyFrom.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e)
			{
				//get the orginal node
				copyOrgNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); 
			}
		});

		//add copyTo button
		JButton copyTo = new JButton("COPY TO");

		/**
		 * Responds to a user copying a file
		 */
		// TODO: rethink how this should be done i.e via menus		
		copyTo.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e)
			{
				if (copyOrgNode == null) {
					return;
				} else {
					//get the orginal node
					copyDestNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); 
					String pdOrgFname = copyOrgNode.toString().replace(DOCUMENT_ROOT, "");
					String pdDestFname = copyDestNode.toString().replace(DOCUMENT_ROOT, "");
					tree.alertPDFileOperation(PDFileBrowser.COPY, pdOrgFname, pdDestFname);
				}
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

		initFileBrowser();

		// fileOrganiserPane.add(tree);
		//create split panes

		JSplitPane historySplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,historyPane,functionalButtonPanel);
		historySplitPane.setDividerSize(8);
		historySplitPane.setContinuousLayout(true);

		htmlTextArea.setPreferredSize(new Dimension(450,450));
		JSplitPane editTextSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,htmlTextArea, new JScrollPane(textEditor));

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


		/*
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {

		            	//save all the content to the file
		            	if (editingFileName != null){

		            		saveFile(editTextArea.getText(),node.getParent().toString()+"/"+node.toString());
		            	}



		            }
		        }, 
		        5000 
		);

		 */

	}

	/**
	 * Load the PDStore instances by the CMS
	 * 
	 * @param userID the local user
	 * @param historyID the shared history
	 */
	private void initPDObjects(GUID userID, GUID historyID){
		user = PDUser.load(wc, userID);
		history = PDHistory.load(wc, historyID);

	}


	/**
	 * Show the files in the DOCUMENT_ROOT
	 */
	private void initFileBrowser(){
		DefaultMutableTreeNode defaultTreeNode = initDocumentTree(null, new File(DOCUMENT_ROOT));
		tree = new PDFileBrowser(defaultTreeNode, DOCUMENT_ROOT, user, history, wc, this);

		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
		renderer.setTextSelectionColor(Color.white);
		renderer.setBackgroundSelectionColor(Color.blue);
		renderer.setBorderSelectionColor(Color.black);

		// Setup PDFileOperation listener
		GUID role2 = PDFileOperation.roleOpTypeId;
		wc.getStore().getDetachedListenerList().add(new PDFileBrowserListener(this, role2));
		tree.setEditable(true);

		/**
		 * Setup the behavior of file selections. Currently they load in the text view and are rendered in the 
		 * HTML view.
		 */
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				String filepath = "";
				String ext = node.toString().substring(node.toString().lastIndexOf('.'));
				String pdfname = node.toString().replace(DOCUMENT_ROOT, "");
				if (ext.contains(".html") || ext.contains(".css") || ext.contains(".js")){
					setCurrentDocument(pdfname);	
					filepath = node.getParent().toString()+"/"+node.toString();
					synchronized (opHistory) {
						htmlTextArea.render("");
						RSyntaxDocument doc = new RSyntaxDocument( getStyle(ext) );
						textEditor.setDocument(doc);
						textEditor.setupDoc();		

						PDStoreDocumentFilter filter = (PDStoreDocumentFilter) doc.getDocumentFilter();		
						filter.setFilter(false);
						textEditor.setText(readFromFile(filepath));
						filter.setFilter(true);
						textEditor.updateFromHistory(opHistory);
					}
				}
			}
		});

	}

	/**
	 * Basic file content reading using FileUtils
	 * 
	 * @param filepath the file path string
	 * @return the String contents of the file
	 */
	private String readFromFile(String filepath){
		File f = new File(filepath);
		String s = "";
		try {
			s = FileUtils.readFileToString(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * Determine the content type for use with syntax highlighting
	 * @param ext the filename extention
	 * @return the RSyntaxTextArea style
	 */
	private String getStyle(String ext){
		if (ext.toLowerCase().contains("html")) {
			return RSyntaxTextArea.SYNTAX_STYLE_HTML;
		} else if (ext.toLowerCase().contains("css")) {
			return RSyntaxTextArea.SYNTAX_STYLE_CSS;
		} else if (ext.toLowerCase().contains("js")) {
			return RSyntaxTextArea.SYNTAX_STYLE_JAVASCRIPT;
		} else {
			return RSyntaxTextArea.SYNTAX_STYLE_NONE;
		}
	}

	@Deprecated
	protected boolean noDocHistory(String fname){
		DefaultMutableTreeNode node = historyBrowser.searchNode(fname);
		if (node == null || node.getChildCount() == 0){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets the current document for the local user.
	 * Used to determine which document operations should be applied to the current view.
	 * 
	 * @param fname the filename
	 */
	protected void setCurrentDocument(String fname){
		Collection<PDInstance> docs = wc.getAllInstancesOfType(PDDocument.typeId);
		for (PDInstance i : docs){
			PDDocument d = (PDDocument) i;
			String s = d.getDocumentFileName();
			if (s != null && s.equals(fname)){
				user.setCurrentDocument(d.getId());
				//wc.commit();
				break;
			}
		}

	}

	/**
	 * Sets up the html view
	 */
	// TODO: refactor, this is no longer a textArea
	private void initHTMLViewer(){
		htmlTextArea = new PDHtmlPanel();
	}

	/**
	 * Used for debugging the RSyntax editor
	 */
	private void initDefaultTextEditor(){

		/*
		// Setup editor
		textEditor = new PDStoreTextPane(wc, user, history, htmlTextArea, this);
		DefaultCaret c = (DefaultCaret) textEditor.getCaret();
		c.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		// Set up mulitiple carets
		//CMSCaret caret = new CMSCaret(wc, user);
		//caret.setUpdatePolicy(CMSCaret.UPDATE_WHEN_ON_EDT);
		textEditor.setCaret(c);	

		// Editor label
		JLabel text = new JLabel("Text Editor");
		textEditor.add(text);

		// Setup PDDocumentOperation listener
		GUID role2 = PDDocumentOperation.roleOpTypeId;
		wc.getStore().getDetachedListenerList().add(new PDDocumentOperationListener(this, role2));

		// Set key listener to notify html view
		textEditor.addKeyListener(this);
		 */

	}	

	/**
	 * Init the syntax highlighting document editor
	 */
	private void initRSyntaxTextEditor(){

		// Setup editor
		textEditor = new PDStoreRTextPane(wc, user, history, htmlTextArea, this);

		// RSyntax Highlighting
		JPanel cp = new JPanel(new BorderLayout());
		textEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
		textEditor.setCodeFoldingEnabled(true);
		textEditor.setAntiAliasingEnabled(true);
		RTextScrollPane sp = new RTextScrollPane(textEditor);
		sp.setFoldIndicatorEnabled(true);
		cp.add(sp);		

		// Set up mulitiple carets
		CMSRSyntaxCaret caret = new CMSRSyntaxCaret(wc, user); // has some instability issues
		caret.setUpdatePolicy(CMSCaret.ALWAYS_UPDATE);
		textEditor.setCaret(caret);	

		// Editor label
		JLabel text = new JLabel("Text Editor");
		textEditor.add(text);

		// Set key listener to notify html view
		textEditor.addKeyListener(this);

	}

	/**
	 * Create the document root if it doesn't exist.
	 */
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
	 * Setup the listener for history events
	 */
	private void initHistoryListener(){
		opHistory = new CMSOperationList(PDCMSOperation.class, history, PDHistory.roleCMSOperationId, PDCMSOperation.typeId, PDCMSOperation.roleNextOpId);	

		// Setup PDDocumentOperation listener
		GUID role2 = PDHistory.roleCMSOperationId;
		wc.getStore().getDetachedListenerList().add(new PDCMSHistoryListener(this, role2));

	}

	/**
	 * Called by other classes to update the text of the textEditor
	 */
	protected void updateDocument(){
		textEditor.updateFromHistory(opHistory);
	}

	/**
	 * Called by other classes to refresh the view of the history browser
	 */
	protected void refreshHistory() {
		//System.out.println("HIST: "+opHistory.size());
		historyBrowser.refreshTree(opHistory);
	}

	/**
	 * Initialise the history browser using a tree view
	 */
	protected void initHistoryBrowser(){
		initHistoryListener();

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Document History");
		TreeModel historyTreeModel = new DefaultTreeModel(root);
		DefaultMutableTreeNode child;
		for (PDCMSOperation	op : opHistory){
			child = new DefaultMutableTreeNode("Child");
			root.add(child);
		}

		historyBrowser = new PDHistoryBrowser(root, wc, this);
		historyBrowser.setEditable(true);
		historyBrowser.setScrollsOnExpand(true);
		refreshHistory();

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
		//htmlTextArea.render(textEditor.getText());
	}

	/** Handle the key released event from the text field. */
	public void keyReleased(KeyEvent e) {

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
	 * Method to read the content of the file
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

	/**
	 *  Save the file every 5 seconds
	 */

	private static void saveEditingFile(String content,	File file){

	}


	public void saveFile(String content,String filename){

		try{
			FileWriter fstream = new FileWriter(filename,true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content);
			//Close the output stream
			out.close();
		}catch (Exception e){//Catch exception if any

		}
	}




}