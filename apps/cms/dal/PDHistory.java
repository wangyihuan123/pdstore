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

	public static final GUID typeId = new GUID("2e275f54965e11e18be2d8a25e8c53de"); 

	public static final GUID roleFirstOpId = new GUID("2e275f5f965e11e18be2d8a25e8c53de");

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
	 * Returns the instance connected to this instance through the role "FirstOp".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDOperation getFirstOp() throws PDStoreException {
	 	return (PDOperation)pdWorkingCopy.getInstance(this, roleFirstOpId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "FirstOp".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDOperation> getFirstOps() throws PDStoreException {
	 	Set<PDOperation> result = new HashSet<PDOperation>();
	 	GUID PDOperationTypeId = new GUID("2e275f55965e11e18be2d8a25e8c53de");
		pdWorkingCopy.getInstances(this, roleFirstOpId, PDOperation.class, PDOperationTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "FirstOp".
	 * If the given instance is null, nothing happens.
	 * @param firstOp the instance to connect
	 * @throws PDStoreException
	 */
	public void addFirstOp(GUID firstOp) throws PDStoreException {

			if (firstOp != null) {
				
				pdWorkingCopy.addLink(this.id, roleFirstOpId, firstOp);
			}

	}


	/**
	 * Connects this instance to the given instance using role "FirstOp".
	 * If the given instance is null, nothing happens.
	 * @param firstOp the instance to connect
	 * @throws PDStoreException
	 */
	public void addFirstOp(PDOperation firstOp) throws PDStoreException {
		if (firstOp != null) {
			addFirstOp(firstOp.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "FirstOp".
	 * If the given collection of instances is null, nothing happens.
	 * @param firstOp the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addFirstOps(Collection<PDOperation> firstOps) throws PDStoreException {
		if (firstOps == null)
			return;
		
		for (PDOperation instance : firstOps)
			addFirstOp(instance);	
	}

	/**
	 * Removes the link from this instance through role "FirstOp".
	 * @throws PDStoreException
	 */
	public void removeFirstOp() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleFirstOpId, 
			pdWorkingCopy.getInstance(this, roleFirstOpId));
	}

	/**
	 * Removes the link from this instance through role "FirstOp" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeFirstOp(Object firstOp) throws PDStoreException {
		if (firstOp == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleFirstOpId, firstOp);
	}

	/**
	 * Removes the links from this instance through role "FirstOp" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeFirstOps(Collection<PDOperation> firstOps) throws PDStoreException {
		if (firstOps == null)
			return;
		
		for (PDOperation instance : firstOps)
			pdWorkingCopy.removeLink(this.id, roleFirstOpId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "FirstOp".
	 * If there is already an instance connected to this instance through role "FirstOp", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param firstOp the instance to connect
	 * @throws PDStoreException
	 */
	public void setFirstOp(GUID firstOp) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleFirstOpId, firstOp);	
	}
	/**
	 * Connects this instance to the given instance using role "FirstOp".
	 * If there is already an instance connected to this instance through role "FirstOp", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param firstOp the instance to connect
	 * @throws PDStoreException
	 */
	public void setFirstOp(PDOperation firstOp) throws PDStoreException {
		setFirstOp(firstOp.getId());
	}

}
