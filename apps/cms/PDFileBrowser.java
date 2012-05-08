package cms;

import java.io.File;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class PDFileBrowser extends JTree {
	
	private static final long serialVersionUID = 1L;
	
	private String DOCUMENT_ROOT;
	
	public PDFileBrowser(DefaultMutableTreeNode node, String docRoot){
		super(node);	
		DOCUMENT_ROOT = docRoot;
	}
	
	public void addNodeToTree(DefaultMutableTreeNode selNode, String filename){
		// refresh tree
		DefaultTreeModel m_model = (DefaultTreeModel) (this.getModel()); 
		if (selNode != null) 
		{ 
			// add new node as a child of a selected node at the end 
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(filename); 
			m_model.insertNodeInto(newNode, selNode, selNode.getChildCount()); 

			//make the node visible by scroll to it 
			TreeNode[] nodes = m_model.getPathToRoot(newNode); 
			TreePath path = new TreePath(nodes); 
			this.scrollPathToVisible(path); 

			//select the newly added node 
			this.setSelectionPath(path); 


		} 		
	}
	
	
	/**
	 * MOVE node method
	 * @param selNode
	 */
	
	public void moveNodeToTree(DefaultMutableTreeNode orgNode,DefaultMutableTreeNode desNode){
		
		// update the tree after clicking move button
		addNodeToTree(desNode,orgNode.toString());
		deleteNodeFromTree(orgNode);
		
	}
	
	/**
	 * COPY node method
	 * @param selNode
	 */
	
public void copyNodeToTree(DefaultMutableTreeNode orgNode,DefaultMutableTreeNode desNode){
		
		// update the tree after clicking move button
		addNodeToTree(desNode,orgNode.toString());
		
	}

	
	public void deleteNodeFromTree(DefaultMutableTreeNode selNode){
		
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
	private static void deleteFilesSystem(String filename){
		File deletefile = new File(filename);
		deletefile.delete();
	}

	/**
	 * Method to delete the file from file system
	 * @param filename
	 */
	private static void addFilesSystem(String filename){
		File newfile = new File(filename);
		newfile.mkdir();
		
	}
	
	/**
	 * Method to move the file from original position to destination position
	 * @param original
	 * @param destination
	 */
	private static void moveFilesSystem(String original,String destination){
		addFilesSystem(destination);
		deleteFilesSystem(original);
	}
	
	/**
	 * Method to copy the file from original position to destination position
	 * @param original
	 * @param destination
	 */
	private static void copyFilesSystem(String original,String destination){
		addFilesSystem(destination);
		
	}
	
	
	// Refreshes view of DOCUMENT_ROOT
	public void refresh(){
		
		
		
		
	}
	
	// Fname is relative to DOCUMENT_ROOT
	public void addNode(String fname){
		
	}
	
	// Fname is relative to DOCUMENT_ROOT
	public void removeNode(String fname){
		
	}

	

}


