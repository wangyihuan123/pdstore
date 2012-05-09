package cms;

import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import cms.dal.PDDocument;
import cms.dal.PDFileOperation;
import cms.dal.PDUser;

import pdstore.GUID;
import pdstore.dal.PDInstance;
import pdstore.generic.PDChange;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListener;

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
		//System.out.println("###### "+cms.getTitle()+" #####");
		for (PDChange<GUID, Object, GUID> change : transaction) {
			//System.out.println("Change: " + change);
			if (change.getRole2().equals(role2)){
				//System.out.println("Found Operation");
				PDFileOperation op = PDFileOperation.load(cms.wc, (GUID)change.getInstance1());
				PDUser otherUser = op.getOpUser();
				if (otherUser == null){
					return;
				}
				// Check to see if the operation was made by this user
				if (otherUser.getName().equals(cms.user.getName())){
					performOperation(op);
				} else {
					refreshView(op);
				}
			}
		}
	}

	private void performOperation(PDFileOperation op){
		
		//while (op.getOpType() == null); // seems the get method can return null at first
		if (op.getOpType() == null){
			return;
		}
		long type = op.getOpType();
		String paramA = op.getOpParamA();
		String paramB = op.getOpParamB();

		// Do something appropriate given the OpType
		switch ((int)type){	
		case PDFileBrowser.ADD:
			File selNode = new File(cms.DOCUMENT_ROOT+"/"+paramA);
			//String parent = selNode.getParent();
			//DefaultTreeModel model = (DefaultTreeModel) (cms.tree.getModel());
			//DefaultMutableTreeNode newFile = new DefaultMutableTreeNode(paramA);
			//cms.tree.addNodeToTree(newFile, paramA);
			break;
		case PDFileBrowser.DELETE:
			// cms.tree.deleteFile(paramA);	
			break;
		case PDFileBrowser.COPY:	
			// cms.tree.copyFile(paramA, paramB);
			break;		
		case PDFileBrowser.MOVE:	
			// cms.tree.moveFile(paramA, paramB);
			break;			
		}

	}

	private void refreshView(PDFileOperation op){
		cms.tree.refresh();
		//while (op.getOpType() == null); // seems the get method can return null at first
		if (op.getOpType() == null){
			return;
		}
		long type = op.getOpType();
		String paramA = op.getOpParamA();	
		switch ((int)type){
			case PDFileBrowser.SELECT:	
				// TODO: Highlight other user's selection in addition to main user selection
				// cms.tree.selectFile(paramA);
				break;			
		}
		
	}

	@Override
	public Collection<PDChange<GUID, Object, GUID>> getMatchingTemplates() {
		return null;
	}
}
