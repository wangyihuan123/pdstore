package cms;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Enumeration; 

import javax.swing.tree.DefaultMutableTreeNode;
import cms.dal.PDFileOperation;
import cms.dal.PDUser;

import pdstore.GUID;
import pdstore.generic.PDChange;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListener;

/**
 * Listens for file operations made by multiple users and updates the file tree view.
 * 
 * @author Sina Masoud-Ansari (s.ansari@auckland.ac.nz)
 *
 */
public class PDFileBrowserListener implements PDListener<GUID, Object, GUID> {

	private ContentManagementSystem cms;
	private GUID role2;

	public PDFileBrowserListener(ContentManagementSystem cms, GUID role2) {
		super();
		this.cms = cms;
		this.role2 = role2;
	}

	@Override
	public void transactionCommitted(
			List<PDChange<GUID, Object, GUID>> transaction,
			List<PDChange<GUID, Object, GUID>> matchedChanges, PDCoreI<GUID, Object, GUID> core) {
		Object[] copy = transaction.toArray();
		for (Object o : copy) {
			PDChange<GUID, Object, GUID> change = (PDChange<GUID, Object, GUID>) o;
			if (change.getRole2().equals(role2)){
				PDFileOperation op = PDFileOperation.load(cms.wc, (GUID)change.getInstance1());
				PDUser otherUser = op.getOpUser();
				if (otherUser == null){
					return;
				}
				performOperation(op, otherUser);
			}
		}
	}

	/**
	 * Perform the operation. If it is a filesystem operation, only the owner should make the change, 
	 * the others just update their view.
	 * 
	 * @param op the operation to perform
	 * @param otherUser the user who initiated this operation
	 */
	private void performOperation(PDFileOperation op, PDUser otherUser){
		
		//while (op.getOpType() == null); // seems the get method can return null at first
		if (op.getOpType() == null){
			return;
		}
		long type = op.getOpType();
		String paramA = op.getOpParamA();
		String paramB = op.getOpParamB();

		// Do something appropriate given the OpType
		File fileA, fileB;
		DefaultMutableTreeNode nodeA, nodeB;
		switch ((int)type){	
		case PDFileBrowser.ADD:
			fileA = new File(cms.DOCUMENT_ROOT+"/"+paramA);
			nodeA = searchNode(cms.tree, fileA.getParent());
			cms.tree.addNodeToTree(nodeA, fileA.getName());
			if (otherUser.getName().equals(cms.user.getName())){
				cms.tree.addFileSystem(fileA.getAbsolutePath());
				cms.tree.scrollAndSelect();
			}
			break;
		case PDFileBrowser.DELETE:
			fileA = new File(cms.DOCUMENT_ROOT+"/"+paramA);
			nodeA = searchNode(cms.tree, fileA.getAbsolutePath());
			cms.tree.deleteNodeFromTree(nodeA);		
			if (otherUser.getName().equals(cms.user.getName())){
				cms.tree.deleteFileSystem(fileA.getAbsolutePath());
			}			
			break;
		case PDFileBrowser.COPY:	
			fileA = new File(cms.DOCUMENT_ROOT+"/"+paramA);
			fileB = new File(cms.DOCUMENT_ROOT+"/"+paramB);
			nodeA = searchNode(cms.tree, fileA.getName()); //source file
			nodeB = searchNode(cms.tree, fileB.getParent()); //destination folder
			cms.tree.copyNodeToTree(nodeA, nodeB);
			if (otherUser.getName().equals(cms.user.getName())){
				cms.tree.copyFileSystem(fileA.getAbsolutePath(), fileB.getAbsolutePath());
			}			
			break;		
		case PDFileBrowser.MOVE:	
			fileA = new File(cms.DOCUMENT_ROOT+"/"+paramA);
			fileB = new File(cms.DOCUMENT_ROOT+"/"+paramB);
			nodeA = searchNode(cms.tree, fileA.getName()); //source file
			nodeB = searchNode(cms.tree, fileB.getParent()); //destination folder
			cms.tree.moveNodeToTree(nodeA, nodeB);
			if (otherUser.getName().equals(cms.user.getName())){
				cms.tree.moveFileSystem(fileA.getAbsolutePath(), fileB.getAbsolutePath());
			}			
			break;			
		}

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
    public DefaultMutableTreeNode searchNode(PDFileBrowser tree, String nodeStr) 
    { 
        DefaultMutableTreeNode node = null; 
         
        //Get the enumeration 
        Enumeration en = ((DefaultMutableTreeNode)(tree.getModel().getRoot())).breadthFirstEnumeration(); 
         
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

	@Override
	public Collection<PDChange<GUID, Object, GUID>> getMatchingTemplates() {
		return null;
	}
}
