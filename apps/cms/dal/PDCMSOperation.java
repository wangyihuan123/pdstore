package cms.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "CMSOperation" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("cms.dal.PDCMSOperation");
 * @author PDGen
 */
public class PDCMSOperation implements PDInstance {

	public static final GUID typeId = new GUID("bc0f5b529ccc11e1ae94d8a25e8c53de"); 

	public static final GUID roleDocumentOpId = new GUID("bc0f5b6a9ccc11e1ae94d8a25e8c53de");
	public static final GUID roleFileOpId = new GUID("bc0f5b6b9ccc11e1ae94d8a25e8c53de");
	public static final GUID roleOpTypeId = new GUID("ee32adf0f68b11df860e1cc1dec00ed3");
	public static final GUID roleNextOpId = new GUID("bc0f5b699ccc11e1ae94d8a25e8c53de");

	static {
		register();
	}
	
	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		DALClassRegister.addDataClass(typeId, PDCMSOperation.class);
	}
	
	private PDWorkingCopy pdWorkingCopy;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDCMSOperation:" + name;
		else
			return "PDCMSOperation:" + id;
	}
	
	/**
	 * Creates an PDCMSOperation object representing the given instance in the given cache.
	 * @param workingCopy the working copy the instance should be in
	 */
	public PDCMSOperation(PDWorkingCopy workingCopy) {
		this(workingCopy, new GUID());
	}
	
	/**
	 * Creates an PDCMSOperation object representing the given instance in the given copy.
	 * @param workingCopy the working copy the instance should be in
	 * @param id GUID of the instance
	 */
	public PDCMSOperation(PDWorkingCopy workingCopy, GUID id) {
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
	public static PDCMSOperation load(PDWorkingCopy pdWorkingCopy, GUID id) {
		PDInstance instance = pdWorkingCopy.load(typeId, id);
		return (PDCMSOperation)instance;
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
	 * Returns the instance connected to this instance through the role "DocumentOp".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDDocumentOperation getDocumentOp() throws PDStoreException {
	 	return (PDDocumentOperation)pdWorkingCopy.getInstance(this, roleDocumentOpId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "DocumentOp".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDDocumentOperation> getDocumentOps() throws PDStoreException {
	 	Set<PDDocumentOperation> result = new HashSet<PDDocumentOperation>();
	 	GUID PDDocumentOperationTypeId = new GUID("bc0f5b539ccc11e1ae94d8a25e8c53de");
		pdWorkingCopy.getInstances(this, roleDocumentOpId, PDDocumentOperation.class, PDDocumentOperationTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "DocumentOp".
	 * If the given instance is null, nothing happens.
	 * @param documentOp the instance to connect
	 * @throws PDStoreException
	 */
	public void addDocumentOp(GUID documentOp) throws PDStoreException {

			if (documentOp != null) {
				
				pdWorkingCopy.addLink(this.id, roleDocumentOpId, documentOp);
			}

	}


	/**
	 * Connects this instance to the given instance using role "DocumentOp".
	 * If the given instance is null, nothing happens.
	 * @param documentOp the instance to connect
	 * @throws PDStoreException
	 */
	public void addDocumentOp(PDDocumentOperation documentOp) throws PDStoreException {
		if (documentOp != null) {
			addDocumentOp(documentOp.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "DocumentOp".
	 * If the given collection of instances is null, nothing happens.
	 * @param documentOp the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addDocumentOps(Collection<PDDocumentOperation> documentOps) throws PDStoreException {
		if (documentOps == null)
			return;
		
		for (PDDocumentOperation instance : documentOps)
			addDocumentOp(instance);	
	}

	/**
	 * Removes the link from this instance through role "DocumentOp".
	 * @throws PDStoreException
	 */
	public void removeDocumentOp() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleDocumentOpId, 
			pdWorkingCopy.getInstance(this, roleDocumentOpId));
	}

	/**
	 * Removes the link from this instance through role "DocumentOp" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeDocumentOp(Object documentOp) throws PDStoreException {
		if (documentOp == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleDocumentOpId, documentOp);
	}

	/**
	 * Removes the links from this instance through role "DocumentOp" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeDocumentOps(Collection<PDDocumentOperation> documentOps) throws PDStoreException {
		if (documentOps == null)
			return;
		
		for (PDDocumentOperation instance : documentOps)
			pdWorkingCopy.removeLink(this.id, roleDocumentOpId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "DocumentOp".
	 * If there is already an instance connected to this instance through role "DocumentOp", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param documentOp the instance to connect
	 * @throws PDStoreException
	 */
	public void setDocumentOp(GUID documentOp) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleDocumentOpId, documentOp);	
	}
	/**
	 * Connects this instance to the given instance using role "DocumentOp".
	 * If there is already an instance connected to this instance through role "DocumentOp", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param documentOp the instance to connect
	 * @throws PDStoreException
	 */
	public void setDocumentOp(PDDocumentOperation documentOp) throws PDStoreException {
		setDocumentOp(documentOp.getId());
	}



	/**
	 * Returns the instance connected to this instance through the role "FileOp".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDFileOperation getFileOp() throws PDStoreException {
	 	return (PDFileOperation)pdWorkingCopy.getInstance(this, roleFileOpId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "FileOp".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDFileOperation> getFileOps() throws PDStoreException {
	 	Set<PDFileOperation> result = new HashSet<PDFileOperation>();
	 	GUID PDFileOperationTypeId = new GUID("bc0f5b549ccc11e1ae94d8a25e8c53de");
		pdWorkingCopy.getInstances(this, roleFileOpId, PDFileOperation.class, PDFileOperationTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "FileOp".
	 * If the given instance is null, nothing happens.
	 * @param fileOp the instance to connect
	 * @throws PDStoreException
	 */
	public void addFileOp(GUID fileOp) throws PDStoreException {

			if (fileOp != null) {
				
				pdWorkingCopy.addLink(this.id, roleFileOpId, fileOp);
			}

	}


	/**
	 * Connects this instance to the given instance using role "FileOp".
	 * If the given instance is null, nothing happens.
	 * @param fileOp the instance to connect
	 * @throws PDStoreException
	 */
	public void addFileOp(PDFileOperation fileOp) throws PDStoreException {
		if (fileOp != null) {
			addFileOp(fileOp.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "FileOp".
	 * If the given collection of instances is null, nothing happens.
	 * @param fileOp the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addFileOps(Collection<PDFileOperation> fileOps) throws PDStoreException {
		if (fileOps == null)
			return;
		
		for (PDFileOperation instance : fileOps)
			addFileOp(instance);	
	}

	/**
	 * Removes the link from this instance through role "FileOp".
	 * @throws PDStoreException
	 */
	public void removeFileOp() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleFileOpId, 
			pdWorkingCopy.getInstance(this, roleFileOpId));
	}

	/**
	 * Removes the link from this instance through role "FileOp" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeFileOp(Object fileOp) throws PDStoreException {
		if (fileOp == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleFileOpId, fileOp);
	}

	/**
	 * Removes the links from this instance through role "FileOp" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeFileOps(Collection<PDFileOperation> fileOps) throws PDStoreException {
		if (fileOps == null)
			return;
		
		for (PDFileOperation instance : fileOps)
			pdWorkingCopy.removeLink(this.id, roleFileOpId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "FileOp".
	 * If there is already an instance connected to this instance through role "FileOp", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param fileOp the instance to connect
	 * @throws PDStoreException
	 */
	public void setFileOp(GUID fileOp) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleFileOpId, fileOp);	
	}
	/**
	 * Connects this instance to the given instance using role "FileOp".
	 * If there is already an instance connected to this instance through role "FileOp", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param fileOp the instance to connect
	 * @throws PDStoreException
	 */
	public void setFileOp(PDFileOperation fileOp) throws PDStoreException {
		setFileOp(fileOp.getId());
	}



	/**
	 * Returns the instance connected to this instance through the role "OpType".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDType getOpType() throws PDStoreException {
	 	return (PDType)pdWorkingCopy.getInstance(this, roleOpTypeId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "OpType".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDType> getOpTypes() throws PDStoreException {
	 	Set<PDType> result = new HashSet<PDType>();
	 	GUID PDTypeTypeId = new GUID("518a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleOpTypeId, PDType.class, PDTypeTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "OpType".
	 * If the given instance is null, nothing happens.
	 * @param opType the instance to connect
	 * @throws PDStoreException
	 */
	public void addOpType(GUID opType) throws PDStoreException {

			if (opType != null) {
				
				pdWorkingCopy.addLink(this.id, roleOpTypeId, opType);
			}

	}


	/**
	 * Connects this instance to the given instance using role "OpType".
	 * If the given instance is null, nothing happens.
	 * @param opType the instance to connect
	 * @throws PDStoreException
	 */
	public void addOpType(PDType opType) throws PDStoreException {
		if (opType != null) {
			addOpType(opType.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "OpType".
	 * If the given collection of instances is null, nothing happens.
	 * @param opType the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addOpTypes(Collection<PDType> opTypes) throws PDStoreException {
		if (opTypes == null)
			return;
		
		for (PDType instance : opTypes)
			addOpType(instance);	
	}

	/**
	 * Removes the link from this instance through role "OpType".
	 * @throws PDStoreException
	 */
	public void removeOpType() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleOpTypeId, 
			pdWorkingCopy.getInstance(this, roleOpTypeId));
	}

	/**
	 * Removes the link from this instance through role "OpType" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOpType(Object opType) throws PDStoreException {
		if (opType == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleOpTypeId, opType);
	}

	/**
	 * Removes the links from this instance through role "OpType" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOpTypes(Collection<PDType> opTypes) throws PDStoreException {
		if (opTypes == null)
			return;
		
		for (PDType instance : opTypes)
			pdWorkingCopy.removeLink(this.id, roleOpTypeId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "OpType".
	 * If there is already an instance connected to this instance through role "OpType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param opType the instance to connect
	 * @throws PDStoreException
	 */
	public void setOpType(GUID opType) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleOpTypeId, opType);	
	}
	/**
	 * Connects this instance to the given instance using role "OpType".
	 * If there is already an instance connected to this instance through role "OpType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param opType the instance to connect
	 * @throws PDStoreException
	 */
	public void setOpType(PDType opType) throws PDStoreException {
		setOpType(opType.getId());
	}



	/**
	 * Returns the instance connected to this instance through the role "NextOp".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDCMSOperation getNextOp() throws PDStoreException {
	 	return (PDCMSOperation)pdWorkingCopy.getInstance(this, roleNextOpId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "NextOp".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDCMSOperation> getNextOps() throws PDStoreException {
	 	Set<PDCMSOperation> result = new HashSet<PDCMSOperation>();
	 	GUID PDCMSOperationTypeId = new GUID("bc0f5b529ccc11e1ae94d8a25e8c53de");
		pdWorkingCopy.getInstances(this, roleNextOpId, PDCMSOperation.class, PDCMSOperationTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "NextOp".
	 * If the given instance is null, nothing happens.
	 * @param nextOp the instance to connect
	 * @throws PDStoreException
	 */
	public void addNextOp(GUID nextOp) throws PDStoreException {

			if (nextOp != null) {
				
				pdWorkingCopy.addLink(this.id, roleNextOpId, nextOp);
			}

	}


	/**
	 * Connects this instance to the given instance using role "NextOp".
	 * If the given instance is null, nothing happens.
	 * @param nextOp the instance to connect
	 * @throws PDStoreException
	 */
	public void addNextOp(PDCMSOperation nextOp) throws PDStoreException {
		if (nextOp != null) {
			addNextOp(nextOp.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "NextOp".
	 * If the given collection of instances is null, nothing happens.
	 * @param nextOp the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addNextOps(Collection<PDCMSOperation> nextOps) throws PDStoreException {
		if (nextOps == null)
			return;
		
		for (PDCMSOperation instance : nextOps)
			addNextOp(instance);	
	}

	/**
	 * Removes the link from this instance through role "NextOp".
	 * @throws PDStoreException
	 */
	public void removeNextOp() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleNextOpId, 
			pdWorkingCopy.getInstance(this, roleNextOpId));
	}

	/**
	 * Removes the link from this instance through role "NextOp" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeNextOp(Object nextOp) throws PDStoreException {
		if (nextOp == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleNextOpId, nextOp);
	}

	/**
	 * Removes the links from this instance through role "NextOp" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeNextOps(Collection<PDCMSOperation> nextOps) throws PDStoreException {
		if (nextOps == null)
			return;
		
		for (PDCMSOperation instance : nextOps)
			pdWorkingCopy.removeLink(this.id, roleNextOpId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "NextOp".
	 * If there is already an instance connected to this instance through role "NextOp", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param nextOp the instance to connect
	 * @throws PDStoreException
	 */
	public void setNextOp(GUID nextOp) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleNextOpId, nextOp);	
	}
	/**
	 * Connects this instance to the given instance using role "NextOp".
	 * If there is already an instance connected to this instance through role "NextOp", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param nextOp the instance to connect
	 * @throws PDStoreException
	 */
	public void setNextOp(PDCMSOperation nextOp) throws PDStoreException {
		setNextOp(nextOp.getId());
	}

}
