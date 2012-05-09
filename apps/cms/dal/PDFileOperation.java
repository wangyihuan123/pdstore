package cms.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "FileOperation" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("cms.dal.PDFileOperation");
 * @author PDGen
 */
public class PDFileOperation implements PDInstance {

	public static final GUID typeId = new GUID("0a83d543999e11e18ad8d8a25e8c53de"); 

	public static final GUID roleOpParamBId = new GUID("0a83d557999e11e18ad8d8a25e8c53de");
	public static final GUID roleOpUserId = new GUID("0a83d555999e11e18ad8d8a25e8c53de");
	public static final GUID roleOpParamAId = new GUID("0a83d556999e11e18ad8d8a25e8c53de");
	public static final GUID roleOpTypeId = new GUID("0a83d554999e11e18ad8d8a25e8c53de");

	static {
		register();
	}
	
	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		DALClassRegister.addDataClass(typeId, PDFileOperation.class);
	}
	
	private PDWorkingCopy pdWorkingCopy;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDFileOperation:" + name;
		else
			return "PDFileOperation:" + id;
	}
	
	/**
	 * Creates an PDFileOperation object representing the given instance in the given cache.
	 * @param workingCopy the working copy the instance should be in
	 */
	public PDFileOperation(PDWorkingCopy workingCopy) {
		this(workingCopy, new GUID());
	}
	
	/**
	 * Creates an PDFileOperation object representing the given instance in the given copy.
	 * @param workingCopy the working copy the instance should be in
	 * @param id GUID of the instance
	 */
	public PDFileOperation(PDWorkingCopy workingCopy, GUID id) {
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
	public static PDFileOperation load(PDWorkingCopy pdWorkingCopy, GUID id) {
		PDInstance instance = pdWorkingCopy.load(typeId, id);
		return (PDFileOperation)instance;
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
	 * Returns the instance connected to this instance through the role "OpParamB".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public String getOpParamB() throws PDStoreException {
	 	return (String)pdWorkingCopy.getInstance(this, roleOpParamBId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "OpParamB".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<String> getOpParamBs() throws PDStoreException {
	 	Set<String> result = new HashSet<String>();
	 	GUID StringTypeId = new GUID("4a8a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleOpParamBId, String.class, StringTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "OpParamB".
	 * If the given instance is null, nothing happens.
	 * @param opParamB the instance to connect
	 * @throws PDStoreException
	 */
	public void addOpParamB(String opParamB) throws PDStoreException {

			if (opParamB != null) {
				
				pdWorkingCopy.addLink(this.id, roleOpParamBId, opParamB);
			}

	}

	/**
	 * Connects this instance to the given instances using role "OpParamB".
	 * If the given collection of instances is null, nothing happens.
	 * @param opParamB the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addOpParamBs(Collection<String> opParamBs) throws PDStoreException {
		if (opParamBs == null)
			return;

		for (String instance : opParamBs)
			addOpParamB(instance);
	}


	/**
	 * Removes the link from this instance through role "OpParamB".
	 * @throws PDStoreException
	 */
	public void removeOpParamB() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleOpParamBId, 
			pdWorkingCopy.getInstance(this, roleOpParamBId));
	}

	/**
	 * Removes the link from this instance through role "OpParamB" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOpParamB(Object opParamB) throws PDStoreException {
		if (opParamB == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleOpParamBId, opParamB);
	}


   /**
	 * Connects this instance to the given instance using role "OpParamB".
	 * If there is already an instance connected to this instance through role "OpParamB", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param opParamB the instance to connect
	 * @throws PDStoreException
	 */
	public void setOpParamB(String opParamB) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleOpParamBId, opParamB);	
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
	 	GUID PDUserTypeId = new GUID("0a83d540999e11e18ad8d8a25e8c53de");
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



	/**
	 * Returns the instance connected to this instance through the role "OpParamA".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public String getOpParamA() throws PDStoreException {
	 	return (String)pdWorkingCopy.getInstance(this, roleOpParamAId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "OpParamA".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<String> getOpParamAs() throws PDStoreException {
	 	Set<String> result = new HashSet<String>();
	 	GUID StringTypeId = new GUID("4a8a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleOpParamAId, String.class, StringTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "OpParamA".
	 * If the given instance is null, nothing happens.
	 * @param opParamA the instance to connect
	 * @throws PDStoreException
	 */
	public void addOpParamA(String opParamA) throws PDStoreException {

			if (opParamA != null) {
				
				pdWorkingCopy.addLink(this.id, roleOpParamAId, opParamA);
			}

	}

	/**
	 * Connects this instance to the given instances using role "OpParamA".
	 * If the given collection of instances is null, nothing happens.
	 * @param opParamA the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addOpParamAs(Collection<String> opParamAs) throws PDStoreException {
		if (opParamAs == null)
			return;

		for (String instance : opParamAs)
			addOpParamA(instance);
	}


	/**
	 * Removes the link from this instance through role "OpParamA".
	 * @throws PDStoreException
	 */
	public void removeOpParamA() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleOpParamAId, 
			pdWorkingCopy.getInstance(this, roleOpParamAId));
	}

	/**
	 * Removes the link from this instance through role "OpParamA" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOpParamA(Object opParamA) throws PDStoreException {
		if (opParamA == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleOpParamAId, opParamA);
	}


   /**
	 * Connects this instance to the given instance using role "OpParamA".
	 * If there is already an instance connected to this instance through role "OpParamA", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param opParamA the instance to connect
	 * @throws PDStoreException
	 */
	public void setOpParamA(String opParamA) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleOpParamAId, opParamA);	
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
}
