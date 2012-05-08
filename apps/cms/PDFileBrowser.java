package cms;

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
	
	public void addNode(DefaultMutableTreeNode selNode, String filename){
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


