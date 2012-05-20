
package cms;

import javax.swing.SwingUtilities;
import javax.swing.text.*;

import cms.dal.PDCMSOperation;
import cms.dal.PDDocument;
import cms.dal.PDDocumentOperation;
import cms.dal.PDHistory;
import cms.dal.PDUser;
import pdstore.GUID;
import pdstore.GUIDGen;
import pdstore.dal.PDWorkingCopy;

public class PDStoreDocumentFilter extends DocumentFilter {

	protected static final int
		REMOVE = 0,
		INSERT = 1,
		REPLACE = 2;	
	
	private boolean filter = true;
	private boolean replay = false;
	
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
	    	//try{	
	    		PDRemove(fb, offset, length);
			//} catch (Exception e){
			//	e.printStackTrace();
			//}    		
    	} else {
    		//try {
    			super.remove(fb, offset, length);
    		//} catch (BadLocationException e){
    		//	System.out.println("Bad location in DocumentFilter");
    		//}	
    	}
    	
    }
    
    @Override
    public void insertString(FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
    	if (filter){
	    	//try{
	    		PDInsertString(fb, offset, str, attr);
			//} catch (Exception e){
			//	e.printStackTrace();
			//}
    	} else {
    		//try {
    			super.insertString(fb, offset, str, attr);
    		//} catch (BadLocationException e){
    		//	System.out.println("Bad location in DocumentFilter");
    		//}	
    	}
    }
    
    @Override
    public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attr) throws BadLocationException {
    	if (filter){
    		//try {
    			PDReplace(fb, offset, length, str, attr);
    		//} catch (Exception e){
    			//e.printStackTrace();
    		//}
    	} else {
    		//try {
    			super.replace(fb, offset, length, str, attr);
    		//} catch (Exception e){
    			//System.out.println("Bad location in DocumentFilter");
    		//}
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
    	/*
    	SwingUtilities.invokeLater(new Runnable (){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				cms.opHistory.add(cmso);
			}
    		
    	});
    	*/
    	//history.addCMSOperation(cmso);    	
		//history.addDocumentOperation(op); // needs to be some kind of linked list
		// Commit
		//wc.commit();
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
    	/*
    	SwingUtilities.invokeLater(new Runnable (){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				cms.opHistory.add(cmso);
			}
    		
    	});
    	*/
    	
    	//history.addCMSOperation(cmso);    	
		//history.addDocumentOperation(op); // needs to be some kind of linked list
		// Commit
		//wc.commit();
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
    	
    
    	/*
    	SwingUtilities.invokeLater(new Runnable (){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
    		
    	});
    	*/
    	//history.addCMSOperation(cmso);
		//history.addDocumentOperation(op); // needs to be some kind of linked list
		// Commit
		//wc.commit();
		//System.out.println("THREAD: "+Thread.currentThread().getId()+" OP TYPE: "+op.getOpType());
    }
    
    public synchronized void setFilter(boolean b) {
    	filter = b;
    }
   
    public synchronized void setReplay(boolean b) {
    	replay = b;
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
