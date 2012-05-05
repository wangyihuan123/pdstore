package cms.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Operation" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("cms.dal.PDOperation");
 * @author PDGen
 */
public class PDOperation implements PDInstance {

	public static final GUID typeId = new GUID("2e275f55965e11e18be2d8a25e8c53de"); 

	public static final GUID roleOpTypeId = new GUID("2e275f60965e11e18be2d8a25e8c53de");
	public static final GUID roleNextOpId = new GUID("2e275f63965e11e18be2d8a25e8c53de");
	public static final GUID rolePrevOpId = new GUID("2e275f62965e11e18be2d8a25e8c53de");
	public static final GUID roleOpUserId = new GUID("2e275f61965e11e18be2d8a25e8c53de");

	static {
		register();
	}
	
	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		DALClassRegister.addDataClass(typeId, PDOperation.class);
	}
	
	private PDWorkingCopy pdWorkingCopy;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDOperation:" + name;
		else
			return "PDOperation:" + id;
	}
	
	/**
	 * Creates an PDOperation object representing the given instance in the given cache.
	 * @param workingCopy the working copy the instance should be in
	 */
	public PDOperation(PDWorkingCopy workingCopy) {
		this(workingCopy, new GUID());
	}
	
	/**
	 * Creates an PDOperation object representing the given instance in the given copy.
	 * @param workingCopy the working copy the instance should be in
	 * @param id GUID of the instance
	 */
	public PDOperation(PDWorkingCopy workingCopy, GUID id) {
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
	public static PDOperation load(PDWorkingCopy pdWorkingCopy, GUID id) {
		PDInstance instance = pdWorkingCopy.load(typeId, id);
		return (PDOperation)instance;
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
	 * Returns the instance connected to this instance through the role "OpType".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public Long getOpType() throws PDStoreException {
	 	return (Long)pdWorkingCopy.getInstance(this, roleOpTypeId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "OpType".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<Long> getOpTypes() throws PDStoreException {
	 	Set<Long> result = new HashSet<Long>();
	 	GUID LongTypeId = new GUID("4b8a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleOpTypeId, Long.class, LongTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "OpType".
	 * If the given instance is null, nothing happens.
	 * @param opType the instance to connect
	 * @throws PDStoreException
	 */
	public void addOpType(Long opType) throws PDStoreException {

			if (opType != null) {
				
				pdWorkingCopy.addLink(this.id, roleOpTypeId, opType);
			}

	}

	/**
	 * Connects this instance to the given instances using role "OpType".
	 * If the given collection of instances is null, nothing happens.
	 * @param opType the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addOpTypes(Collection<Long> opTypes) throws PDStoreException {
		if (opTypes == null)
			return;

		for (Long instance : opTypes)
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
	 * Connects this instance to the given instance using role "OpType".
	 * If there is already an instance connected to this instance through role "OpType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param opType the instance to connect
	 * @throws PDStoreException
	 */
	public void setOpType(Long opType) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleOpTypeId, opType);	
	}


	/**
	 * Returns the instance connected to this instance through the role "NextOp".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDOperation getNextOp() throws PDStoreException {
	 	return (PDOperation)pdWorkingCopy.getInstance(this, roleNextOpId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "NextOp".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDOperation> getNextOps() throws PDStoreException {
	 	Set<PDOperation> result = new HashSet<PDOperation>();
	 	GUID PDOperationTypeId = new GUID("2e275f55965e11e18be2d8a25e8c53de");
		pdWorkingCopy.getInstances(this, roleNextOpId, PDOperation.class, PDOperationTypeId, result);
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
	public void addNextOp(PDOperation nextOp) throws PDStoreException {
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
	public void addNextOps(Collection<PDOperation> nextOps) throws PDStoreException {
		if (nextOps == null)
			return;
		
		for (PDOperation instance : nextOps)
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
	public void removeNextOps(Collection<PDOperation> nextOps) throws PDStoreException {
		if (nextOps == null)
			return;
		
		for (PDOperation instance : nextOps)
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
	public void setNextOp(PDOperation nextOp) throws PDStoreException {
		setNextOp(nextOp.getId());
	}



	/**
	 * Returns the instance connected to this instance through the role "PrevOp".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDOperation getPrevOp() throws PDStoreException {
	 	return (PDOperation)pdWorkingCopy.getInstance(this, rolePrevOpId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "PrevOp".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDOperation> getPrevOps() throws PDStoreException {
	 	Set<PDOperation> result = new HashSet<PDOperation>();
	 	GUID PDOperationTypeId = new GUID("2e275f55965e11e18be2d8a25e8c53de");
		pdWorkingCopy.getInstances(this, rolePrevOpId, PDOperation.class, PDOperationTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "PrevOp".
	 * If the given instance is null, nothing happens.
	 * @param prevOp the instance to connect
	 * @throws PDStoreException
	 */
	public void addPrevOp(GUID prevOp) throws PDStoreException {

			if (prevOp != null) {
				
				pdWorkingCopy.addLink(this.id, rolePrevOpId, prevOp);
			}

	}


	/**
	 * Connects this instance to the given instance using role "PrevOp".
	 * If the given instance is null, nothing happens.
	 * @param prevOp the instance to connect
	 * @throws PDStoreException
	 */
	public void addPrevOp(PDOperation prevOp) throws PDStoreException {
		if (prevOp != null) {
			addPrevOp(prevOp.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "PrevOp".
	 * If the given collection of instances is null, nothing happens.
	 * @param prevOp the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addPrevOps(Collection<PDOperation> prevOps) throws PDStoreException {
		if (prevOps == null)
			return;
		
		for (PDOperation instance : prevOps)
			addPrevOp(instance);	
	}

	/**
	 * Removes the link from this instance through role "PrevOp".
	 * @throws PDStoreException
	 */
	public void removePrevOp() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, rolePrevOpId, 
			pdWorkingCopy.getInstance(this, rolePrevOpId));
	}

	/**
	 * Removes the link from this instance through role "PrevOp" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removePrevOp(Object prevOp) throws PDStoreException {
		if (prevOp == null)
			return;
		pdWorkingCopy.removeLink(this.id, rolePrevOpId, prevOp);
	}

	/**
	 * Removes the links from this instance through role "PrevOp" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removePrevOps(Collection<PDOperation> prevOps) throws PDStoreException {
		if (prevOps == null)
			return;
		
		for (PDOperation instance : prevOps)
			pdWorkingCopy.removeLink(this.id, rolePrevOpId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "PrevOp".
	 * If there is already an instance connected to this instance through role "PrevOp", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param prevOp the instance to connect
	 * @throws PDStoreException
	 */
	public void setPrevOp(GUID prevOp) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  rolePrevOpId, prevOp);	
	}
	/**
	 * Connects this instance to the given instance using role "PrevOp".
	 * If there is already an instance connected to this instance through role "PrevOp", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param prevOp the instance to connect
	 * @throws PDStoreException
	 */
	public void setPrevOp(PDOperation prevOp) throws PDStoreException {
		setPrevOp(prevOp.getId());
	}



	/**
	 * Returns the instance connected to this instance through the role "OpUser".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDUser getOpUser() throws PDStoreException {
	 	return (PDUser)pdWorkingCopy.getInstance(this, roleOpUserId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "OpUser".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDUser> getOpUsers() throws PDStoreException {
	 	Set<PDUser> result = new HashSet<PDUser>();
	 	GUID PDUserTypeId = new GUID("2e275f50965e11e18be2d8a25e8c53de");
		pdWorkingCopy.getInstances(this, roleOpUserId, PDUser.class, PDUserTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "OpUser".
	 * If the given instance is null, nothing happens.
	 * @param opUser the instance to connect
	 * @throws PDStoreException
	 */
	public void addOpUser(GUID opUser) throws PDStoreException {

			if (opUser != null) {
				
				pdWorkingCopy.addLink(this.id, roleOpUserId, opUser);
			}

	}


	/**
	 * Connects this instance to the given instance using role "OpUser".
	 * If the given instance is null, nothing happens.
	 * @param opUser the instance to connect
	 * @throws PDStoreException
	 */
	public void addOpUser(PDUser opUser) throws PDStoreException {
		if (opUser != null) {
			addOpUser(opUser.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "OpUser".
	 * If the given collection of instances is null, nothing happens.
	 * @param opUser the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addOpUsers(Collection<PDUser> opUsers) throws PDStoreException {
		if (opUsers == null)
			return;
		
		for (PDUser instance : opUsers)
			addOpUser(instance);	
	}

	/**
	 * Removes the link from this instance through role "OpUser".
	 * @throws PDStoreException
	 */
	public void removeOpUser() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleOpUserId, 
			pdWorkingCopy.getInstance(this, roleOpUserId));
	}

	/**
	 * Removes the link from this instance through role "OpUser" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOpUser(Object opUser) throws PDStoreException {
		if (opUser == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleOpUserId, opUser);
	}

	/**
	 * Removes the links from this instance through role "OpUser" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOpUsers(Collection<PDUser> opUsers) throws PDStoreException {
		if (opUsers == null)
			return;
		
		for (PDUser instance : opUsers)
			pdWorkingCopy.removeLink(this.id, roleOpUserId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "OpUser".
	 * If there is already an instance connected to this instance through role "OpUser", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param opUser the instance to connect
	 * @throws PDStoreException
	 */
	public void setOpUser(GUID opUser) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleOpUserId, opUser);	
	}
	/**
	 * Connects this instance to the given instance using role "OpUser".
	 * If there is already an instance connected to this instance through role "OpUser", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param opUser the instance to connect
	 * @throws PDStoreException
	 */
	public void setOpUser(PDUser opUser) throws PDStoreException {
		setOpUser(opUser.getId());
	}

}
