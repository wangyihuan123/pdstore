package pdstore.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pdstore.GUID;
import pdstore.PDStoreException;
import pdstore.notify.ListenerDispatcher;

public abstract class GenericPDStore<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>>
implements PDStoreI<TransactionType, InstanceType, RoleType> {

	protected PDStoreI<TransactionType, InstanceType, RoleType> store;
	private ListenerDispatcher<TransactionType, InstanceType, RoleType> viewDispatcher = new ListenerDispatcher<TransactionType, InstanceType, RoleType>();


	@Override
	public Collection<PDChange<TransactionType, InstanceType, RoleType>> getChanges(PDChange<TransactionType, InstanceType, RoleType> change) throws PDStoreException {
		ArrayList<PDChange<TransactionType, InstanceType, RoleType>> viewResult = new ArrayList<PDChange<TransactionType, InstanceType, RoleType>>();
		List<PDChange<TransactionType, InstanceType, RoleType>> matchedChanges = new ArrayList<PDChange<TransactionType, InstanceType, RoleType>>();
		matchedChanges.add(change);
		getViewDispatcher().transactionCommitted(viewResult, matchedChanges , store);
		if(!viewResult.isEmpty()) return viewResult;
		Collection<PDChange<TransactionType, InstanceType, RoleType>> changes = store.getChanges(change);
		
		ArrayList<Collection<PDChange<TransactionType, InstanceType, RoleType>>> iteratorlist = 
				new ArrayList<Collection<PDChange<TransactionType,InstanceType,RoleType>>>();
		iteratorlist.add(changes);
		iteratorlist.add(viewResult);
		return changes;
	}
	
	public ListenerDispatcher<TransactionType,InstanceType,RoleType> getViewDispatcher() {
		return viewDispatcher;
	}

}