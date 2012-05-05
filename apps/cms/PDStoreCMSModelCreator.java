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
	protected static final GUID RESOURCE_TYPEID = GUIDGen.generateGUIDs(1).remove(0);	
	protected static final GUID DOCUMENT_TYPEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID CHARACTER_TYPEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID HISTORY_TYPEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID OPERATION_TYPEID = GUIDGen.generateGUIDs(1).remove(0);
	
	// User roles
	protected static final GUID USERNAME_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID USER_RESOURCE_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	
	// Resource roles
	protected static final GUID RESOURCE_NAME_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID RESOURCE_LOCATION_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	
	// Document roles
	protected static final GUID FISRT_CHAR_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID LAST_CHAR_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	
	// Character roles
	protected static final GUID CHAR_VALUE_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID PREVIOUS_CHAR_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID NEXT_CHAR_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	
	// History roles
	protected static final GUID HISTORY_OPERATION_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	
	// Operation roles
	protected static final GUID OPERATION_TYPE_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID OPERATION_USER_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID PREVIOUS_OPERATION_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID NEXT_OPERATION_ROLEID = GUIDGen.generateGUIDs(1).remove(0);	
		
	public PDStoreCMSModelCreator(String DBFilename){
		
		store = new PDStore(DBFilename);
		GUID transaction = store.begin();
		
		// Create CMS model
		store.createModel(transaction, CMS_MODELID, CMS_MODELNAME);
		
		// Create CMS types
		store.createType(transaction, CMS_MODELID, USER_TYPEID, "User");
		store.createType(transaction, CMS_MODELID, RESOURCE_TYPEID, "Resource");
		store.createType(transaction, CMS_MODELID, DOCUMENT_TYPEID, "Document");
		store.createType(transaction, CMS_MODELID, CHARACTER_TYPEID, "Character");
		store.createType(transaction, CMS_MODELID, HISTORY_TYPEID, "History");
		store.createType(transaction, CMS_MODELID, OPERATION_TYPEID, "Operation");
		
		// Create CMS roles
		store.createRelation(transaction, USER_TYPEID, null, "Username", USERNAME_ROLEID, PDStore.STRING_TYPEID);	
		store.createRelation(transaction, USER_TYPEID, null, "CurrentResource", USER_RESOURCE_ROLEID, RESOURCE_TYPEID);
		
		store.createRelation(transaction, RESOURCE_TYPEID, null, "ResourceType", PDStore.HAS_TYPE_ROLEID, PDStore.TYPE_TYPEID);
		store.createRelation(transaction, RESOURCE_TYPEID, null, "ResourceFileName", RESOURCE_NAME_ROLEID, PDStore.STRING_TYPEID);
		store.createRelation(transaction, RESOURCE_TYPEID, null, "ResourceFileLocation", RESOURCE_LOCATION_ROLEID ,PDStore.STRING_TYPEID);
		
		store.createRelation(transaction, DOCUMENT_TYPEID, null, "FirstChar", FISRT_CHAR_ROLEID ,CHARACTER_TYPEID);
		store.createRelation(transaction, DOCUMENT_TYPEID, null, "LastChar", LAST_CHAR_ROLEID ,CHARACTER_TYPEID);
		
		store.createRelation(transaction, CHARACTER_TYPEID, null, "CharValue", CHAR_VALUE_ROLEID , PDStore.CHAR_TYPEID);
		store.createRelation(transaction, CHARACTER_TYPEID, null, "PrevChar", PREVIOUS_CHAR_ROLEID ,CHARACTER_TYPEID);
		store.createRelation(transaction, CHARACTER_TYPEID, null, "NextChar", NEXT_CHAR_ROLEID ,CHARACTER_TYPEID);
		
		store.createRelation(transaction, HISTORY_TYPEID, null, "FirstOp", HISTORY_OPERATION_ROLEID, OPERATION_TYPEID);
		
		store.createRelation(transaction, OPERATION_TYPEID, null, "OpType", OPERATION_TYPE_ROLEID, PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, OPERATION_TYPEID, null, "OpUser", OPERATION_USER_ROLEID, USER_TYPEID);
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
