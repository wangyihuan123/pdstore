// Currently not used due to procedurally creating the relationships currently seeming to be the better idea

package gldebug;

import pdstore.*;
//import pdstore.dal.*; // Currently not used

public class CreateGLModel
{
	public final static GUID glStateModel = new GUID("27c594c0521011e186b450e5495bfcc0");
	
	public final static GUID sessionType = new GUID("6868b8207ed611e1855d50e5495bfcc0");
	public final static GUID stateType = new GUID("27c5bbd0521011e186b450e5495bfcc0"); // Represents a specialised state variable at the root of the state tree that doesn't actually hold any state data
	public final static GUID stateVariableType = new GUID("4a0a79c0876611e1a67950e5495bfcc0"); // Represents generalised state variables
	
	public final static GUID stateRole = new GUID("7f3857af7f2211e1a8c750e5495bfcc0");
	public final static GUID transactionIDRole = new GUID("f9eaf6a07e5911e189a950e5495bfcc0"); // TODO: document
	
	public final static GUID timeStampRole = new GUID("686954917ed611e1855d50e5495bfcc0");
	
	public final static GUID stateVariableValueRole = new GUID("5efd4c6087be11e1817150e5495bfcc0");
	public final static GUID stateVariableNonuniqueNameRole = new GUID("c88cb8a087c811e195ee50e5495bfcc0");
	public final static GUID childStateVariableRole = new GUID("4a0a79c1876611e1a67950e5495bfcc0");
	
	public static void main(String[] args)
	{
		PDStore store = new PDStore("MyGlStateDatabase");
		GUID transaction = store.begin();

		// create a new model
		store.createModel(transaction, glStateModel, "GLModel");

		// create the new complex types
		store.createType(transaction, glStateModel, stateType, "State");
		store.createType(transaction, glStateModel, stateVariableType, "StateVariable");
		store.createType(transaction, glStateModel, sessionType, "SessionData");
			
		store.createRelation(transaction, sessionType, null, "state", stateRole, stateType);
		store.createRelation(transaction, sessionType, "session", "transactionID", transactionIDRole, PDStore.TRANSACTION_TYPEID);
		
		store.createRelation(transaction, stateType, null, "timeStamp", timeStampRole, PDStore.TIMESTAMP_TYPEID);
		store.createRelation(transaction, stateType, "state", "stateVariable", childStateVariableRole, stateVariableType); // The state keeps the top level state variables
		
		store.createRelation(transaction, stateVariableType, null, "stateVariableValue", stateVariableValueRole, PDStore.STRING_TYPEID);
		store.createRelation(transaction, stateVariableType, null, "stateVariableNonuniqueName", stateVariableNonuniqueNameRole, PDStore.STRING_TYPEID);
		store.createRelation(transaction, stateVariableType, "parentStateVariable", "childStateVariable", childStateVariableRole, stateVariableType);
		
		store.commit(transaction);
	}
}
