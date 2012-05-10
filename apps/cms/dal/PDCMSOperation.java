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

	public static final GUID typeId = new GUID("599131e29aa311e190cbd8a25e8c53de"); 

	public static final GUID roleCMSOperationId = new GUID("599131f89aa311e190cbd8a25e8c53de");
	public static final GUID roleOpTypeId = new GUID("ee32adf0f68b11df860e1cc1dec00ed3");

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
	 * Returns the instance connected to this instance through the role "CMSOperation".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDCMSOperation getCMSOperation() throws PDStoreException {
	 	return (PDCMSOperation)pdWorkingCopy.getInstance(this, roleCMSOperationId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "CMSOperation".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDCMSOperation> getCMSOperations() throws PDStoreException {
	 	Set<PDCMSOperation> result = new HashSet<PDCMSOperation>();
	 	GUID PDCMSOperationTypeId = new GUID("599131e29aa311e190cbd8a25e8c53de");
		pdWorkingCopy.getInstances(this, roleCMSOperationId, PDCMSOperation.class, PDCMSOperationTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "CMSOperation".
	 * If the given instance is null, nothing happens.
	 * @param cMSOperation the instance to connect
	 * @throws PDStoreException
	 */
	public void addCMSOperation(GUID cMSOperation) throws PDStoreException {

			if (cMSOperation != null) {
				
				pdWorkingCopy.addLink(this.id, roleCMSOperationId, cMSOperation);
			}

	}


	/**
	 * Connects this instance to the given instance using role "CMSOperation".
	 * If the given instance is null, nothing happens.
	 * @param cMSOperation the instance to connect
	 * @throws PDStoreException
	 */
	public void addCMSOperation(PDCMSOperation cMSOperation) throws PDStoreException {
		if (cMSOperation != null) {
			addCMSOperation(cMSOperation.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "CMSOperation".
	 * If the given collection of instances is null, nothing happens.
	 * @param cMSOperation the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addCMSOperations(Collection<PDCMSOperation> cMSOperations) throws PDStoreException {
		if (cMSOperations == null)
			return;
		
		for (PDCMSOperation instance : cMSOperations)
			addCMSOperation(instance);	
	}

	/**
	 * Removes the link from this instance through role "CMSOperation".
	 * @throws PDStoreException
	 */
	public void removeCMSOperation() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleCMSOperationId, 
			pdWorkingCopy.getInstance(this, roleCMSOperationId));
	}

	/**
	 * Removes the link from this instance through role "CMSOperation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeCMSOperation(Object cMSOperation) throws PDStoreException {
		if (cMSOperation == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleCMSOperationId, cMSOperation);
	}

	/**
	 * Removes the links from this instance through role "CMSOperation" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeCMSOperations(Collection<PDCMSOperation> cMSOperations) throws PDStoreException {
		if (cMSOperations == null)
			return;
		
		for (PDCMSOperation instance : cMSOperations)
			pdWorkingCopy.removeLink(this.id, roleCMSOperationId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "CMSOperation".
	 * If there is already an instance connected to this instance through role "CMSOperation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param cMSOperation the instance to connect
	 * @throws PDStoreException
	 */
	public void setCMSOperation(GUID cMSOperation) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleCMSOperationId, cMSOperation);	
	}
	/**
	 * Connects this instance to the given instance using role "CMSOperation".
	 * If there is already an instance connected to this instance through role "CMSOperation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param cMSOperation the instance to connect
	 * @throws PDStoreException
	 */
	public void setCMSOperation(PDCMSOperation cMSOperation) throws PDStoreException {
		setCMSOperation(cMSOperation.getId());
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

}
