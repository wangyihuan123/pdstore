package cms;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Enumeration; 

import javax.swing.SwingUtilities;
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

/**
 * Listen to changes in the history of a CMS and update the CMS as appropriate.
 * 
 * @author Sina Masoud-Ansari (s.ansari@auckland.ac.nz)
 *
 */
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
		
		Object[] copy = transaction.toArray();
		for (Object o : copy) {
			PDChange<GUID, Object, GUID> change = (PDChange<GUID, Object, GUID>) o;
			if (change.getRole2().equals(role2)){
				//System.out.println(transaction.toString());
				
				PDHistory history = PDHistory.load(cms.wc, (GUID)change.getInstance1());
				if (history == null){
					//System.err.println("User: "+cms.user.getName()+" HistoryListener: history was null");
					return;
				} else {
					//System.out.println("User: "+cms.user.getName()+" HistoryListener: history was recieved");
				}	
				
				PDCMSOperation op = history.getCMSOperation();
				if (op == null){
					//System.err.println("User: "+cms.user.getName()+" HistoryListener: op was null");
					return;
				} else {
					//System.out.println("User: "+cms.user.getName()+" HistoryListener: op was recieved");
				}
				
		    	synchronized (cms.opHistory) {
		    		cms.updateDocument();
		    		cms.refreshHistory();
		    	}
						
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

	
    @Override
	public Collection<PDChange<GUID, Object, GUID>> getMatchingTemplates() {
		return null;
	}
}
