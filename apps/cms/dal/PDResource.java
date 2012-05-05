package cms.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Resource" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("cms.dal.PDResource");
 * @author PDGen
 */
public class PDResource implements PDInstance {

	public static final GUID typeId = new GUID("2e275f51965e11e18be2d8a25e8c53de"); 

	public static final GUID roleResourceFileNameId = new GUID("2e275f58965e11e18be2d8a25e8c53de");
	public static final GUID roleResourceTypeId = new GUID("ee32adf0f68b11df860e1cc1dec00ed3");
	public static final GUID roleResourceFileLocationId = new GUID("2e275f59965e11e18be2d8a25e8c53de");

	static {
		register();
	}
	
	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		DALClassRegister.addDataClass(typeId, PDResource.class);
	}
	
	private PDWorkingCopy pdWorkingCopy;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDResource:" + name;
		else
			return "PDResource:" + id;
	}
	
	/**
	 * Creates an PDResource object representing the given instance in the given cache.
	 * @param workingCopy the working copy the instance should be in
	 */
	public PDResource(PDWorkingCopy workingCopy) {
		this(workingCopy, new GUID());
	}
	
	/**
	 * Creates an PDResource object representing the given instance in the given copy.
	 * @param workingCopy the working copy the instance should be in
	 * @param id GUID of the instance
	 */
	public PDResource(PDWorkingCopy workingCopy, GUID id) {
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
	public static PDResource load(PDWorkingCopy pdWorkingCopy, GUID id) {
		PDInstance instance = pdWorkingCopy.load(typeId, id);
		return (PDResource)instance;
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
	 * Returns the instance connected to this instance through the role "ResourceFileName".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public String getResourceFileName() throws PDStoreException {
	 	return (String)pdWorkingCopy.getInstance(this, roleResourceFileNameId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "ResourceFileName".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<String> getResourceFileNames() throws PDStoreException {
	 	Set<String> result = new HashSet<String>();
	 	GUID StringTypeId = new GUID("4a8a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleResourceFileNameId, String.class, StringTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "ResourceFileName".
	 * If the given instance is null, nothing happens.
	 * @param resourceFileName the instance to connect
	 * @throws PDStoreException
	 */
	public void addResourceFileName(String resourceFileName) throws PDStoreException {

			if (resourceFileName != null) {
				
				pdWorkingCopy.addLink(this.id, roleResourceFileNameId, resourceFileName);
			}

	}

	/**
	 * Connects this instance to the given instances using role "ResourceFileName".
	 * If the given collection of instances is null, nothing happens.
	 * @param resourceFileName the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addResourceFileNames(Collection<String> resourceFileNames) throws PDStoreException {
		if (resourceFileNames == null)
			return;

		for (String instance : resourceFileNames)
			addResourceFileName(instance);
	}


	/**
	 * Removes the link from this instance through role "ResourceFileName".
	 * @throws PDStoreException
	 */
	public void removeResourceFileName() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleResourceFileNameId, 
			pdWorkingCopy.getInstance(this, roleResourceFileNameId));
	}

	/**
	 * Removes the link from this instance through role "ResourceFileName" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeResourceFileName(Object resourceFileName) throws PDStoreException {
		if (resourceFileName == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleResourceFileNameId, resourceFileName);
	}


   /**
	 * Connects this instance to the given instance using role "ResourceFileName".
	 * If there is already an instance connected to this instance through role "ResourceFileName", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param resourceFileName the instance to connect
	 * @throws PDStoreException
	 */
	public void setResourceFileName(String resourceFileName) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleResourceFileNameId, resourceFileName);	
	}


	/**
	 * Returns the instance connected to this instance through the role "ResourceType".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDType getResourceType() throws PDStoreException {
	 	return (PDType)pdWorkingCopy.getInstance(this, roleResourceTypeId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "ResourceType".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDType> getResourceTypes() throws PDStoreException {
	 	Set<PDType> result = new HashSet<PDType>();
	 	GUID PDTypeTypeId = new GUID("518a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleResourceTypeId, PDType.class, PDTypeTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "ResourceType".
	 * If the given instance is null, nothing happens.
	 * @param resourceType the instance to connect
	 * @throws PDStoreException
	 */
	public void addResourceType(GUID resourceType) throws PDStoreException {

			if (resourceType != null) {
				
				pdWorkingCopy.addLink(this.id, roleResourceTypeId, resourceType);
			}

	}


	/**
	 * Connects this instance to the given instance using role "ResourceType".
	 * If the given instance is null, nothing happens.
	 * @param resourceType the instance to connect
	 * @throws PDStoreException
	 */
	public void addResourceType(PDType resourceType) throws PDStoreException {
		if (resourceType != null) {
			addResourceType(resourceType.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "ResourceType".
	 * If the given collection of instances is null, nothing happens.
	 * @param resourceType the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addResourceTypes(Collection<PDType> resourceTypes) throws PDStoreException {
		if (resourceTypes == null)
			return;
		
		for (PDType instance : resourceTypes)
			addResourceType(instance);	
	}

	/**
	 * Removes the link from this instance through role "ResourceType".
	 * @throws PDStoreException
	 */
	public void removeResourceType() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleResourceTypeId, 
			pdWorkingCopy.getInstance(this, roleResourceTypeId));
	}

	/**
	 * Removes the link from this instance through role "ResourceType" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeResourceType(Object resourceType) throws PDStoreException {
		if (resourceType == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleResourceTypeId, resourceType);
	}

	/**
	 * Removes the links from this instance through role "ResourceType" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeResourceTypes(Collection<PDType> resourceTypes) throws PDStoreException {
		if (resourceTypes == null)
			return;
		
		for (PDType instance : resourceTypes)
			pdWorkingCopy.removeLink(this.id, roleResourceTypeId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "ResourceType".
	 * If there is already an instance connected to this instance through role "ResourceType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param resourceType the instance to connect
	 * @throws PDStoreException
	 */
	public void setResourceType(GUID resourceType) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleResourceTypeId, resourceType);	
	}
	/**
	 * Connects this instance to the given instance using role "ResourceType".
	 * If there is already an instance connected to this instance through role "ResourceType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param resourceType the instance to connect
	 * @throws PDStoreException
	 */
	public void setResourceType(PDType resourceType) throws PDStoreException {
		setResourceType(resourceType.getId());
	}



	/**
	 * Returns the instance connected to this instance through the role "ResourceFileLocation".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public String getResourceFileLocation() throws PDStoreException {
	 	return (String)pdWorkingCopy.getInstance(this, roleResourceFileLocationId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "ResourceFileLocation".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<String> getResourceFileLocations() throws PDStoreException {
	 	Set<String> result = new HashSet<String>();
	 	GUID StringTypeId = new GUID("4a8a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleResourceFileLocationId, String.class, StringTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "ResourceFileLocation".
	 * If the given instance is null, nothing happens.
	 * @param resourceFileLocation the instance to connect
	 * @throws PDStoreException
	 */
	public void addResourceFileLocation(String resourceFileLocation) throws PDStoreException {

			if (resourceFileLocation != null) {
				
				pdWorkingCopy.addLink(this.id, roleResourceFileLocationId, resourceFileLocation);
			}

	}

	/**
	 * Connects this instance to the given instances using role "ResourceFileLocation".
	 * If the given collection of instances is null, nothing happens.
	 * @param resourceFileLocation the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addResourceFileLocations(Collection<String> resourceFileLocations) throws PDStoreException {
		if (resourceFileLocations == null)
			return;

		for (String instance : resourceFileLocations)
			addResourceFileLocation(instance);
	}


	/**
	 * Removes the link from this instance through role "ResourceFileLocation".
	 * @throws PDStoreException
	 */
	public void removeResourceFileLocation() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleResourceFileLocationId, 
			pdWorkingCopy.getInstance(this, roleResourceFileLocationId));
	}

	/**
	 * Removes the link from this instance through role "ResourceFileLocation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeResourceFileLocation(Object resourceFileLocation) throws PDStoreException {
		if (resourceFileLocation == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleResourceFileLocationId, resourceFileLocation);
	}


   /**
	 * Connects this instance to the given instance using role "ResourceFileLocation".
	 * If there is already an instance connected to this instance through role "ResourceFileLocation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param resourceFileLocation the instance to connect
	 * @throws PDStoreException
	 */
	public void setResourceFileLocation(String resourceFileLocation) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleResourceFileLocationId, resourceFileLocation);	
	}
}
