package pdstore.notify;

import java.util.Collection;
import java.util.List;

import pdstore.GUID;
import pdstore.generic.PDChange;
import pdstore.generic.PDCoreI;
import pdstore.generic.Pairable;

/**
 * This is the interface used for the Listeners as well as for the Interceptors.
 * More detailed comments on the mechanisms can be found in the comments to the
 * methods getDetachedListenerList() and getInterceptorList()
 * 
 * 
 * 
 * @author gweb017
 * 
 * @param <TransactionType>
 * @param <InstanceType>
 * @param <RoleType>
 */
public interface PDListener<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>> {

	void transactionCommitted(
			List<PDChange<TransactionType, InstanceType, RoleType>> transaction,
			List<PDChange<TransactionType, InstanceType, RoleType>> matchedChanges,
			PDCoreI<TransactionType, InstanceType, RoleType> core);


	/**
	 * The collection of matchingtemplates that this Listener is registered against.
	 * 
	 * 
	 * @return
	 */
	Collection<PDChange<TransactionType, InstanceType, RoleType>> getMatchingTemplates();
}



   
