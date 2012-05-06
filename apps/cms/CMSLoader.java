package cms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pdstore.GUID;
import pdstore.GUIDGen;
import pdstore.PDStore;
import pdstore.dal.PDSimpleWorkingCopy;
import pdstore.dal.PDWorkingCopy;
import cms.dal.PDUser;

public class CMSLoader {

	private static final String STORE_NAME = "ContentManagementSystem";
	private int instances;
	private static PDStore store;
	ArrayList<PDWorkingCopy> copies;
	List<GUID> userIDs;
	List<GUID> historyIDs;

	public CMSLoader(boolean network, int instances){

		cleanDBFile();
		
		this.instances = instances;

		userIDs = GUIDGen.generateGUIDs(instances);
		historyIDs = GUIDGen.generateGUIDs(instances);
		
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
			//sharedCopy.setAutocommit(true);
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

		store.commit(transaction);
	}	


	public static void main(String[] args) {

		final boolean NETWORK_ACCESS = false;
		final int NUM_USERS = 2;

		// This is required
		try {
			Class.forName("cms.dal.PDCharacter");
			Class.forName("cms.dal.PDDocument");
			Class.forName("cms.dal.PDHistory");
			Class.forName("cms.dal.PDOperation");			
			Class.forName("cms.dal.PDUser");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
		
		CMSLoader cmsl = new CMSLoader(NETWORK_ACCESS, NUM_USERS);
		cmsl.init();

	}
}