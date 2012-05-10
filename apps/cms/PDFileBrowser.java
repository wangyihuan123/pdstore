package cms;

import java.io.File;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import pdstore.GUIDGen;
import pdstore.dal.PDWorkingCopy;
import cms.dal.PDDocument;
import cms.dal.PDFileOperation;
import cms.dal.PDHistory;
import cms.dal.PDUser;

public class PDFileBrowser extends JTree {

	private static final long serialVersionUID = 8740726184571526994L;
	
	PDWorkingCopy wc;
	PDUser user;
	PDHistory history;
	TreePath recentlyAdded;

	protected static final int
	ADD = 0,
	DELETE = 1,
	COPY = 2,
	MOVE = 3,
	SELECT = 4;

	protected PDFileBrowser(DefaultMutableTreeNode node, String docRoot, PDUser user, PDHistory history, PDWorkingCopy wc){
		super(node);	
		this.user = user;
		this.history = history;
		this.wc = wc;
	}

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
	protected static void deleteFileSystem(String filename){
		File deletefile = new File(filename);
		deletefile.delete();
	}

	/**
	 * Method to delete the file from file system
	 * @param filename
	 */
	protected static void addFileSystem(String filename){
		File newfile = new File(filename);
		newfile.mkdir();

	}

	/**
	 * Method to move the file from original position to destination position
	 * @param original
	 * @param destination
	 */
	protected static void moveFileSystem(String original,String destination){
		addFileSystem(destination);
		deleteFileSystem(original);
	}

	/**
	 * Method to copy the file from original position to destination position
	 * @param original
	 * @param destination
	 */
	protected static void copyFileSystem(String original,String destination){
		addFileSystem(destination);

	}

	protected void alertPDFileOperation(int type, String paramA, String paramB){
		// Create file operation
		PDFileOperation op = PDFileOperation.load(wc, GUIDGen.generateGUIDs(1).remove(0));
		op.setOpUser(user);
		op.setOpType((long)type);
		op.setOpParamA(paramA);
		paramB = paramB == null ? "" : paramB;
		op.setOpParamB(paramB);

		// Attach to history
		history.addFileOperation(op); // needs to be some kind of linked list
		// Commit
		wc.commit();		
	}

	protected void createPDDocument(String fname){
		File newFile = new File(fname);
		PDDocument pddoc = PDDocument.load(wc, GUIDGen.generateGUIDs(1).remove(0));
		pddoc.setDocumentFileLocation(fname);
		pddoc.setDocumentFileName(newFile.getName());
		pddoc.setDocumentType(getExtension(newFile.getName()));
		wc.commit();
	}

	private String getExtension(String fname){
		int last = fname.lastIndexOf('.');
		return fname.substring(last);
	}

}


