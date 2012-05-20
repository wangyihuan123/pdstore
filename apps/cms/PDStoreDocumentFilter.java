
package cms;

import javax.swing.text.*;

import cms.dal.PDCMSOperation;
import cms.dal.PDDocument;
import cms.dal.PDDocumentOperation;
import cms.dal.PDHistory;
import cms.dal.PDUser;
import pdstore.GUID;
import pdstore.GUIDGen;
import pdstore.dal.PDWorkingCopy;

/**
 * Filters Document operations and sends them to PDStore where they are implemented by each CMS instance.
 * 
 * @author Sina Masoud-Ansari (s.ansari@auckland.ac.nz)
 *
 */
public class PDStoreDocumentFilter extends DocumentFilter {

	protected static final int
		REMOVE = 0,
		INSERT = 1,
		REPLACE = 2;	
	
	private boolean filter = true;
	
	PDUser user;
	PDHistory history;
	PDWorkingCopy wc;
	GUID transaction;
	ContentManagementSystem cms;
	
    public PDStoreDocumentFilter(PDWorkingCopy wc, PDUser user, PDHistory history, ContentManagementSystem cms) {
		this.user = user;
		this.history = history;
		this.wc = wc;
		this.cms = cms;
    }
    
    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
    	if (filter){
	    		PDRemove(fb, offset, length); 		
    	} else {
    			super.remove(fb, offset, length);
    	}
    	
    }
    
    @Override
    public void insertString(FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
    	if (filter){
	    		PDInsertString(fb, offset, str, attr);
    	} else {
    			super.insertString(fb, offset, str, attr);
    	}
    }
    
    @Override
    public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attr) throws BadLocationException {
    	if (filter){
    			PDReplace(fb, offset, length, str, attr);
    	} else {
    			super.replace(fb, offset, length, str, attr);
    	}
    	
    }
    
    public void PDRemove(FilterBypass fb, int offset, int length) throws BadLocationException {
    	//System.out.println(user.getName()+" Recieved REMOVE: offset: "+offset+", length: "+length);
    	
    	// Get current document
    	PDDocument pddoc = getCurrentDocument();
    	//wc.commit();

    	// Create operation
    	PDDocumentOperation op = PDDocumentOperation.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    	op.setOpDocument(pddoc);
    	op.setOpUser(user);
    	op.setOpType((long)REMOVE);
    	op.setName("Remove: "+length);
    	op.setOpOffset((long)offset);
    	op.setOpLength((long)length);
    	// Attach to history
    	final PDCMSOperation cmso = PDCMSOperation.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    	cmso.setDocumentOp(op);
    	cmso.setOpType(op.getTypeId());
    	synchronized (cms.opHistory) {
    		cms.opHistory.add(cmso);
    	}
    }    
    
    public void PDInsertString(FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
    	//System.out.println(user.getName()+" Recieved INSERT: offset: "+offset+", str: \n'"+str+"'");
    	
    	// Get current document
    	PDDocument pddoc = getCurrentDocument();
    	//wc.commit();
    	
    	// Create operation
    	PDDocumentOperation op = PDDocumentOperation.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    	op.setOpDocument(pddoc);
    	op.setOpUser(user);
    	op.setOpType((long)INSERT);
    	op.setName("Insert: "+str);
    	op.setOpOffset((long)offset);
    	op.setOpString(str);
    	// Attach to history
    	final PDCMSOperation cmso = PDCMSOperation.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    	cmso.setDocumentOp(op);
    	cmso.setOpType(op.getTypeId());
    	synchronized (cms.opHistory) {
    		cms.opHistory.add(cmso);
    	}

    }  

    public void PDReplace(FilterBypass fb, int offset, int length, String str, AttributeSet attr) throws BadLocationException {
    	//System.out.println(user.getName()+" Recieved REPLACE: offset: "+offset+", length: "+length+", str: \n'"+str+"'");
    	
    	// Get current document
    	PDDocument pddoc = getCurrentDocument();
    	//wc.commit();
    	
    	// Create operation
    	PDDocumentOperation op = PDDocumentOperation.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    	op.setOpDocument(pddoc);
    	op.setOpUser(user);
    	op.setOpType((long)REPLACE);
    	op.setName("Replace: "+str);
    	op.setOpOffset((long)offset);
    	op.setOpLength((long)length);
    	op.setOpString(str);
    	// Attach to history
    	PDCMSOperation cmso = PDCMSOperation.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    	cmso.setDocumentOp(op);
    	cmso.setOpType(op.getTypeId());
    		
    	synchronized (cms.opHistory) {
    		cms.opHistory.add(cmso);
    	}

    }
    
    public synchronized void setFilter(boolean b) {
    	filter = b;
    }
    
    protected PDDocument getCurrentDocument(){
    	
    	PDDocument pddoc = user.getCurrentDocument();
    	
    	if (pddoc == null){
    		// Need to create a document and set as current resource
    		pddoc = PDDocument.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    		pddoc.setDocumentFileName(user.getName()+".temp");
    		user.setCurrentDocument(pddoc);
    	} 
    	
    	return pddoc;
    }
    

}
