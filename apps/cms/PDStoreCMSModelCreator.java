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
	protected static final GUID CMS_OPERATION_TYPEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID DOCUMENT_OPERATION_TYPEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID FILE_OPERATION_TYPEID = GUIDGen.generateGUIDs(1).remove(0);	
	protected static final GUID HISTORY_TYPEID = GUIDGen.generateGUIDs(1).remove(0);
	
	// User roles
	protected static final GUID USER_CURRENT_DOCUMENT_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID USER_CARET_R_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID USER_CARET_G_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID USER_CARET_B_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID USER_CARET_POSITION_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	
	// Document roles
	protected static final GUID DOCUMENT_TYPE_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID DOCUMENT_FILENAME_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID DOCUMENT_LOCATION_ROLEID = GUIDGen.generateGUIDs(1).remove(0);	
	
	// Document Operation roles
	protected static final GUID DOCUMENT_OPERATION_TYPE_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID DOCUMENT_OPERATION_USER_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID DOCUMENT_OPERATION_OFFSET_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID DOCUMENT_OPERATION_LENGTH_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID DOCUMENT_OPERATION_STRING_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID DOCUMENT_OPERATION_DOCUMENT_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	
	// File Operation roles
	protected static final GUID FILE_OPERATION_TYPE_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID FILE_OPERATION_USER_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID FILE_OPERATION_PARAM_A_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	protected static final GUID FILE_OPERATION_PARAM_B_ROLEID = GUIDGen.generateGUIDs(1).remove(0);	
	
	// CMS Operation roles
	protected static final GUID CMS_OPERATION_ROLEID = GUIDGen.generateGUIDs(1).remove(0);
	
	public PDStoreCMSModelCreator(String DBFilename){
		
		store = new PDStore(DBFilename);
		GUID transaction = store.begin();
		
		// Create CMS model
		store.createModel(transaction, CMS_MODELID, CMS_MODELNAME);
		
		// Create CMS types
		store.createType(transaction, CMS_MODELID, USER_TYPEID, "User");
		store.createType(transaction, CMS_MODELID, DOCUMENT_TYPEID, "Document");
		store.createType(transaction, CMS_MODELID, HISTORY_TYPEID, "History");
		store.createType(transaction, CMS_MODELID, CMS_OPERATION_TYPEID, "CMSOperation");
		store.createType(transaction, CMS_MODELID, DOCUMENT_OPERATION_TYPEID, "DocumentOperation");
		store.createType(transaction, CMS_MODELID, FILE_OPERATION_TYPEID, "FileOperation");
		
		// Create CMS roles
		store.createRelation(transaction, USER_TYPEID, null, "CurrentDocument", USER_CURRENT_DOCUMENT_ROLEID, DOCUMENT_TYPEID);
		store.createRelation(transaction, USER_TYPEID, null, "CaretColorR", USER_CARET_R_ROLEID, PDStore.INTEGER_TYPEID); //Chars aren't working for some reason
		store.createRelation(transaction, USER_TYPEID, null, "CaretColorG", USER_CARET_G_ROLEID, PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, USER_TYPEID, null, "CaretColorB", USER_CARET_B_ROLEID, PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, USER_TYPEID, null, "CaretPosition", USER_CARET_POSITION_ROLEID, PDStore.INTEGER_TYPEID);
		
		store.createRelation(transaction, DOCUMENT_TYPEID, null, "DocumentType", DOCUMENT_TYPE_ROLEID, PDStore.STRING_TYPEID);
		store.createRelation(transaction, DOCUMENT_TYPEID, null, "DocumentFileName", DOCUMENT_FILENAME_ROLEID, PDStore.STRING_TYPEID);
		store.createRelation(transaction, DOCUMENT_TYPEID, null, "DocumentFileLocation", DOCUMENT_LOCATION_ROLEID ,PDStore.STRING_TYPEID);		
		
		store.createRelation(transaction, CMS_OPERATION_TYPEID, null, "OpType", PDStore.HAS_TYPE_ROLEID, PDStore.TYPE_TYPEID);
		store.createRelation(transaction, CMS_OPERATION_TYPEID, null, "NextOp", CMS_OPERATION_ROLEID, CMS_OPERATION_TYPEID);
		
		store.createRelation(transaction, DOCUMENT_OPERATION_TYPEID, null, "OpType", DOCUMENT_OPERATION_TYPE_ROLEID, PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, DOCUMENT_OPERATION_TYPEID, null, "OpOffset", DOCUMENT_OPERATION_OFFSET_ROLEID, PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, DOCUMENT_OPERATION_TYPEID, null, "OpLength", DOCUMENT_OPERATION_LENGTH_ROLEID, PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, DOCUMENT_OPERATION_TYPEID, null, "OpString", DOCUMENT_OPERATION_STRING_ROLEID, PDStore.STRING_TYPEID);
		store.createRelation(transaction, DOCUMENT_OPERATION_TYPEID, null, "OpUser", DOCUMENT_OPERATION_USER_ROLEID, USER_TYPEID);
		store.createRelation(transaction, DOCUMENT_OPERATION_TYPEID, null, "OpDocument", DOCUMENT_OPERATION_DOCUMENT_ROLEID, DOCUMENT_TYPEID);
		
		store.createRelation(transaction, FILE_OPERATION_TYPEID, null, "OpType", FILE_OPERATION_TYPE_ROLEID, PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, FILE_OPERATION_TYPEID, null, "OpUser", FILE_OPERATION_USER_ROLEID, USER_TYPEID);
		store.createRelation(transaction, FILE_OPERATION_TYPEID, null, "OpParamA", FILE_OPERATION_PARAM_A_ROLEID, PDStore.STRING_TYPEID);
		store.createRelation(transaction, FILE_OPERATION_TYPEID, null, "OpParamB", FILE_OPERATION_PARAM_B_ROLEID, PDStore.STRING_TYPEID);	
		
		store.createRelation(transaction, HISTORY_TYPEID, null, "CMSOperation",CMS_OPERATION_ROLEID, CMS_OPERATION_TYPEID);	
		
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
