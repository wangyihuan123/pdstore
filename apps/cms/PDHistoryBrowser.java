package cms;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class PDHistoryBrowser extends JTree {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public PDHistoryBrowser(DefaultMutableTreeNode root){
		super(root);
	}
	
	/*
	private void setList(){
		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setDragEnabled(true); 
	}
	*/
	
	public void refreshTree(CMSOperationList opHistory){

		// Clean tree
		DefaultTreeModel m_model = (DefaultTreeModel) this.getModel();
		DefaultMutableTreeNode target = (DefaultMutableTreeNode) m_model.getRoot(); //searchNode("Root");
		target.removeAllChildren();
		
		// Iterate over opHistory and filter by criterion e.g User, Date, File, Operation, Document
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(Math.random());
		target.add(node);
		
		// Notify tree changed
		m_model.nodeStructureChanged(target);
		
	}

/** 
 * This method takes the node string and 
 * traverses the tree till it finds the node 
 * matching the string. If the match is found  
 * the node is returned else null is returned 
 *  
 * @param nodeStr node string to search for 
 * @return tree node  
 * @author Rahul Sapkal(rahul@javareference.com)
 */ 
public DefaultMutableTreeNode searchNode(String nodeStr) 
{ 
    DefaultMutableTreeNode node = null; 
     
    //Get the enumeration 
    Enumeration en = ((DefaultMutableTreeNode)(this.getModel().getRoot())).breadthFirstEnumeration(); 
     
    //iterate through the enumeration 
    while(en.hasMoreElements()) 
    { 
        //get the node 
        node = (DefaultMutableTreeNode)en.nextElement(); 
         
        //match the string with the user-object of the node 
        if(nodeStr.equals(node.getUserObject().toString())) 
        { 
            //tree node with string found 
            return node;                          
        } 
    } 
     
    //tree node with string node found return null 
    return null; 
} 	


	
}
