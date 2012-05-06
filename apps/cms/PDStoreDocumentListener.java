package cms;

import java.util.Collection;
import java.util.List;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import cms.dal.PDOperation;
import cms.dal.PDUser;

import pdstore.GUID;
import pdstore.dal.PDInstance;
import pdstore.generic.PDChange;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListener;

public class PDStoreDocumentListener implements PDListener<GUID, Object, GUID> {

	private ContentManagementSystem cms;
	private GUID role2;
	
	public PDStoreDocumentListener(ContentManagementSystem cms, GUID role2) {
		super();
		this.cms = cms;
		this.role2 = role2;
	}

	@Override
	public void transactionCommitted(
			List<PDChange<GUID, Object, GUID>> transaction,
			List<PDChange<GUID, Object, GUID>> matchedChanges, PDCoreI<GUID, Object, GUID> core) {
		//System.out.println("###### "+cms.getTitle()+" #####");
		for (PDChange<GUID, Object, GUID> change : transaction) {
			//System.out.println("Change: " + change);
			if (change.getRole2().equals(role2)){
				//System.out.println("Found Operation");
				PDOperation op = PDOperation.load(cms.wc, (GUID)change.getInstance1());
				performOperation(op);
			}
		}
	}
	
	private void performOperation(PDOperation op){
		
		AbstractDocument doc = (AbstractDocument)cms.textEditor.getStyledDocument();
		PDStoreDocumentFilter filter = (PDStoreDocumentFilter) doc.getDocumentFilter();
		
		while (op.getOpType() == null); // seems the get method can return null at first
		
		long type = op.getOpType();
		long offset = op.getOpOffset();
		long length = op.getOpLength();
		String str = op.getOpString();
		
		while (op.getOpUser() == null); // method can return null at first
		PDUser user = op.getOpUser();
		String username = user.getName();
	
		// Do something appropriate given the OpType
		filter.setFilter(false);
		try {
			
			int oldPos, newPos;
			
			switch ((int)type){	
				case PDStoreDocumentFilter.REMOVE:
					doc.remove((int) offset, (int) length);
					// Broadcast change to other users
					//broadcastCaretChange((int)-length);
					// Update caret for this user
					if (username.equals(cms.user.getName())){ 
						//cms.textEditor.setCaretPosition((int)offset);
						cms.textEditor.setCaretPosition((int) ((cms.textEditor.getCaretPosition() - length)));
					}
					break;
				case PDStoreDocumentFilter.INSERT:
					doc.insertString((int) offset, str, null);
					// Broadcast change to other users
					oldPos = cms.textEditor.getCaretPosition();
					newPos = oldPos + str.length();
					//broadcastCaretChange(oldPos, newPos);
					// Update caret for this user
					if (username.equals(cms.user.getName())){
						cms.textEditor.setCaretPosition(newPos);
					} 						
					break;
				case PDStoreDocumentFilter.REPLACE:	
					doc.replace((int) offset, (int) length, str, null);
					// Broadcast change to other users
					oldPos = cms.textEditor.getCaretPosition();
					newPos = oldPos + str.length();
					//broadcastCaretChange(oldPos, newPos);
					// Update caret for this user
					if (username.equals(cms.user.getName())){
						cms.textEditor.setCaretPosition(newPos);
					}				
					break;		
			}
		} catch (BadLocationException e){
			System.out.println("Bad location in DoucmentListener");
		}
		filter.setFilter(true);
	
	}

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
		cms.wc.commit();		
	}
	
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
		cms.wc.commit();
	}

	@Override
	public Collection<PDChange<GUID, Object, GUID>> getMatchingTemplates() {
		// TODO Auto-generated method stub
		return null;
	}
}
