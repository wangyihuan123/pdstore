package cms;

import java.util.Collection;
import java.util.List;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import cms.dal.PDOperation;
import cms.dal.PDUser;

import pdstore.GUID;
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
			switch ((int)type){
				case PDStoreDocumentFilter.REMOVE:
					doc.remove((int) offset, (int) length);
					// Update caret for this user
					if (username.equals(cms.user.getName())){
						cms.textEditor.setCaretPosition((int)offset);
					}
					break;
				case PDStoreDocumentFilter.INSERT:
					doc.insertString((int) offset, str, null);
					// Update caret for this user
					if (username.equals(cms.user.getName())){
						cms.textEditor.setCaretPosition(cms.textEditor.getCaretPosition()+str.length());
					}							
					break;
				case PDStoreDocumentFilter.REPLACE:	
					doc.replace((int) offset, (int) length, str, null);
					// Update caret for this user
					if (username.equals(cms.user.getName())){
						cms.textEditor.setCaretPosition(cms.textEditor.getCaretPosition()+str.length());
					}						
					break;		
			}
		} catch (BadLocationException e){
			e.printStackTrace();
		}
		filter.setFilter(true);
	
	}

	@Override
	public Collection<PDChange<GUID, Object, GUID>> getMatchingTemplates() {
		// TODO Auto-generated method stub
		return null;
	}
}
