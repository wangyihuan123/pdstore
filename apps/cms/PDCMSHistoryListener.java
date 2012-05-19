package cms;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Enumeration; 

import javax.swing.tree.DefaultMutableTreeNode;

import cms.dal.PDCMSOperation;
import cms.dal.PDDocument;
import cms.dal.PDDocumentOperation;
import cms.dal.PDFileOperation;
import cms.dal.PDHistory;
import cms.dal.PDUser;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.generic.PDChange;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListener;

public class PDCMSHistoryListener implements PDListener<GUID, Object, GUID> {

	private ContentManagementSystem cms;
	private GUID role2;

	public PDCMSHistoryListener(ContentManagementSystem cms, GUID role2) {
		super();
		this.cms = cms;
		this.role2 = role2;
	}

	@Override
	public void transactionCommitted(
			List<PDChange<GUID, Object, GUID>> transaction,
			List<PDChange<GUID, Object, GUID>> matchedChanges, PDCoreI<GUID, Object, GUID> core) {
		for (PDChange<GUID, Object, GUID> change : transaction) {
			if (change.getRole2().equals(role2)){
				//System.out.println(transaction.toString());
				
				PDHistory history = PDHistory.load(cms.wc, (GUID)change.getInstance1());
				if (history == null){
					return;
				}				
				PDCMSOperation op = history.getCMSOperation();
				if (op == null){
					return;
				}					
				
				//cms.refreshHistory();
				// This is used for future user filtering / highlighting of other users operations
				GUID typeID = op.getOpType().getId();
				if (typeID.equals(PDDocumentOperation.typeId)) {
					PDDocumentOperation dop = op.getDocumentOp();
				} else if (typeID.equals(PDFileOperation.typeId)) {
					PDFileOperation fop = op.getFileOp();
				} else {
					return;
				}
			}
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
    private DefaultMutableTreeNode searchNode(PDFileBrowser tree, String nodeStr) 
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
