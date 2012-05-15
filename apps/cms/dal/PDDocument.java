package cms.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Document" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("cms.dal.PDDocument");
 * @author PDGen
 */
public class PDDocument implements PDInstance {

	public static final GUID typeId = new GUID("bc0f5b519ccc11e1ae94d8a25e8c53de"); 

	public static final GUID roleDocumentFileLocationId = new GUID("bc0f5b5d9ccc11e1ae94d8a25e8c53de");
	public static final GUID roleDocumentTypeId = new GUID("bc0f5b5b9ccc11e1ae94d8a25e8c53de");
	public static final GUID roleDocumentFileNameId = new GUID("bc0f5b5c9ccc11e1ae94d8a25e8c53de");

	static {
		register();
	}
	
	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		DALClassRegister.addDataClass(typeId, PDDocument.class);
	}
	
	private PDWorkingCopy pdWorkingCopy;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDDocument:" + name;
		else
			return "PDDocument:" + id;
	}
	
	/**
	 * Creates an PDDocument object representing the given instance in the given cache.
	 * @param workingCopy the working copy the instance should be in
	 */
	public PDDocument(PDWorkingCopy workingCopy) {
		this(workingCopy, new GUID());
	}
	
	/**
	 * Creates an PDDocument object representing the given instance in the given copy.
	 * @param workingCopy the working copy the instance should be in
	 * @param id GUID of the instance
	 */
	public PDDocument(PDWorkingCopy workingCopy, GUID id) {
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
	public static PDDocument load(PDWorkingCopy pdWorkingCopy, GUID id) {
		PDInstance instance = pdWorkingCopy.load(typeId, id);
		return (PDDocument)instance;
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
	 * Returns the instance connected to this instance through the role "DocumentFileLocation".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public String getDocumentFileLocation() throws PDStoreException {
	 	return (String)pdWorkingCopy.getInstance(this, roleDocumentFileLocationId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "DocumentFileLocation".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<String> getDocumentFileLocations() throws PDStoreException {
	 	Set<String> result = new HashSet<String>();
	 	GUID StringTypeId = new GUID("4a8a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleDocumentFileLocationId, String.class, StringTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "DocumentFileLocation".
	 * If the given instance is null, nothing happens.
	 * @param documentFileLocation the instance to connect
	 * @throws PDStoreException
	 */
	public void addDocumentFileLocation(String documentFileLocation) throws PDStoreException {

			if (documentFileLocation != null) {
				
				pdWorkingCopy.addLink(this.id, roleDocumentFileLocationId, documentFileLocation);
			}

	}

	/**
	 * Connects this instance to the given instances using role "DocumentFileLocation".
	 * If the given collection of instances is null, nothing happens.
	 * @param documentFileLocation the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addDocumentFileLocations(Collection<String> documentFileLocations) throws PDStoreException {
		if (documentFileLocations == null)
			return;

		for (String instance : documentFileLocations)
			addDocumentFileLocation(instance);
	}


	/**
	 * Removes the link from this instance through role "DocumentFileLocation".
	 * @throws PDStoreException
	 */
	public void removeDocumentFileLocation() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleDocumentFileLocationId, 
			pdWorkingCopy.getInstance(this, roleDocumentFileLocationId));
	}

	/**
	 * Removes the link from this instance through role "DocumentFileLocation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeDocumentFileLocation(Object documentFileLocation) throws PDStoreException {
		if (documentFileLocation == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleDocumentFileLocationId, documentFileLocation);
	}


   /**
	 * Connects this instance to the given instance using role "DocumentFileLocation".
	 * If there is already an instance connected to this instance through role "DocumentFileLocation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param documentFileLocation the instance to connect
	 * @throws PDStoreException
	 */
	public void setDocumentFileLocation(String documentFileLocation) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleDocumentFileLocationId, documentFileLocation);	
	}


	/**
	 * Returns the instance connected to this instance through the role "DocumentType".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public String getDocumentType() throws PDStoreException {
	 	return (String)pdWorkingCopy.getInstance(this, roleDocumentTypeId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "DocumentType".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<String> getDocumentTypes() throws PDStoreException {
	 	Set<String> result = new HashSet<String>();
	 	GUID StringTypeId = new GUID("4a8a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleDocumentTypeId, String.class, StringTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "DocumentType".
	 * If the given instance is null, nothing happens.
	 * @param documentType the instance to connect
	 * @throws PDStoreException
	 */
	public void addDocumentType(String documentType) throws PDStoreException {

			if (documentType != null) {
				
				pdWorkingCopy.addLink(this.id, roleDocumentTypeId, documentType);
			}

	}

	/**
	 * Connects this instance to the given instances using role "DocumentType".
	 * If the given collection of instances is null, nothing happens.
	 * @param documentType the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addDocumentTypes(Collection<String> documentTypes) throws PDStoreException {
		if (documentTypes == null)
			return;

		for (String instance : documentTypes)
			addDocumentType(instance);
	}


	/**
	 * Removes the link from this instance through role "DocumentType".
	 * @throws PDStoreException
	 */
	public void removeDocumentType() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleDocumentTypeId, 
			pdWorkingCopy.getInstance(this, roleDocumentTypeId));
	}

	/**
	 * Removes the link from this instance through role "DocumentType" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeDocumentType(Object documentType) throws PDStoreException {
		if (documentType == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleDocumentTypeId, documentType);
	}


   /**
	 * Connects this instance to the given instance using role "DocumentType".
	 * If there is already an instance connected to this instance through role "DocumentType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param documentType the instance to connect
	 * @throws PDStoreException
	 */
	public void setDocumentType(String documentType) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleDocumentTypeId, documentType);	
	}


	/**
	 * Returns the instance connected to this instance through the role "DocumentFileName".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public String getDocumentFileName() throws PDStoreException {
	 	return (String)pdWorkingCopy.getInstance(this, roleDocumentFileNameId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "DocumentFileName".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<String> getDocumentFileNames() throws PDStoreException {
	 	Set<String> result = new HashSet<String>();
	 	GUID StringTypeId = new GUID("4a8a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleDocumentFileNameId, String.class, StringTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "DocumentFileName".
	 * If the given instance is null, nothing happens.
	 * @param documentFileName the instance to connect
	 * @throws PDStoreException
	 */
	public void addDocumentFileName(String documentFileName) throws PDStoreException {

			if (documentFileName != null) {
				
				pdWorkingCopy.addLink(this.id, roleDocumentFileNameId, documentFileName);
			}

	}

	/**
	 * Connects this instance to the given instances using role "DocumentFileName".
	 * If the given collection of instances is null, nothing happens.
	 * @param documentFileName the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addDocumentFileNames(Collection<String> documentFileNames) throws PDStoreException {
		if (documentFileNames == null)
			return;

		for (String instance : documentFileNames)
			addDocumentFileName(instance);
	}


	/**
	 * Removes the link from this instance through role "DocumentFileName".
	 * @throws PDStoreException
	 */
	public void removeDocumentFileName() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleDocumentFileNameId, 
			pdWorkingCopy.getInstance(this, roleDocumentFileNameId));
	}

	/**
	 * Removes the link from this instance through role "DocumentFileName" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeDocumentFileName(Object documentFileName) throws PDStoreException {
		if (documentFileName == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleDocumentFileNameId, documentFileName);
	}


   /**
	 * Connects this instance to the given instance using role "DocumentFileName".
	 * If there is already an instance connected to this instance through role "DocumentFileName", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param documentFileName the instance to connect
	 * @throws PDStoreException
	 */
	public void setDocumentFileName(String documentFileName) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleDocumentFileNameId, documentFileName);	
	}
}
