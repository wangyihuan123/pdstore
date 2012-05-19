package cms;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import pdstore.GUID;
import pdstore.GUIDGen;
import pdstore.PDStore;
import pdstore.dal.PDSimpleWorkingCopy;
import pdstore.dal.PDWorkingCopy;
import cms.dal.PDDocument;
import cms.dal.PDUser;

public class CMSLoader {

	private static final String STORE_NAME = "ContentManagementSystem";
	private static String DOCUMENT_ROOT;
	private int instances;
	private static PDStore store;
	protected ArrayList<ContentManagementSystem> cmsList;
	ArrayList<PDWorkingCopy> copies;
	List<GUID> userIDs;
	List<GUID> historyIDs;

	public CMSLoader(boolean network, int instances, String docRoot){

		cleanDBFile();
		
		this.instances = instances;
		this.DOCUMENT_ROOT = docRoot;

		userIDs = GUIDGen.generateGUIDs(instances);
		historyIDs = GUIDGen.generateGUIDs(instances);
		cmsList = new ArrayList<ContentManagementSystem>(); 
		
		copies = new ArrayList<PDWorkingCopy>(instances);
		if (network) {
			store = PDStore.connectToServer(null);
			addData();
			for (int i = 0; i < instances; i++){
				PDWorkingCopy copy = new PDSimpleWorkingCopy(store);
				//copy.setAutocommit(true);				
				copies.add(copy);
			}
		} else {
			store = new PDStore(STORE_NAME);
			addData();
			PDWorkingCopy sharedCopy = new PDSimpleWorkingCopy(store);
			sharedCopy.setAutocommit(true);
			for (int i = 0; i < instances; i++){
				copies.add(sharedCopy);
			}			
		}	
	}
	
	private void cleanDBFile(){
		
		File dbfile = new File(PDStore.DEFAULT_FILEPATH+STORE_NAME+PDStore.DEFAULT_EXTENSION);
		if (dbfile.exists()){
			if (!dbfile.delete()){
				System.err.println("Unable to delete PDS file '"+dbfile.getAbsolutePath()+"'");
				System.exit(127);
			}
		}		
	}

	public void init(){

		for (int i = 0; i < instances; i++){
			ContentManagementSystem cms = new ContentManagementSystem(userIDs.get(i), historyIDs.get(i), copies.get(i));
			cms.setVisible(true);
			cmsList.add(cms);
		}
	}

	private void addData(){

		GUID transaction = store.begin(); 
		
		// Create Users
		for (int i = 0; i < instances; i++){
			// General user properties
			GUID uID = userIDs.get(i);
			store.setName(transaction, uID, "User_"+i);
			store.setType(transaction, uID, PDUser.typeId);
			// Assign caret colors
			store.addLink(transaction, uID, PDUser.roleCaretColorRId, new Long((long)(Math.random()*255.0)));
			store.addLink(transaction, uID, PDUser.roleCaretColorGId, new Long((long)(Math.random()*255.0)));
			store.addLink(transaction, uID, PDUser.roleCaretColorBId, new Long((long)(Math.random()*255.0)));
			// Assign default caret position
			store.addLink(transaction, uID, PDUser.roleCaretPositionId, new Long(0));
			
		}

		//Register files and documents
		File root = new File(DOCUMENT_ROOT);
		String [] exts = new String [] {"text", "html", "js", "css"};
		Collection<File> fcol = FileUtils.listFiles(root, exts, true);
		File [] files = new File[fcol.size()];
		fcol.toArray(files);
		List<GUID> docIDs = GUIDGen.generateGUIDs(files.length);
		for (int i = 0; i < files.length; i++){
			GUID dID = docIDs.get(i);
			File f = files[i];
			store.setType(transaction, dID, PDDocument.typeId);
			String fname = f.getAbsolutePath();
			String ext = fname.substring(fname.lastIndexOf('.')+1);
			store.addLink(transaction, dID, PDDocument.roleDocumentTypeId, ext);
			store.addLink(transaction, dID, PDDocument.roleDocumentFileNameId, f.getName());
			store.addLink(transaction, dID, PDDocument.roleDocumentFileLocationId, fname.replace(DOCUMENT_ROOT, ""));
		}
		
		
		store.commit(transaction);
	}	


	public static void main(String[] args) {

		final boolean NETWORK_ACCESS = false;
		final int NUM_USERS = 2;
		final String DOCUMENT_ROOT = System.getenv("HOME")+"/www";

		// This is required
		try {
			Class.forName("cms.dal.PDDocument");
			Class.forName("cms.dal.PDHistory");
			Class.forName("cms.dal.PDCMSOperation");
			Class.forName("cms.dal.PDDocumentOperation");	
			Class.forName("cms.dal.PDFileOperation");
			Class.forName("cms.dal.PDUser");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
		
		//try {
			CMSLoader cmsl = new CMSLoader(NETWORK_ACCESS, NUM_USERS, DOCUMENT_ROOT);
			cmsl.init();
		//} catch (Exception e) {
		//	e.printStackTrace();
		//}

	}
}