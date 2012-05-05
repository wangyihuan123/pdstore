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

	public static final GUID typeId = new GUID("70cc22b396bc11e184cdd8a25e8c53de"); 

	public static final GUID roleOperationId = new GUID("70cc22be96bc11e184cdd8a25e8c53de");

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
	 * Returns the instance connected to this instance through the role "Operation".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDOperation getOperation() throws PDStoreException {
	 	return (PDOperation)pdWorkingCopy.getInstance(this, roleOperationId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "Operation".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDOperation> getOperations() throws PDStoreException {
	 	Set<PDOperation> result = new HashSet<PDOperation>();
	 	GUID PDOperationTypeId = new GUID("70cc22b496bc11e184cdd8a25e8c53de");
		pdWorkingCopy.getInstances(this, roleOperationId, PDOperation.class, PDOperationTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "Operation".
	 * If the given instance is null, nothing happens.
	 * @param operation the instance to connect
	 * @throws PDStoreException
	 */
	public void addOperation(GUID operation) throws PDStoreException {

			if (operation != null) {
				
				pdWorkingCopy.addLink(this.id, roleOperationId, operation);
			}

	}


	/**
	 * Connects this instance to the given instance using role "Operation".
	 * If the given instance is null, nothing happens.
	 * @param operation the instance to connect
	 * @throws PDStoreException
	 */
	public void addOperation(PDOperation operation) throws PDStoreException {
		if (operation != null) {
			addOperation(operation.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "Operation".
	 * If the given collection of instances is null, nothing happens.
	 * @param operation the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addOperations(Collection<PDOperation> operations) throws PDStoreException {
		if (operations == null)
			return;
		
		for (PDOperation instance : operations)
			addOperation(instance);	
	}

	/**
	 * Removes the link from this instance through role "Operation".
	 * @throws PDStoreException
	 */
	public void removeOperation() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleOperationId, 
			pdWorkingCopy.getInstance(this, roleOperationId));
	}

	/**
	 * Removes the link from this instance through role "Operation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOperation(Object operation) throws PDStoreException {
		if (operation == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleOperationId, operation);
	}

	/**
	 * Removes the links from this instance through role "Operation" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOperations(Collection<PDOperation> operations) throws PDStoreException {
		if (operations == null)
			return;
		
		for (PDOperation instance : operations)
			pdWorkingCopy.removeLink(this.id, roleOperationId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "Operation".
	 * If there is already an instance connected to this instance through role "Operation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param operation the instance to connect
	 * @throws PDStoreException
	 */
	public void setOperation(GUID operation) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleOperationId, operation);	
	}
	/**
	 * Connects this instance to the given instance using role "Operation".
	 * If there is already an instance connected to this instance through role "Operation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param operation the instance to connect
	 * @throws PDStoreException
	 */
	public void setOperation(PDOperation operation) throws PDStoreException {
		setOperation(operation.getId());
	}

}
