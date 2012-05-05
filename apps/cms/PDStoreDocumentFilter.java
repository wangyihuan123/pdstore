
package cms;

import javax.swing.text.*;

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
    
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
    	// don't do anything
    	System.out.println(user.getName()+" Recieved REMOVE: offset: "+offset+", length: "+length);
    	
    }
    
    public void insertString(FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
    	// don't do anything
    	System.out.println(user.getName()+" Recieved INSERT: offset: "+offset+", str: \n'"+str+"'");
    }
    
    public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attr) throws BadLocationException {
    	// don't do anything
    	System.out.println(user.getName()+" Recieved REPLACE: offset: "+offset+", length: "+length+", str: \n'"+str+"'");
    	
    	// Start
    	// Get current document
    	PDDocument pddoc = getCurrentDocument();
    	// Create operation
    	PDOperation op = PDOperation.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    	op.setOpDocument(pddoc);
    	op.setOpUser(user);
    	op.setOpType("replace");
    	op.setOpOffset((long)offset);
    	op.setOpLength((long)length);
    	op.setOpString(str);
    	// Attach to hirstory
		history.addOperation(op);
		// Commit
		wc.commit();
  	
    }
    
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
    
    private PDDocument getCurrentDocument(){
    	
    	PDDocument pddoc = user.getCurrentDocument();
    	
    	if (pddoc == null){
    		// Need to create a document and set as current resource
    		pddoc = PDDocument.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    		user.setCurrentDocument(pddoc);
    	} else {
    		System.out.println("Document exists");
    	}   
    	
    	return pddoc;
    }
    
    
    //needs listeners to PDStore instance
    //listeners will call super methods
 

}
