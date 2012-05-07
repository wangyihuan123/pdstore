
package cms;

import javax.swing.text.*;

import cms.dal.PDDocument;
import cms.dal.PDResource;
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
    	/*
    	PDResource res = user.getCurrentResource();
    	PDDocument pddoc;
    	if (res == null){
    		res = PDResource.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    		user.setCurrentResource(res);
    		pddoc = PDDocument.load(wc, GUIDGen.generateGUIDs(1).remove(0));
    		res.setResourceType(pddoc.getId());
    	} else if (res.getResourceType().getTypeId().equals(PDDocument.typeId)) {
    		pddoc = PDDocument.load(wc, res.getResourceType().getId());
    	}
    	// add char to document
    	wc.commit();
    	*/
    	
    }
    
    //needs listeners to PDStore instance
    //listeners will call super methods
 

}
