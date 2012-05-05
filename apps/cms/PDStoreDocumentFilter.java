
package cms;

import javax.swing.text.*;

import cms.dal.PDDocument;
import cms.dal.PDResource;
import cms.dal.PDUser;

import pdstore.GUID;
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
    	PDResource res = user.getCurrentResource();
    	if (res == null){
    		
    	}
    	/*
    	if (res.getResourceType().typeId.equals(PDDocument.typeId)) {
    		System.out.println("Resource is Document");
    	} else {
    		System.out.println("Please create a document");
    	}
    	*/
    	
    }
    
    //needs listeners to PDStore instance
    //listeners will call super methods
 

}
