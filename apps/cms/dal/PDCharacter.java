package cms.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Character" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("cms.dal.PDCharacter");
 * @author PDGen
 */
public class PDCharacter implements PDInstance {

	public static final GUID typeId = new GUID("88f390a2975a11e18f70d8a25e8c53de"); 

	public static final GUID roleCharValueId = new GUID("88f390af975a11e18f70d8a25e8c53de");
	public static final GUID roleNextCharId = new GUID("88f390b1975a11e18f70d8a25e8c53de");
	public static final GUID rolePrevCharId = new GUID("88f390b0975a11e18f70d8a25e8c53de");

	static {
		register();
	}
	
	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		DALClassRegister.addDataClass(typeId, PDCharacter.class);
	}
	
	private PDWorkingCopy pdWorkingCopy;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDCharacter:" + name;
		else
			return "PDCharacter:" + id;
	}
	
	/**
	 * Creates an PDCharacter object representing the given instance in the given cache.
	 * @param workingCopy the working copy the instance should be in
	 */
	public PDCharacter(PDWorkingCopy workingCopy) {
		this(workingCopy, new GUID());
	}
	
	/**
	 * Creates an PDCharacter object representing the given instance in the given copy.
	 * @param workingCopy the working copy the instance should be in
	 * @param id GUID of the instance
	 */
	public PDCharacter(PDWorkingCopy workingCopy, GUID id) {
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
	public static PDCharacter load(PDWorkingCopy pdWorkingCopy, GUID id) {
		PDInstance instance = pdWorkingCopy.load(typeId, id);
		return (PDCharacter)instance;
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
	 * Returns the instance connected to this instance through the role "CharValue".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public Character getCharValue() throws PDStoreException {
	 	return (Character)pdWorkingCopy.getInstance(this, roleCharValueId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "CharValue".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<Character> getCharValues() throws PDStoreException {
	 	Set<Character> result = new HashSet<Character>();
	 	GUID CharacterTypeId = new GUID("508a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleCharValueId, Character.class, CharacterTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "CharValue".
	 * If the given instance is null, nothing happens.
	 * @param charValue the instance to connect
	 * @throws PDStoreException
	 */
	public void addCharValue(Character charValue) throws PDStoreException {

			if (charValue != null) {
				
				pdWorkingCopy.addLink(this.id, roleCharValueId, charValue);
			}

	}

	/**
	 * Connects this instance to the given instances using role "CharValue".
	 * If the given collection of instances is null, nothing happens.
	 * @param charValue the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addCharValues(Collection<Character> charValues) throws PDStoreException {
		if (charValues == null)
			return;

		for (Character instance : charValues)
			addCharValue(instance);
	}


	/**
	 * Removes the link from this instance through role "CharValue".
	 * @throws PDStoreException
	 */
	public void removeCharValue() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleCharValueId, 
			pdWorkingCopy.getInstance(this, roleCharValueId));
	}

	/**
	 * Removes the link from this instance through role "CharValue" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeCharValue(Object charValue) throws PDStoreException {
		if (charValue == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleCharValueId, charValue);
	}


   /**
	 * Connects this instance to the given instance using role "CharValue".
	 * If there is already an instance connected to this instance through role "CharValue", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param charValue the instance to connect
	 * @throws PDStoreException
	 */
	public void setCharValue(Character charValue) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleCharValueId, charValue);	
	}


	/**
	 * Returns the instance connected to this instance through the role "NextChar".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDCharacter getNextChar() throws PDStoreException {
	 	return (PDCharacter)pdWorkingCopy.getInstance(this, roleNextCharId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "NextChar".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDCharacter> getNextChars() throws PDStoreException {
	 	Set<PDCharacter> result = new HashSet<PDCharacter>();
	 	GUID PDCharacterTypeId = new GUID("88f390a2975a11e18f70d8a25e8c53de");
		pdWorkingCopy.getInstances(this, roleNextCharId, PDCharacter.class, PDCharacterTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "NextChar".
	 * If the given instance is null, nothing happens.
	 * @param nextChar the instance to connect
	 * @throws PDStoreException
	 */
	public void addNextChar(GUID nextChar) throws PDStoreException {

			if (nextChar != null) {
				
				pdWorkingCopy.addLink(this.id, roleNextCharId, nextChar);
			}

	}


	/**
	 * Connects this instance to the given instance using role "NextChar".
	 * If the given instance is null, nothing happens.
	 * @param nextChar the instance to connect
	 * @throws PDStoreException
	 */
	public void addNextChar(PDCharacter nextChar) throws PDStoreException {
		if (nextChar != null) {
			addNextChar(nextChar.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "NextChar".
	 * If the given collection of instances is null, nothing happens.
	 * @param nextChar the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addNextChars(Collection<PDCharacter> nextChars) throws PDStoreException {
		if (nextChars == null)
			return;
		
		for (PDCharacter instance : nextChars)
			addNextChar(instance);	
	}

	/**
	 * Removes the link from this instance through role "NextChar".
	 * @throws PDStoreException
	 */
	public void removeNextChar() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleNextCharId, 
			pdWorkingCopy.getInstance(this, roleNextCharId));
	}

	/**
	 * Removes the link from this instance through role "NextChar" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeNextChar(Object nextChar) throws PDStoreException {
		if (nextChar == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleNextCharId, nextChar);
	}

	/**
	 * Removes the links from this instance through role "NextChar" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeNextChars(Collection<PDCharacter> nextChars) throws PDStoreException {
		if (nextChars == null)
			return;
		
		for (PDCharacter instance : nextChars)
			pdWorkingCopy.removeLink(this.id, roleNextCharId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "NextChar".
	 * If there is already an instance connected to this instance through role "NextChar", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param nextChar the instance to connect
	 * @throws PDStoreException
	 */
	public void setNextChar(GUID nextChar) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleNextCharId, nextChar);	
	}
	/**
	 * Connects this instance to the given instance using role "NextChar".
	 * If there is already an instance connected to this instance through role "NextChar", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param nextChar the instance to connect
	 * @throws PDStoreException
	 */
	public void setNextChar(PDCharacter nextChar) throws PDStoreException {
		setNextChar(nextChar.getId());
	}



	/**
	 * Returns the instance connected to this instance through the role "PrevChar".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDCharacter getPrevChar() throws PDStoreException {
	 	return (PDCharacter)pdWorkingCopy.getInstance(this, rolePrevCharId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "PrevChar".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDCharacter> getPrevChars() throws PDStoreException {
	 	Set<PDCharacter> result = new HashSet<PDCharacter>();
	 	GUID PDCharacterTypeId = new GUID("88f390a2975a11e18f70d8a25e8c53de");
		pdWorkingCopy.getInstances(this, rolePrevCharId, PDCharacter.class, PDCharacterTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "PrevChar".
	 * If the given instance is null, nothing happens.
	 * @param prevChar the instance to connect
	 * @throws PDStoreException
	 */
	public void addPrevChar(GUID prevChar) throws PDStoreException {

			if (prevChar != null) {
				
				pdWorkingCopy.addLink(this.id, rolePrevCharId, prevChar);
			}

	}


	/**
	 * Connects this instance to the given instance using role "PrevChar".
	 * If the given instance is null, nothing happens.
	 * @param prevChar the instance to connect
	 * @throws PDStoreException
	 */
	public void addPrevChar(PDCharacter prevChar) throws PDStoreException {
		if (prevChar != null) {
			addPrevChar(prevChar.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "PrevChar".
	 * If the given collection of instances is null, nothing happens.
	 * @param prevChar the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addPrevChars(Collection<PDCharacter> prevChars) throws PDStoreException {
		if (prevChars == null)
			return;
		
		for (PDCharacter instance : prevChars)
			addPrevChar(instance);	
	}

	/**
	 * Removes the link from this instance through role "PrevChar".
	 * @throws PDStoreException
	 */
	public void removePrevChar() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, rolePrevCharId, 
			pdWorkingCopy.getInstance(this, rolePrevCharId));
	}

	/**
	 * Removes the link from this instance through role "PrevChar" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removePrevChar(Object prevChar) throws PDStoreException {
		if (prevChar == null)
			return;
		pdWorkingCopy.removeLink(this.id, rolePrevCharId, prevChar);
	}

	/**
	 * Removes the links from this instance through role "PrevChar" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removePrevChars(Collection<PDCharacter> prevChars) throws PDStoreException {
		if (prevChars == null)
			return;
		
		for (PDCharacter instance : prevChars)
			pdWorkingCopy.removeLink(this.id, rolePrevCharId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "PrevChar".
	 * If there is already an instance connected to this instance through role "PrevChar", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param prevChar the instance to connect
	 * @throws PDStoreException
	 */
	public void setPrevChar(GUID prevChar) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  rolePrevCharId, prevChar);	
	}
	/**
	 * Connects this instance to the given instance using role "PrevChar".
	 * If there is already an instance connected to this instance through role "PrevChar", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param prevChar the instance to connect
	 * @throws PDStoreException
	 */
	public void setPrevChar(PDCharacter prevChar) throws PDStoreException {
		setPrevChar(prevChar.getId());
	}

}
