package cms.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "History" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("cms.dal.PDHistory");
 * @author PDGen
 */
public class PDHistory implements PDInstance {

	public static final GUID typeId = new GUID("a66fc74498ed11e1921bc42c0302465e"); 

	public static final GUID roleDocumentOperationId = new GUID("a66fc75898ed11e1921bc42c0302465e");
	public static final GUID roleFileOperationId = new GUID("a66fc75998ed11e1921bc42c0302465e");

	static {
		register();
	}
	
	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		DALClassRegister.addDataClass(typeId, PDHistory.class);
	}
	
	private PDWorkingCopy pdWorkingCopy;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDHistory:" + name;
		else
			return "PDHistory:" + id;
	}
	
	/**
	 * Creates an PDHistory object representing the given instance in the given cache.
	 * @param workingCopy the working copy the instance should be in
	 */
	public PDHistory(PDWorkingCopy workingCopy) {
		this(workingCopy, new GUID());
	}
	
	/**
	 * Creates an PDHistory object representing the given instance in the given copy.
	 * @param workingCopy the working copy the instance should be in
	 * @param id GUID of the instance
	 */
	public PDHistory(PDWorkingCopy workingCopy, GUID id) {
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
	public static PDHistory load(PDWorkingCopy pdWorkingCopy, GUID id) {
		PDInstance instance = pdWorkingCopy.load(typeId, id);
		return (PDHistory)instance;
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
	 * Returns the instance connected to this instance through the role "DocumentOperation".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDDocumentOperation getDocumentOperation() throws PDStoreException {
	 	return (PDDocumentOperation)pdWorkingCopy.getInstance(this, roleDocumentOperationId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "DocumentOperation".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDDocumentOperation> getDocumentOperations() throws PDStoreException {
	 	Set<PDDocumentOperation> result = new HashSet<PDDocumentOperation>();
	 	GUID PDDocumentOperationTypeId = new GUID("a66fc74298ed11e1921bc42c0302465e");
		pdWorkingCopy.getInstances(this, roleDocumentOperationId, PDDocumentOperation.class, PDDocumentOperationTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "DocumentOperation".
	 * If the given instance is null, nothing happens.
	 * @param documentOperation the instance to connect
	 * @throws PDStoreException
	 */
	public void addDocumentOperation(GUID documentOperation) throws PDStoreException {

			if (documentOperation != null) {
				
				pdWorkingCopy.addLink(this.id, roleDocumentOperationId, documentOperation);
			}

	}


	/**
	 * Connects this instance to the given instance using role "DocumentOperation".
	 * If the given instance is null, nothing happens.
	 * @param documentOperation the instance to connect
	 * @throws PDStoreException
	 */
	public void addDocumentOperation(PDDocumentOperation documentOperation) throws PDStoreException {
		if (documentOperation != null) {
			addDocumentOperation(documentOperation.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "DocumentOperation".
	 * If the given collection of instances is null, nothing happens.
	 * @param documentOperation the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addDocumentOperations(Collection<PDDocumentOperation> documentOperations) throws PDStoreException {
		if (documentOperations == null)
			return;
		
		for (PDDocumentOperation instance : documentOperations)
			addDocumentOperation(instance);	
	}

	/**
	 * Removes the link from this instance through role "DocumentOperation".
	 * @throws PDStoreException
	 */
	public void removeDocumentOperation() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleDocumentOperationId, 
			pdWorkingCopy.getInstance(this, roleDocumentOperationId));
	}

	/**
	 * Removes the link from this instance through role "DocumentOperation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeDocumentOperation(Object documentOperation) throws PDStoreException {
		if (documentOperation == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleDocumentOperationId, documentOperation);
	}

	/**
	 * Removes the links from this instance through role "DocumentOperation" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeDocumentOperations(Collection<PDDocumentOperation> documentOperations) throws PDStoreException {
		if (documentOperations == null)
			return;
		
		for (PDDocumentOperation instance : documentOperations)
			pdWorkingCopy.removeLink(this.id, roleDocumentOperationId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "DocumentOperation".
	 * If there is already an instance connected to this instance through role "DocumentOperation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param documentOperation the instance to connect
	 * @throws PDStoreException
	 */
	public void setDocumentOperation(GUID documentOperation) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleDocumentOperationId, documentOperation);	
	}
	/**
	 * Connects this instance to the given instance using role "DocumentOperation".
	 * If there is already an instance connected to this instance through role "DocumentOperation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param documentOperation the instance to connect
	 * @throws PDStoreException
	 */
	public void setDocumentOperation(PDDocumentOperation documentOperation) throws PDStoreException {
		setDocumentOperation(documentOperation.getId());
	}



	/**
	 * Returns the instance connected to this instance through the role "FileOperation".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDFileOperation getFileOperation() throws PDStoreException {
	 	return (PDFileOperation)pdWorkingCopy.getInstance(this, roleFileOperationId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "FileOperation".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDFileOperation> getFileOperations() throws PDStoreException {
	 	Set<PDFileOperation> result = new HashSet<PDFileOperation>();
	 	GUID PDFileOperationTypeId = new GUID("a66fc74398ed11e1921bc42c0302465e");
		pdWorkingCopy.getInstances(this, roleFileOperationId, PDFileOperation.class, PDFileOperationTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "FileOperation".
	 * If the given instance is null, nothing happens.
	 * @param fileOperation the instance to connect
	 * @throws PDStoreException
	 */
	public void addFileOperation(GUID fileOperation) throws PDStoreException {

			if (fileOperation != null) {
				
				pdWorkingCopy.addLink(this.id, roleFileOperationId, fileOperation);
			}

	}


	/**
	 * Connects this instance to the given instance using role "FileOperation".
	 * If the given instance is null, nothing happens.
	 * @param fileOperation the instance to connect
	 * @throws PDStoreException
	 */
	public void addFileOperation(PDFileOperation fileOperation) throws PDStoreException {
		if (fileOperation != null) {
			addFileOperation(fileOperation.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "FileOperation".
	 * If the given collection of instances is null, nothing happens.
	 * @param fileOperation the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addFileOperations(Collection<PDFileOperation> fileOperations) throws PDStoreException {
		if (fileOperations == null)
			return;
		
		for (PDFileOperation instance : fileOperations)
			addFileOperation(instance);	
	}

	/**
	 * Removes the link from this instance through role "FileOperation".
	 * @throws PDStoreException
	 */
	public void removeFileOperation() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleFileOperationId, 
			pdWorkingCopy.getInstance(this, roleFileOperationId));
	}

	/**
	 * Removes the link from this instance through role "FileOperation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeFileOperation(Object fileOperation) throws PDStoreException {
		if (fileOperation == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleFileOperationId, fileOperation);
	}

	/**
	 * Removes the links from this instance through role "FileOperation" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeFileOperations(Collection<PDFileOperation> fileOperations) throws PDStoreException {
		if (fileOperations == null)
			return;
		
		for (PDFileOperation instance : fileOperations)
			pdWorkingCopy.removeLink(this.id, roleFileOperationId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "FileOperation".
	 * If there is already an instance connected to this instance through role "FileOperation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param fileOperation the instance to connect
	 * @throws PDStoreException
	 */
	public void setFileOperation(GUID fileOperation) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleFileOperationId, fileOperation);	
	}
	/**
	 * Connects this instance to the given instance using role "FileOperation".
	 * If there is already an instance connected to this instance through role "FileOperation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param fileOperation the instance to connect
	 * @throws PDStoreException
	 */
	public void setFileOperation(PDFileOperation fileOperation) throws PDStoreException {
		setFileOperation(fileOperation.getId());
	}

}
