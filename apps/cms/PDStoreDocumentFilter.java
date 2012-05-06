
package cms;

import javax.swing.event.DocumentEvent;
import javax.swing.text.*;
import javax.swing.text.DocumentFilter.FilterBypass;

import cms.dal.PDCharacter;
import cms.dal.PDDocument;
import cms.dal.PDHistory;
import cms.dal.PDUser;
import cms.dal.PDOperation;

import pdstore.GUID;
import pdstore.GUIDGen;
import pdstore.dal.PDWorkingCopy;

import java.util.ArrayList;
import java.util.Collection;

public class PDStoreDocumentFilter extends DocumentFilter {

	protected static final int REMOVE = 0;
	protected static final int INSERT = 1;
	protected static final int REPLACE = 2;	
	
	private boolean filter = true;
	
	PDUser user;
	PDHistory history;
	PDWorkingCopy wc;
	GUID transaction;
	
    public PDStoreDocumentFilter(PDWorkingCopy wc, PDUser user, PDHistory history) {
		this.user = user;
		this.history = history;
		this.wc = wc;
		// TODO: get username from PDStore and listen to changes in name
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
    	wc.commit();
    	
    	// Create operation
    	PDOperation op = PDOperation.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    	op.setOpDocument(pddoc);
    	op.setOpUser(user);
    	op.setOpType((long)REMOVE);
    	op.setOpOffset((long)offset);
    	op.setOpLength((long)length);
    	// Attach to history
		history.addOperation(op); // needs to be some kind of linked list
		// Commit
		wc.commit();
    }    
    
    public void PDInsertString(FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
    	//System.out.println(user.getName()+" Recieved INSERT: offset: "+offset+", str: \n'"+str+"'");
    	
    	// Get current document
    	PDDocument pddoc = getCurrentDocument();
    	wc.commit();
    	
    	// Create operation
    	PDOperation op = PDOperation.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    	op.setOpDocument(pddoc);
    	op.setOpUser(user);
    	op.setOpType((long)INSERT);
    	op.setOpOffset((long)offset);
    	op.setOpString(str);
    	// Attach to history
		history.addOperation(op); // needs to be some kind of linked list
		// Commit
		wc.commit();
    }  

    public void PDReplace(FilterBypass fb, int offset, int length, String str, AttributeSet attr) throws BadLocationException {
    	//System.out.println(user.getName()+" Recieved REPLACE: offset: "+offset+", length: "+length+", str: \n'"+str+"'");
    	
    	// Get current document
    	PDDocument pddoc = getCurrentDocument();
    	wc.commit();
    	
    	// Create operation
    	PDOperation op = PDOperation.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    	op.setOpDocument(pddoc);
    	op.setOpUser(user);
    	op.setOpType((long)REPLACE);
    	op.setOpOffset((long)offset);
    	op.setOpLength((long)length);
    	op.setOpString(str);
    	// Attach to history
		history.addOperation(op); // needs to be some kind of linked list
		// Commit
		wc.commit();
		//System.out.println("THREAD: "+Thread.currentThread().getId()+" OP TYPE: "+op.getOpType());
    }
    
    @Deprecated
    private void addCharacters(PDDocument pddoc, int offset, int length, String str){
    	Collection<PDCharacter> chars = pddoc.getCharacters(); //not null
    	ArrayList<PDCharacter> list = new ArrayList<PDCharacter>(str.length());
    	PDCharacter c;
    	
    	if (chars.size() == 0){
	    	// Convert string to PDCharacters
	    	for (int i = 0; i < str.length(); i++){
	    		c = PDCharacter.load(wc, GUIDGen.generateGUIDs(1).remove(0));
	    		c.setCharValue(str.charAt(i));
	    		list.add(c);
	    	}
	    	
	    	// Set previous and next chars
	    	for (int i = 0; i < list.size(); i++){
	    		if (i > 0){
	    			// set previous
	    			list.get(i).setPrevChar(list.get(i-1));
	    		}
	    		if (i < (list.size() - 1)){
	    			// set next
	    			list.get(i).setNextChar(list.get(i+1));
	    		}
	    	}  
	    	
	    	pddoc.addCharacters(list);
    	}
    	
    }
    
    public void setFilter(boolean on) {
    	filter = on;
    }
    
    private PDDocument getCurrentDocument(){
    	
    	PDDocument pddoc = user.getCurrentDocument();
    	
    	if (pddoc == null){
    		// Need to create a document and set as current resource
    		pddoc = PDDocument.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    		user.setCurrentDocument(pddoc);
    	} else {
    		//System.out.println("Document exists");
    	}   
    	
    	return pddoc;
    }
    

}
