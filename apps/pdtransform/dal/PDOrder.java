package pdtransform.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Order" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdtransform.dal.PDOrder");
 * @author PDGen
 */
public class PDOrder implements PDInstance {

	public static final GUID typeId = new GUID("b2ba85348e0ede11980f9a097666e103"); 

	public static final GUID roleOrderedPairsId = new GUID("b7ba85348e0ede11980f9a097666e103");

	static {
		DALClassRegister.addDataClass(typeId, PDOrder.class);
	}
	private PDWorkingCopy pdWorkingCopy;
	private GUID id;
	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDOrder:" + name;
		else
			return "PDOrder:" + id;
	}
	/**
	 * Creates an PDOrder object representing the given instance in the given cache.
	 * @param workingCopy the working copy the instance should be in
	 */
	public PDOrder(PDWorkingCopy workingCopy) {
		this(workingCopy, new GUID());
	}
	
	/**
	 * Creates an PDOrder object representing the given instance in the given cache.
	 * @param workingCopy the working copy the instance should be in
	 * @param id GUID of the instance
	 */
	public PDOrder(PDWorkingCopy workingCopy, GUID id) {
		this.pdWorkingCopy = workingCopy;
		this.id = id;
		
		
	}
	/**
	 * Loads an instance object of this type into a cache.
	 * If the instance is already in the cache, the cached instance is returned.
	 * @param PDWorkingCopy pdWorkingCopy to load the instance into
	 * @param id GUID of the instance
	 * Do not directly call this method. Use the newInstance() method in PDCache which would call this method
	 */
	public static PDOrder load(PDWorkingCopy pdWorkingCopy, GUID id) {
		PDInstance instance = pdWorkingCopy.load(typeId, id);
		return (PDOrder)instance;
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
	 * Gets the name of this instance.
	 * In PDStore every instance can be given a name.
	 * @return name the instance name
	 * @throws PDStoreException
	 */
	public String getName() throws PDStoreException {
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
	public void setName(String name) throws PDStoreException {
		pdWorkingCopy.setName(id, name);
	}
	/**
	 * Removes the name of this instance.
	 * In PDStore every instance can be given a name.
	 * If the instance does not have a name, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeName() throws PDStoreException {
		pdWorkingCopy.removeName(id);
	}
	/**
	 * Gets the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * @return icon the instance icon
	 * @throws PDStoreException
	 */
	public Blob getIcon() throws PDStoreException {
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
	public void setIcon(Blob icon) throws PDStoreException {
		pdWorkingCopy.setIcon(id, icon);
	}
	/**
	 * Removes the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * If the instance does not have an icon, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeIcon() throws PDStoreException {
		pdWorkingCopy.removeIcon(id);
	}

	
	  // Code generated by PDGetSetRemoveGen:


	/**
	 * Returns the instance connected to this instance through the role "ordered pairs".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDOrderedPair getOrderedPairs() throws PDStoreException {
	 	return (PDOrderedPair)pdWorkingCopy.getInstance(this, roleOrderedPairsId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "ordered pairs".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDOrderedPair> getOrderedPairss() throws PDStoreException {
	 	Set<PDOrderedPair> result = new HashSet<PDOrderedPair>();
	 	GUID PDOrderedPairTypeId = new GUID("b3ba85348e0ede11980f9a097666e103");
		pdWorkingCopy.getInstances(this, roleOrderedPairsId, PDOrderedPair.class, PDOrderedPairTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "ordered pairs".
	 * If the given instance is null, nothing happens.
	 * @param orderedPairs the instance to connect
	 * @throws PDStoreException
	 */
	public void addOrderedPairs(GUID orderedPairs) throws PDStoreException {

			if (orderedPairs != null) {
				
				pdWorkingCopy.addLink(this.id, roleOrderedPairsId, orderedPairs);
			}

	}

	/**
	 * Connects this instance to the given instance using role "ordered pairs".
	 * If the given instance is null, nothing happens.
	 * @param orderedPairs the instance to connect
	 * @throws PDStoreException
	 */
	public void addOrderedPairs(PDOrderedPair orderedPairs) throws PDStoreException {
		if (orderedPairs != null) {
			addOrderedPairs(orderedPairs.getId());
		}		
	}

	/**
	 * Removes the link from this instance through role "ordered pairs".
	 * @throws PDStoreException
	 */
	public void removeOrderedPairs() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleOrderedPairsId, 
			pdWorkingCopy.getInstance(this, roleOrderedPairsId));
	}

	/**
	 * Removes the link from this instance through role "ordered pairs" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.orderedPairs.getId()
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOrderedPairs(Object orderedPairs) throws PDStoreException {
		if (orderedPairs==null)
			return;
		pdWorkingCopy.removeLink(this.id, roleOrderedPairsId, orderedPairs);
	}

   /**
	 * Connects this instance to the given instance using role "ordered pairs".
	 * If there is already an instance connected to this instance through role "ordered pairs", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param orderedPairs the instance to connect
	 * @throws PDStoreException
	 */
	public void setOrderedPairs(GUID orderedPairs) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleOrderedPairsId, orderedPairs);	
	}
	/**
	 * Connects this instance to the given instance using role "ordered pairs".
	 * If there is already an instance connected to this instance through role "ordered pairs", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param orderedPairs the instance to connect
	 * @throws PDStoreException
	 */
	public void setOrderedPairs(PDOrderedPair orderedPairs) throws PDStoreException {
		setOrderedPairs(orderedPairs.getId());
	}

}
