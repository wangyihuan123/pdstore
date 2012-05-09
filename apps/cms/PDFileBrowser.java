package cms;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import pdstore.GUIDGen;
import pdstore.dal.PDWorkingCopy;
import cms.dal.PDFileOperation;
import cms.dal.PDHistory;
import cms.dal.PDUser;

public class PDFileBrowser extends JTree {
	
	private static final long serialVersionUID = 1L;
	
	PDWorkingCopy wc;
	PDUser user;
	PDHistory history;
	
	protected static final int
		ADD = 0,
		DELETE = 1,
		COPY = 2,
		MOVE = 3,
		SELECT = 4;
	
	private String DOCUMENT_ROOT;
	
	public PDFileBrowser(DefaultMutableTreeNode node, String docRoot, PDUser user, PDHistory history, PDWorkingCopy wc){
		super(node);	
		DOCUMENT_ROOT = docRoot;
		this.user = user;
		this.history = history;
		this.wc = wc;
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

}


