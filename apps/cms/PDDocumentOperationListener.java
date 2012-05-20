package cms;

import java.util.Collection;
import java.util.List;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;

import cms.dal.PDDocument;
import cms.dal.PDDocumentOperation;
import cms.dal.PDUser;

import pdstore.GUID;
import pdstore.PDStoreException;
import pdstore.dal.PDInstance;
import pdstore.generic.PDChange;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListener;
/**
 * Used to perform synchronisation between collaborators on documents.
 * Now replaced by PDSMSHistoryListener.
 * 
 * @author Sina Masoud-Ansari (s.ansari@auckland.ac.nz)
 *
 */
@Deprecated
public class PDDocumentOperationListener implements PDListener<GUID, Object, GUID> {

	private ContentManagementSystem cms;
	private GUID role2;

	public PDDocumentOperationListener(ContentManagementSystem cms, GUID role2) {
		super();
		this.cms = cms;
		this.role2 = role2;
	}

	@Override
	public void transactionCommitted (
			List<PDChange<GUID, Object, GUID>> transaction,
			List<PDChange<GUID, Object, GUID>> matchedChanges, PDCoreI<GUID, Object, GUID> core) {
		Object[] copy = transaction.toArray();
		for (Object o : copy) {
			PDChange<GUID, Object, GUID> change = (PDChange<GUID, Object, GUID>) o;
			//System.out.println("Change: " + change);
			if (change.getRole2().equals(role2)){
				//System.out.println("Found Operation");
				//while(PDDocumentOperation.load(cms.wc, (GUID)change.getInstance1()) == null);
				PDDocumentOperation op = PDDocumentOperation.load(cms.wc, (GUID)change.getInstance1());
				if (op == null){
					//System.err.println("User: "+cms.user.getName()+" OpListener: Op was null");
					return;
				} else {
					//System.out.println("User: "+cms.user.getName()+" OpListener: Op was recieved");
				}
			
				// Check if they are working on the same document
				PDDocument userDoc = cms.user.getCurrentDocument();
				PDUser otherUser = op.getOpUser();
				//while(otherUser == null);
				
				if (otherUser == null){
					//System.err.println("User: "+cms.user.getName()+" OpListener: otherUser was null");
					return;
				} else {
					//System.out.println("User: "+cms.user.getName()+" OpListener: otherUser was recieved");
				}
				
				PDDocument otherDoc = otherUser.getCurrentDocument();
				if (otherDoc == null){
					return;
				} else if (userDoc == null)  {
					return;
				} else if (userDoc.getId().equals(otherDoc.getId())){
					// Only perform ops when users are working on the same document
					try {
						performOperation(op);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						//throw new NullPointerException();
					}
				}
			}
		}
	}

	private void performOperation(PDDocumentOperation op) throws BadLocationException, PDStoreException {

		AbstractDocument doc = (AbstractDocument)cms.textEditor.getDocument();
		PDStoreDocumentFilter filter = (PDStoreDocumentFilter) doc.getDocumentFilter();
		if (doc == null || filter == null){
			return;
		}

		//while (op.getOpType() == null); // seems the get method can return null at first
		
		if (op.getOpType() == null){
			return;
		}
		
		long type = op.getOpType();
		long offset = op.getOpOffset();
		long length = 0;
		String str = op.getOpString();

		// Do something appropriate given the OpType
		filter.setFilter(false);
		//try {
		switch ((int)type){	
		case PDStoreDocumentFilter.REMOVE:
			length = op.getOpLength();
			doc.remove((int) offset, (int) length);
			break;
		case PDStoreDocumentFilter.INSERT:
			doc.insertString((int) offset, str, null);					
			break;
		case PDStoreDocumentFilter.REPLACE:	
			length = op.getOpLength();
			doc.replace((int) offset, (int) length, str, null);			
			break;		
		}
		//} catch (BadLocationException e){
		//System.out.println("Bad location in DoucmentListener");
		//}
		filter.setFilter(true);

	}

	/*
	 * Broadcast methods were used to keep relative distances between carets but it is currently unstable with many bad location errors.
	 */

	@Deprecated
	private void broadcastCaretChange(int length){
		int change = length;
		Collection<PDInstance> users = cms.wc.getAllInstancesOfType(PDUser.typeId);
		int pos;
		for (Object o : users){
			PDUser u = (PDUser) o;
			// Avoid updating the owner twice
			if (!u.getName().equals(cms.user.getName())) {
				pos = u.getCaretPosition().intValue() + change;
				pos = pos < 0 ? 0 : pos;
				u.setCaretPosition(new Long(pos));
			}
		}
		//cms.wc.commit();		
	}

	@Deprecated
	private void broadcastCaretChange(int oldPos, int newPos){
		int change = newPos - oldPos;
		Collection<PDInstance> users = cms.wc.getAllInstancesOfType(PDUser.typeId);
		int pos;
		for (Object o : users){
			PDUser u = (PDUser) o;
			// Avoid updating the owner twice
			if (!u.getName().equals(cms.user.getName())) {
				pos = u.getCaretPosition().intValue() + change;
				pos = pos < 0 ? 0 : pos;
				u.setCaretPosition(new Long(pos));
			}
		}
		//cms.wc.commit();
	}

	@Override
	public Collection<PDChange<GUID, Object, GUID>> getMatchingTemplates() {
		return null;
	}
}
