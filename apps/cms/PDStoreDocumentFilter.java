
package cms;

import javax.swing.text.*;

import cms.dal.PDDocument;
import cms.dal.PDUser;

import pdstore.GUID;
import pdstore.GUIDGen;
import pdstore.dal.PDType;
import pdstore.dal.PDWorkingCopy;

import java.awt.Toolkit;

public class PDStoreDocumentFilter extends DocumentFilter {

	PDUser user;
	PDWorkingCopy wc;
	
    public PDStoreDocumentFilter(PDWorkingCopy wc, PDUser user) {
		this.user = user;
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
    	
    	GUID transaction = wc.getStore().begin();

    	// Get current document
    	PDDocument pddoc = getCurrentDocument();
    	// Add char to correct position in document
    	

    	wc.getStore().commit(transaction);
    	
    }
    
    private PDDocument getCurrentDocument(){
    	
    	PDDocument pddoc = user.getCurrentDocument();
    	
    	if (pddoc == null){
    		// Need to create a document and set as current resource
    		pddoc = PDDocument.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    		user.setCurrentDocument(pddoc);
    	} else {
    		System.out.println("Document exists");
    		//pddoc = PDDocument.load(wc, res.);
    	}   
    	
    	return pddoc;
    }
    
    
    //needs listeners to PDStore instance
    //listeners will call super methods
 

}
