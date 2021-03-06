package pdtransform.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Serializer Application" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdtransform.dal.PDSerializerApplication");
 * @author PDGen
 */
public class PDSerializerApplication implements PDInstance {

	public static final GUID typeId = new GUID("a3ba85348e0ede11980f9a097666e103"); 

	public static final GUID roleSerializerId = new GUID("abba85348e0ede11980f9a097666e103");
	public static final GUID roleInputTypeId = new GUID("a7ba85348e0ede11980f9a097666e103");
	public static final GUID roleOrderId = new GUID("bdba85348e0ede11980f9a097666e103");
	public static final GUID roleInputId = new GUID("a5ba85348e0ede11980f9a097666e103");
	public static final GUID roleOutputId = new GUID("a9ba85348e0ede11980f9a097666e103");

	static {
		DALClassRegister.addDataClass(typeId, PDSerializerApplication.class);
	}
	private PDWorkingCopy pdWorkingCopy;
	private GUID id;
	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDSerializerApplication:" + name;
		else
			return "PDSerializerApplication:" + id;
	}
	/**
	 * Creates an PDSerializerApplication object representing the given instance in the given cache.
	 * @param workingCopy the working copy the instance should be in
	 */
	public PDSerializerApplication(PDWorkingCopy workingCopy) {
		this(workingCopy, new GUID());
	}
	
	/**
	 * Creates an PDSerializerApplication object representing the given instance in the given cache.
	 * @param workingCopy the working copy the instance should be in
	 * @param id GUID of the instance
	 */
	public PDSerializerApplication(PDWorkingCopy workingCopy, GUID id) {
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
	public static PDSerializerApplication load(PDWorkingCopy pdWorkingCopy, GUID id) {
		PDInstance instance = pdWorkingCopy.load(typeId, id);
		return (PDSerializerApplication)instance;
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
	 * Returns the instance connected to this instance through the role "serializer".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDSerializer getSerializer() throws PDStoreException {
	 	return (PDSerializer)pdWorkingCopy.getInstance(this, roleSerializerId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "serializer".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDSerializer> getSerializers() throws PDStoreException {
	 	Set<PDSerializer> result = new HashSet<PDSerializer>();
	 	GUID PDSerializerTypeId = new GUID("5f1e85348e0ede11980f9a097666e103");
		pdWorkingCopy.getInstances(this, roleSerializerId, PDSerializer.class, PDSerializerTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "serializer".
	 * If the given instance is null, nothing happens.
	 * @param serializer the instance to connect
	 * @throws PDStoreException
	 */
	public void addSerializer(GUID serializer) throws PDStoreException {

			if (serializer != null) {
				
				pdWorkingCopy.addLink(this.id, roleSerializerId, serializer);
			}

	}

	/**
	 * Connects this instance to the given instance using role "serializer".
	 * If the given instance is null, nothing happens.
	 * @param serializer the instance to connect
	 * @throws PDStoreException
	 */
	public void addSerializer(PDSerializer serializer) throws PDStoreException {
		if (serializer != null) {
			addSerializer(serializer.getId());
		}		
	}

	/**
	 * Removes the link from this instance through role "serializer".
	 * @throws PDStoreException
	 */
	public void removeSerializer() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleSerializerId, 
			pdWorkingCopy.getInstance(this, roleSerializerId));
	}

	/**
	 * Removes the link from this instance through role "serializer" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.serializer.getId()
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeSerializer(Object serializer) throws PDStoreException {
		if (serializer==null)
			return;
		pdWorkingCopy.removeLink(this.id, roleSerializerId, serializer);
	}

   /**
	 * Connects this instance to the given instance using role "serializer".
	 * If there is already an instance connected to this instance through role "serializer", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param serializer the instance to connect
	 * @throws PDStoreException
	 */
	public void setSerializer(GUID serializer) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleSerializerId, serializer);	
	}
	/**
	 * Connects this instance to the given instance using role "serializer".
	 * If there is already an instance connected to this instance through role "serializer", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param serializer the instance to connect
	 * @throws PDStoreException
	 */
	public void setSerializer(PDSerializer serializer) throws PDStoreException {
		setSerializer(serializer.getId());
	}

  // Code generated by PDGetSetRemoveGen:


	/**
	 * Returns the instance connected to this instance through the role "input type".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDType getInputType() throws PDStoreException {
	 	return (PDType)pdWorkingCopy.getInstance(this, roleInputTypeId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "input type".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDType> getInputTypes() throws PDStoreException {
	 	Set<PDType> result = new HashSet<PDType>();
	 	GUID PDTypeTypeId = new GUID("518a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleInputTypeId, PDType.class, PDTypeTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "input type".
	 * If the given instance is null, nothing happens.
	 * @param inputType the instance to connect
	 * @throws PDStoreException
	 */
	public void addInputType(GUID inputType) throws PDStoreException {

			if (inputType != null) {
				
				pdWorkingCopy.addLink(this.id, roleInputTypeId, inputType);
			}

	}

	/**
	 * Connects this instance to the given instance using role "input type".
	 * If the given instance is null, nothing happens.
	 * @param inputType the instance to connect
	 * @throws PDStoreException
	 */
	public void addInputType(PDType inputType) throws PDStoreException {
		if (inputType != null) {
			addInputType(inputType.getId());
		}		
	}

	/**
	 * Removes the link from this instance through role "input type".
	 * @throws PDStoreException
	 */
	public void removeInputType() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleInputTypeId, 
			pdWorkingCopy.getInstance(this, roleInputTypeId));
	}

	/**
	 * Removes the link from this instance through role "input type" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.inputType.getId()
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeInputType(Object inputType) throws PDStoreException {
		if (inputType==null)
			return;
		pdWorkingCopy.removeLink(this.id, roleInputTypeId, inputType);
	}

   /**
	 * Connects this instance to the given instance using role "input type".
	 * If there is already an instance connected to this instance through role "input type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param inputType the instance to connect
	 * @throws PDStoreException
	 */
	public void setInputType(GUID inputType) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleInputTypeId, inputType);	
	}
	/**
	 * Connects this instance to the given instance using role "input type".
	 * If there is already an instance connected to this instance through role "input type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param inputType the instance to connect
	 * @throws PDStoreException
	 */
	public void setInputType(PDType inputType) throws PDStoreException {
		setInputType(inputType.getId());
	}

  // Code generated by PDGetSetRemoveGen:


	/**
	 * Returns the instance connected to this instance through the role "order".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDOrder getOrder() throws PDStoreException {
	 	return (PDOrder)pdWorkingCopy.getInstance(this, roleOrderId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "order".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDOrder> getOrders() throws PDStoreException {
	 	Set<PDOrder> result = new HashSet<PDOrder>();
	 	GUID PDOrderTypeId = new GUID("b2ba85348e0ede11980f9a097666e103");
		pdWorkingCopy.getInstances(this, roleOrderId, PDOrder.class, PDOrderTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "order".
	 * If the given instance is null, nothing happens.
	 * @param order the instance to connect
	 * @throws PDStoreException
	 */
	public void addOrder(GUID order) throws PDStoreException {

			if (order != null) {
				
				pdWorkingCopy.addLink(this.id, roleOrderId, order);
			}

	}

	/**
	 * Connects this instance to the given instance using role "order".
	 * If the given instance is null, nothing happens.
	 * @param order the instance to connect
	 * @throws PDStoreException
	 */
	public void addOrder(PDOrder order) throws PDStoreException {
		if (order != null) {
			addOrder(order.getId());
		}		
	}

	/**
	 * Removes the link from this instance through role "order".
	 * @throws PDStoreException
	 */
	public void removeOrder() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleOrderId, 
			pdWorkingCopy.getInstance(this, roleOrderId));
	}

	/**
	 * Removes the link from this instance through role "order" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.order.getId()
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOrder(Object order) throws PDStoreException {
		if (order==null)
			return;
		pdWorkingCopy.removeLink(this.id, roleOrderId, order);
	}

   /**
	 * Connects this instance to the given instance using role "order".
	 * If there is already an instance connected to this instance through role "order", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param order the instance to connect
	 * @throws PDStoreException
	 */
	public void setOrder(GUID order) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleOrderId, order);	
	}
	/**
	 * Connects this instance to the given instance using role "order".
	 * If there is already an instance connected to this instance through role "order", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param order the instance to connect
	 * @throws PDStoreException
	 */
	public void setOrder(PDOrder order) throws PDStoreException {
		setOrder(order.getId());
	}

  // Code generated by PDGetSetRemoveGen:


	/**
	 * Returns the instance connected to this instance through the role "input".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public GUID getInput() throws PDStoreException {
	 	return (GUID)pdWorkingCopy.getInstance(this, roleInputId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "input".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<GUID> getInputs() throws PDStoreException {
	 	Set<GUID> result = new HashSet<GUID>();
	 	GUID GUIDTypeId = new GUID("538a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleInputId, GUID.class, GUIDTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "input".
	 * If the given instance is null, nothing happens.
	 * @param input the instance to connect
	 * @throws PDStoreException
	 */
	public void addInput(GUID input) throws PDStoreException {

			if (input != null) {
				
				pdWorkingCopy.addLink(this.id, roleInputId, input);
			}

	}


	/**
	 * Removes the link from this instance through role "input".
	 * @throws PDStoreException
	 */
	public void removeInput() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleInputId, 
			pdWorkingCopy.getInstance(this, roleInputId));
	}

	/**
	 * Removes the link from this instance through role "input" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.input.getId()
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeInput(Object input) throws PDStoreException {
		if (input==null)
			return;
		pdWorkingCopy.removeLink(this.id, roleInputId, input);
	}

   /**
	 * Connects this instance to the given instance using role "input".
	 * If there is already an instance connected to this instance through role "input", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param input the instance to connect
	 * @throws PDStoreException
	 */
	public void setInput(GUID input) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleInputId, input);	
	}
  // Code generated by PDGetSetRemoveGen:


	/**
	 * Returns the instance connected to this instance through the role "output".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public String getOutput() throws PDStoreException {
	 	return (String)pdWorkingCopy.getInstance(this, roleOutputId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "output".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<String> getOutputs() throws PDStoreException {
	 	Set<String> result = new HashSet<String>();
	 	GUID StringTypeId = new GUID("4a8a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleOutputId, String.class, StringTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "output".
	 * If the given instance is null, nothing happens.
	 * @param output the instance to connect
	 * @throws PDStoreException
	 */
	public void addOutput(String output) throws PDStoreException {

			if (output != null) {
				
				pdWorkingCopy.addLink(this.id, roleOutputId, output);
			}

	}


	/**
	 * Removes the link from this instance through role "output".
	 * @throws PDStoreException
	 */
	public void removeOutput() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleOutputId, 
			pdWorkingCopy.getInstance(this, roleOutputId));
	}

	/**
	 * Removes the link from this instance through role "output" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.output.getId()
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOutput(Object output) throws PDStoreException {
		if (output==null)
			return;
		pdWorkingCopy.removeLink(this.id, roleOutputId, output);
	}

   /**
	 * Connects this instance to the given instance using role "output".
	 * If there is already an instance connected to this instance through role "output", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param output the instance to connect
	 * @throws PDStoreException
	 */
	public void setOutput(String output) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleOutputId, output);	
	}
}
