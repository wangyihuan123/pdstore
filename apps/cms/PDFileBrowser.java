package cms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import pdstore.GUIDGen;
import pdstore.dal.PDWorkingCopy;
import cms.dal.PDCMSOperation;
import cms.dal.PDDocument;
import cms.dal.PDDocumentOperation;
import cms.dal.PDFileOperation;
import cms.dal.PDHistory;
import cms.dal.PDUser;

/**
 * Creates a view of the files in the doucment root of the CMS.
 * 
 * @author Sina Masoud-Ansari (s.ansari@auckland.ac.nz)
 * @author David Chen
 *
 */
public class PDFileBrowser extends JTree {

	private static final long serialVersionUID = 8740726184571526994L;
	
	PDWorkingCopy wc;
	PDUser user;
	PDHistory history;
	TreePath recentlyAdded;
	ContentManagementSystem cms;

	protected static final int
	ADD = 0,
	DELETE = 1,
	COPY = 2,
	MOVE = 3,
	SELECT = 4;

	/**
	 * 
	 * @param node the root node of the tree
	 * @param docRoot the document root of the cms
	 * @param user the local user
	 * @param history the shared history
	 * @param wc the working copy of PDStore
	 * @param cms the CMS
	 */
	protected PDFileBrowser(DefaultMutableTreeNode node, String docRoot, PDUser user, PDHistory history, PDWorkingCopy wc, ContentManagementSystem cms){
		super(node);	
		this.user = user;
		this.history = history;
		this.wc = wc;
		this.cms = cms;
	}

	/**
	 * Adds a node to the tree
	 * 
	 * @param selNode the selected node
	 * @param filename the name of the file to be added
	 */
	protected void addNodeToTree(DefaultMutableTreeNode selNode, String filename){
		// refresh tree
		DefaultTreeModel m_model = (DefaultTreeModel) (this.getModel()); 
		if (selNode != null) 
		{ 
			// add new node as a child of a selected node at the end 
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(filename); 
			m_model.insertNodeInto(newNode, selNode, selNode.getChildCount()); 

			//make the node visible by scroll to it 
			TreeNode[] nodes = m_model.getPathToRoot(newNode); 
			recentlyAdded = new TreePath(nodes); 
		} 		
	}
	
	protected void scrollAndSelect(){
		//scroll and select the newly added node 
		this.scrollPathToVisible(recentlyAdded); 
		this.setSelectionPath(recentlyAdded); 		
	}


	/**
	 * MOVE node method
	 * @param selNode
	 */

	protected void moveNodeToTree(DefaultMutableTreeNode orgNode,DefaultMutableTreeNode desNode){

		// update the tree after clicking move button
		addNodeToTree(desNode,orgNode.toString());
		deleteNodeFromTree(orgNode);

	}

	/**
	 * COPY node method
	 * @param selNode
	 */

	protected void copyNodeToTree(DefaultMutableTreeNode orgNode, DefaultMutableTreeNode desNode){

		// update the tree after clicking move button
		addNodeToTree(desNode, orgNode.toString());

	}

	/**
	 * Removes a node from the tree
	 * 
	 * @param selNode the selected node
	 */
	protected void deleteNodeFromTree(DefaultMutableTreeNode selNode){

		// refresh the tree after deletion

		DefaultTreeModel m_model =(DefaultTreeModel) (this.getModel()); 
		if (selNode != null) 
		{ 
			// add new node as a child of a selected node at the end 
			m_model.removeNodeFromParent(selNode); 

		} 		
	}



	/**
	 * Method to delete the file from file system
	 * @param filename
	 */
	protected void deleteFileSystem(String filename){
		File deletefile = new File(filename);
		deletefile.delete();
	}

	/**
	 * Method to delete the file from file system
	 * @param filename
	 */
	protected void addFileSystem(String filename){
		File newfile = new File(filename);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(newfile));
			writer.write(cms.textEditor.getText());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Method to move the file from original position to destination position
	 * @param original
	 * @param destination
	 */
	protected void moveFileSystem(String original,String destination){
		addFileSystem(destination);
		deleteFileSystem(original);
	}

	/**
	 * Method to copy the file from original position to destination position
	 * @param original
	 * @param destination
	 */
	protected void copyFileSystem(String original,String destination){
		addFileSystem(destination);

	}

	/**
	 * Informs other users that the filesystem was changed by this user.
	 * 
	 * @param type the operation type
	 * @param paramA the first parameter
	 * @param paramB the second parameter
	 */
	protected void alertPDFileOperation(int type, String paramA, String paramB){
		// Create file operation
		PDFileOperation op = PDFileOperation.load(wc, GUIDGen.generateGUIDs(1).remove(0));
		op.setOpUser(user);
		op.setOpType((long)type);
		op.setOpParamA(paramA);
		paramB = paramB == null ? "" : paramB;
		op.setOpParamB(paramB);

		// Attach to history
    	final PDCMSOperation cmso = PDCMSOperation.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    	cmso.setFileOp(op);
    	cmso.setOpType(op.getTypeId());
    	if (cms.opHistory != null){
	    	synchronized (cms.opHistory){
	    		cms.opHistory.add(cmso);
	    	}
    	}	
	}

	/**
	 * Creates a document for this user
	 * 
	 * @param fname document name
	 */
	protected void createPDDocument(String fname){
		File newFile = new File(fname);
		PDDocument pddoc = PDDocument.load(wc, GUIDGen.generateGUIDs(1).remove(0));
		pddoc.setDocumentFileLocation(fname);
		pddoc.setDocumentFileName(newFile.getName());
		pddoc.setDocumentType(getExtension(newFile.getName()));
		//wc.commit();
	}

	/**
	 * 
	 * @param fname the filename
	 * @return the extension of the file
	 */
	private String getExtension(String fname){
		int last = fname.lastIndexOf('.');
		return fname.substring(last);
	}

}


