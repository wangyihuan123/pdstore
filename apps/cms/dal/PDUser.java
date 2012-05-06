package cms.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "User" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("cms.dal.PDUser");
 * @author PDGen
 */
public class PDUser implements PDInstance {

	public static final GUID typeId = new GUID("ad319010970611e1a171d8a25e8c53de"); 

	public static final GUID roleCurrentDocumentId = new GUID("ad319016970611e1a171d8a25e8c53de");
	public static final GUID roleUsernameId = new GUID("ad319015970611e1a171d8a25e8c53de");

	static {
		register();
	}
	
	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		DALClassRegister.addDataClass(typeId, PDUser.class);
	}
	
	private PDWorkingCopy pdWorkingCopy;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDUser:" + name;
		else
			return "PDUser:" + id;
	}
	
	/**
	 * Creates an PDUser object representing the given instance in the given cache.
	 * @param workingCopy the working copy the instance should be in
	 */
	public PDUser(PDWorkingCopy workingCopy) {
		this(workingCopy, new GUID());
	}
	
	/**
	 * Creates an PDUser object representing the given instance in the given copy.
	 * @param workingCopy the working copy the instance should be in
	 * @param id GUID of the instance
	 */
	public PDUser(PDWorkingCopy workingCopy, GUID id) {
		this.pdWorkingCopy = workingCopy;
		this.id = id;
		
		// set the has-type link for this instance
		GUID transaction = pdWorkingCopy.getTransaction();
		pdWorkingCopy.getStore().setType(transaction, id, typeId);
	}

	/**
	 * Loads an instance object of this type into a cache.
	 * If the instance is already in the cache, the cached instance is returned.
	 * @param PDWorkingCopy pdWorkingCopy to load the instance into
	 * @param id GUID of the instance
	 * Do not directly call this method. Use the newInstance() method in PDCache which would call this method
	 */
	public static PDUser load(PDWorkingCopy pdWorkingCopy, GUID id) {
		PDInstance instance = pdWorkingCopy.load(typeId, id);
		return (PDUser)instance;
	}

	/**
	 * Gets the pdWorkingCopy this object is stored in.
	 */
	public PDWorkingCopy getPDWorkingCopy() {
		return pdWorkingCopy;
	}

	/**
	 * Gets the GUID of the instance represented by this object.
	 */
	public GUID getId() {
		return id;
	}

	/**
	 * Gets the GUID of the type of the instance represented by this object.
	 */
	public GUID getTypeId() {
		return typeId;
	}

	/**
	 * Gets a textual label for this instance, for use in UIs.
	 * @return a textual label for the instance
	 */
	public String getLabel() {
		return pdWorkingCopy.getLabel(id);
	}
	
	/**
	 * Gets the name of this instance.
	 * In PDStore every instance can be given a name.
	 * @return name the instance name
	 * @throws PDStoreException
	 */
	public String getName() {
		return pdWorkingCopy.getName(id);
	}
	
	/**
	 * Sets the name of this instance.
	 * In PDStore every instance can be given a name.
	 * If the instance already has a name, the name will be overwritten.
	 * If the given name is null, an existing name will be removed.
	 * @return name the new instance name
	 * @throws PDStoreException
	 */
	public void setName(String name) {
		pdWorkingCopy.setName(id, name);
	}

	/**
	 * Removes the name of this instance.
	 * In PDStore every instance can be given a name.
	 * If the instance does not have a name, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeName() {
		pdWorkingCopy.removeName(id);
	}

	/**
	 * Gets the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * @return icon the instance icon
	 * @throws PDStoreException
	 */
	public Blob getIcon() {
		return pdWorkingCopy.getIcon(id);
	}

	/**
	 * Sets the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * If the instance already has an icon, the icon will be overwritten.
	 * If the given icon is null, an existing icon will be removed.
	 * @return icon the new instance icon
	 * @throws PDStoreException
	 */
	public void setIcon(Blob icon) {
		pdWorkingCopy.setIcon(id, icon);
	}

	/**
	 * Removes the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * If the instance does not have an icon, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeIcon() {
		pdWorkingCopy.removeIcon(id);
	}
	

	/**
	 * Returns the instance connected to this instance through the role "CurrentDocument".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDDocument getCurrentDocument() throws PDStoreException {
	 	return (PDDocument)pdWorkingCopy.getInstance(this, roleCurrentDocumentId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "CurrentDocument".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDDocument> getCurrentDocuments() throws PDStoreException {
	 	Set<PDDocument> result = new HashSet<PDDocument>();
	 	GUID PDDocumentTypeId = new GUID("ad319011970611e1a171d8a25e8c53de");
		pdWorkingCopy.getInstances(this, roleCurrentDocumentId, PDDocument.class, PDDocumentTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "CurrentDocument".
	 * If the given instance is null, nothing happens.
	 * @param currentDocument the instance to connect
	 * @throws PDStoreException
	 */
	public void addCurrentDocument(GUID currentDocument) throws PDStoreException {

			if (currentDocument != null) {
				
				pdWorkingCopy.addLink(this.id, roleCurrentDocumentId, currentDocument);
			}

	}


	/**
	 * Connects this instance to the given instance using role "CurrentDocument".
	 * If the given instance is null, nothing happens.
	 * @param currentDocument the instance to connect
	 * @throws PDStoreException
	 */
	public void addCurrentDocument(PDDocument currentDocument) throws PDStoreException {
		if (currentDocument != null) {
			addCurrentDocument(currentDocument.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "CurrentDocument".
	 * If the given collection of instances is null, nothing happens.
	 * @param currentDocument the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addCurrentDocuments(Collection<PDDocument> currentDocuments) throws PDStoreException {
		if (currentDocuments == null)
			return;
		
		for (PDDocument instance : currentDocuments)
			addCurrentDocument(instance);	
	}

	/**
	 * Removes the link from this instance through role "CurrentDocument".
	 * @throws PDStoreException
	 */
	public void removeCurrentDocument() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleCurrentDocumentId, 
			pdWorkingCopy.getInstance(this, roleCurrentDocumentId));
	}

	/**
	 * Removes the link from this instance through role "CurrentDocument" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeCurrentDocument(Object currentDocument) throws PDStoreException {
		if (currentDocument == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleCurrentDocumentId, currentDocument);
	}

	/**
	 * Removes the links from this instance through role "CurrentDocument" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeCurrentDocuments(Collection<PDDocument> currentDocuments) throws PDStoreException {
		if (currentDocuments == null)
			return;
		
		for (PDDocument instance : currentDocuments)
			pdWorkingCopy.removeLink(this.id, roleCurrentDocumentId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "CurrentDocument".
	 * If there is already an instance connected to this instance through role "CurrentDocument", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param currentDocument the instance to connect
	 * @throws PDStoreException
	 */
	public void setCurrentDocument(GUID currentDocument) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleCurrentDocumentId, currentDocument);	
	}
	/**
	 * Connects this instance to the given instance using role "CurrentDocument".
	 * If there is already an instance connected to this instance through role "CurrentDocument", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param currentDocument the instance to connect
	 * @throws PDStoreException
	 */
	public void setCurrentDocument(PDDocument currentDocument) throws PDStoreException {
		setCurrentDocument(currentDocument.getId());
	}



	/**
	 * Returns the instance connected to this instance through the role "Username".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public String getUsername() throws PDStoreException {
	 	return (String)pdWorkingCopy.getInstance(this, roleUsernameId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "Username".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<String> getUsernames() throws PDStoreException {
	 	Set<String> result = new HashSet<String>();
	 	GUID StringTypeId = new GUID("4a8a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleUsernameId, String.class, StringTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "Username".
	 * If the given instance is null, nothing happens.
	 * @param username the instance to connect
	 * @throws PDStoreException
	 */
	public void addUsername(String username) throws PDStoreException {

			if (username != null) {
				
				pdWorkingCopy.addLink(this.id, roleUsernameId, username);
			}

	}

	/**
	 * Connects this instance to the given instances using role "Username".
	 * If the given collection of instances is null, nothing happens.
	 * @param username the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addUsernames(Collection<String> usernames) throws PDStoreException {
		if (usernames == null)
			return;

		for (String instance : usernames)
			addUsername(instance);
	}


	/**
	 * Removes the link from this instance through role "Username".
	 * @throws PDStoreException
	 */
	public void removeUsername() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleUsernameId, 
			pdWorkingCopy.getInstance(this, roleUsernameId));
	}

	/**
	 * Removes the link from this instance through role "Username" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeUsername(Object username) throws PDStoreException {
		if (username == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleUsernameId, username);
	}


   /**
	 * Connects this instance to the given instance using role "Username".
	 * If there is already an instance connected to this instance through role "Username", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param username the instance to connect
	 * @throws PDStoreException
	 */
	public void setUsername(String username) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleUsernameId, username);	
	}
}
