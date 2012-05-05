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

	public static final GUID typeId = new GUID("2e275f52965e11e18be2d8a25e8c53de"); 

	public static final GUID roleFirstCharId = new GUID("2e275f5a965e11e18be2d8a25e8c53de");
	public static final GUID roleLastCharId = new GUID("2e275f5b965e11e18be2d8a25e8c53de");

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
	 * Returns the instance connected to this instance through the role "FirstChar".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDCharacter getFirstChar() throws PDStoreException {
	 	return (PDCharacter)pdWorkingCopy.getInstance(this, roleFirstCharId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "FirstChar".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDCharacter> getFirstChars() throws PDStoreException {
	 	Set<PDCharacter> result = new HashSet<PDCharacter>();
	 	GUID PDCharacterTypeId = new GUID("2e275f53965e11e18be2d8a25e8c53de");
		pdWorkingCopy.getInstances(this, roleFirstCharId, PDCharacter.class, PDCharacterTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "FirstChar".
	 * If the given instance is null, nothing happens.
	 * @param firstChar the instance to connect
	 * @throws PDStoreException
	 */
	public void addFirstChar(GUID firstChar) throws PDStoreException {

			if (firstChar != null) {
				
				pdWorkingCopy.addLink(this.id, roleFirstCharId, firstChar);
			}

	}


	/**
	 * Connects this instance to the given instance using role "FirstChar".
	 * If the given instance is null, nothing happens.
	 * @param firstChar the instance to connect
	 * @throws PDStoreException
	 */
	public void addFirstChar(PDCharacter firstChar) throws PDStoreException {
		if (firstChar != null) {
			addFirstChar(firstChar.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "FirstChar".
	 * If the given collection of instances is null, nothing happens.
	 * @param firstChar the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addFirstChars(Collection<PDCharacter> firstChars) throws PDStoreException {
		if (firstChars == null)
			return;
		
		for (PDCharacter instance : firstChars)
			addFirstChar(instance);	
	}

	/**
	 * Removes the link from this instance through role "FirstChar".
	 * @throws PDStoreException
	 */
	public void removeFirstChar() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleFirstCharId, 
			pdWorkingCopy.getInstance(this, roleFirstCharId));
	}

	/**
	 * Removes the link from this instance through role "FirstChar" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeFirstChar(Object firstChar) throws PDStoreException {
		if (firstChar == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleFirstCharId, firstChar);
	}

	/**
	 * Removes the links from this instance through role "FirstChar" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeFirstChars(Collection<PDCharacter> firstChars) throws PDStoreException {
		if (firstChars == null)
			return;
		
		for (PDCharacter instance : firstChars)
			pdWorkingCopy.removeLink(this.id, roleFirstCharId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "FirstChar".
	 * If there is already an instance connected to this instance through role "FirstChar", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param firstChar the instance to connect
	 * @throws PDStoreException
	 */
	public void setFirstChar(GUID firstChar) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleFirstCharId, firstChar);	
	}
	/**
	 * Connects this instance to the given instance using role "FirstChar".
	 * If there is already an instance connected to this instance through role "FirstChar", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param firstChar the instance to connect
	 * @throws PDStoreException
	 */
	public void setFirstChar(PDCharacter firstChar) throws PDStoreException {
		setFirstChar(firstChar.getId());
	}



	/**
	 * Returns the instance connected to this instance through the role "LastChar".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDCharacter getLastChar() throws PDStoreException {
	 	return (PDCharacter)pdWorkingCopy.getInstance(this, roleLastCharId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "LastChar".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDCharacter> getLastChars() throws PDStoreException {
	 	Set<PDCharacter> result = new HashSet<PDCharacter>();
	 	GUID PDCharacterTypeId = new GUID("2e275f53965e11e18be2d8a25e8c53de");
		pdWorkingCopy.getInstances(this, roleLastCharId, PDCharacter.class, PDCharacterTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "LastChar".
	 * If the given instance is null, nothing happens.
	 * @param lastChar the instance to connect
	 * @throws PDStoreException
	 */
	public void addLastChar(GUID lastChar) throws PDStoreException {

			if (lastChar != null) {
				
				pdWorkingCopy.addLink(this.id, roleLastCharId, lastChar);
			}

	}


	/**
	 * Connects this instance to the given instance using role "LastChar".
	 * If the given instance is null, nothing happens.
	 * @param lastChar the instance to connect
	 * @throws PDStoreException
	 */
	public void addLastChar(PDCharacter lastChar) throws PDStoreException {
		if (lastChar != null) {
			addLastChar(lastChar.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "LastChar".
	 * If the given collection of instances is null, nothing happens.
	 * @param lastChar the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addLastChars(Collection<PDCharacter> lastChars) throws PDStoreException {
		if (lastChars == null)
			return;
		
		for (PDCharacter instance : lastChars)
			addLastChar(instance);	
	}

	/**
	 * Removes the link from this instance through role "LastChar".
	 * @throws PDStoreException
	 */
	public void removeLastChar() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleLastCharId, 
			pdWorkingCopy.getInstance(this, roleLastCharId));
	}

	/**
	 * Removes the link from this instance through role "LastChar" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeLastChar(Object lastChar) throws PDStoreException {
		if (lastChar == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleLastCharId, lastChar);
	}

	/**
	 * Removes the links from this instance through role "LastChar" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeLastChars(Collection<PDCharacter> lastChars) throws PDStoreException {
		if (lastChars == null)
			return;
		
		for (PDCharacter instance : lastChars)
			pdWorkingCopy.removeLink(this.id, roleLastCharId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "LastChar".
	 * If there is already an instance connected to this instance through role "LastChar", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param lastChar the instance to connect
	 * @throws PDStoreException
	 */
	public void setLastChar(GUID lastChar) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleLastCharId, lastChar);	
	}
	/**
	 * Connects this instance to the given instance using role "LastChar".
	 * If there is already an instance connected to this instance through role "LastChar", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param lastChar the instance to connect
	 * @throws PDStoreException
	 */
	public void setLastChar(PDCharacter lastChar) throws PDStoreException {
		setLastChar(lastChar.getId());
	}

}
