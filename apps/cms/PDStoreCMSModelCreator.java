package cms;

import java.io.File;

import pdstore.GUID;
import pdstore.GUIDGen;
import pdstore.PDStore;
import pdstore.dal.PDGen;
import pdstore.dal.PDSimpleWorkingCopy;

public class PDStoreCMSModelCreator {
	
	protected PDStore store;

	// CMS model
	protected static final String CMS_MODELNAME = "ContentManagemetSystem";
	protected static final GUID CMS_MODELID = GUIDGen.generateGUIDs(1).remove(0);
	
	// Complex types
	protected static final GUID USER_TYPEID = GUIDGen.generateGUIDs(1).remove(0);		
	protected static final GUID DOCUMENT_TYPEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID CHARACTER_TYPEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID HISTORY_TYPEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID OPERATION_TYPEID = GUIDGen.generateGUIDs(1).remove(0);
	
	// User roles
	protected static final GUID USERNAME_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID USER_RESOURCE_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID USER_CARET_R_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID USER_CARET_G_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID USER_CARET_B_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID USER_CARET_POSITION_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	
	
	// Document roles
	protected static final GUID DOCUMENT_TYPE_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID DOCUMENT_NAME_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID DOCUMENT_LOCATION_ROLEID = GUIDGen.generateGUIDs(1).remove(0);	
	protected static final GUID CHARACTER_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	
	// Character roles
	protected static final GUID CHAR_VALUE_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID PREVIOUS_CHAR_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID NEXT_CHAR_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	
	// History roles
	protected static final GUID HISTORY_OPERATION_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	
	// Operation roles
	protected static final GUID OPERATION_TYPE_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID OPERATION_USER_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID OPERATION_OFFSET_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID OPERATION_LENGTH_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID OPERATION_STRING_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID PREVIOUS_OPERATION_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID NEXT_OPERATION_ROLEID = GUIDGen.generateGUIDs(1).remove(0);	
	protected static final GUID OPERATION_DOCUMENT_ROLEID = GUIDGen.generateGUIDs(1).remove(0);	
	
		
	public PDStoreCMSModelCreator(String DBFilename){
		
		store = new PDStore(DBFilename);
		GUID transaction = store.begin();
		
		// Create CMS model
		store.createModel(transaction, CMS_MODELID, CMS_MODELNAME);
		
		// Create CMS types
		store.createType(transaction, CMS_MODELID, USER_TYPEID, "User");
		store.createType(transaction, CMS_MODELID, DOCUMENT_TYPEID, "Document");
		store.createType(transaction, CMS_MODELID, CHARACTER_TYPEID, "Character");
		store.createType(transaction, CMS_MODELID, HISTORY_TYPEID, "History");
		store.createType(transaction, CMS_MODELID, OPERATION_TYPEID, "Operation");
		
		// Create CMS roles
			
		store.createRelation(transaction, USER_TYPEID, null, "CurrentDocument", USER_RESOURCE_ROLEID, DOCUMENT_TYPEID);
		store.createRelation(transaction, USER_TYPEID, null, "CaretColorR", USER_CARET_R_ROLEID, PDStore.INTEGER_TYPEID); //Chars aren't working for some reason
		store.createRelation(transaction, USER_TYPEID, null, "CaretColorG", USER_CARET_G_ROLEID, PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, USER_TYPEID, null, "CaretColorB", USER_CARET_B_ROLEID, PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, USER_TYPEID, null, "CaretPosition", USER_CARET_POSITION_ROLEID, PDStore.INTEGER_TYPEID);
		
		store.createRelation(transaction, DOCUMENT_TYPEID, null, "DocumentType", DOCUMENT_TYPE_ROLEID, PDStore.STRING_TYPEID);
		store.createRelation(transaction, DOCUMENT_TYPEID, null, "DocumentFileName", DOCUMENT_NAME_ROLEID, PDStore.STRING_TYPEID);
		store.createRelation(transaction, DOCUMENT_TYPEID, null, "DocumentFileLocation", DOCUMENT_LOCATION_ROLEID ,PDStore.STRING_TYPEID);		
		store.createRelation(transaction, DOCUMENT_TYPEID, null, "Character", CHARACTER_ROLEID, CHARACTER_TYPEID);
		
		store.createRelation(transaction, CHARACTER_TYPEID, null, "CharValue", CHAR_VALUE_ROLEID , PDStore.CHAR_TYPEID);
		store.createRelation(transaction, CHARACTER_TYPEID, null, "PrevChar", PREVIOUS_CHAR_ROLEID ,CHARACTER_TYPEID);
		store.createRelation(transaction, CHARACTER_TYPEID, null, "NextChar", NEXT_CHAR_ROLEID ,CHARACTER_TYPEID);
		
		store.createRelation(transaction, HISTORY_TYPEID, null, "Operation", HISTORY_OPERATION_ROLEID, OPERATION_TYPEID);
		
		store.createRelation(transaction, OPERATION_TYPEID, null, "OpType", OPERATION_TYPE_ROLEID, PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, OPERATION_TYPEID, null, "OpOffset", OPERATION_OFFSET_ROLEID, PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, OPERATION_TYPEID, null, "OpLength", OPERATION_LENGTH_ROLEID, PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, OPERATION_TYPEID, null, "OpString", OPERATION_STRING_ROLEID, PDStore.STRING_TYPEID);
		store.createRelation(transaction, OPERATION_TYPEID, null, "OpUser", OPERATION_USER_ROLEID, USER_TYPEID);
		store.createRelation(transaction, OPERATION_TYPEID, null, "OpDocument", OPERATION_DOCUMENT_ROLEID, DOCUMENT_TYPEID);
		store.createRelation(transaction, OPERATION_TYPEID, null, "PrevOp", PREVIOUS_OPERATION_ROLEID, OPERATION_TYPEID);
		store.createRelation(transaction, OPERATION_TYPEID, null, "NextOp", NEXT_OPERATION_ROLEID, OPERATION_TYPEID);		
		
		// End transaction
		store.commit(transaction);
		
	}
	
	public static void main(String[] args){
		String dbfname = "cms";
		String dalDir = "cms.dal";
		
		if (args.length != 0) {
			dbfname = args[0];
		}
		
		File dbfile = new File(PDStore.DEFAULT_FILEPATH+dbfname+PDStore.DEFAULT_EXTENSION);
		if (dbfile.exists()){
			if (!dbfile.delete()){
				System.err.println("Unable to delete PDS file '"+dbfile.getAbsolutePath()+"'");
				System.exit(127);
			}
		}	
		
		PDStoreCMSModelCreator mc = new PDStoreCMSModelCreator(dbfname);
		PDSimpleWorkingCopy copy = new PDSimpleWorkingCopy(mc.store);
		PDGen.generateModel(CMS_MODELNAME, "apps", copy, dalDir);
	}
	
}
