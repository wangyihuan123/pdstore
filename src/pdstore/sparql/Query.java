package pdstore.sparql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import nz.ac.auckland.se.genoupe.tools.Debug;
import nz.ac.auckland.se.genoupe.tools.FilterIterator;

import pdstore.GUID;

import pdstore.generic.PDChange;

import pdstore.generic.PDStoreI;

public class Query {
	
	private PDStoreI<GUID, Object, GUID> store;
	List<Variable> select;
	List<PDChange<GUID, Object, GUID>> where;
	FilterExpression filter;
	List<Query> optionals;

	private Iterator<List<PDChange<GUID, Object, GUID>>> cartesianIterator;

	/**
	 * 
	 * Standard semantics of query execution is:
	 * 
	 * 1. equijoin, natural join Result: Net of Links Possible further
	 * decomposition: 1.1. Cartesian product 1.2. Exclude tuples not joining
	 * 
	 * 2. step: apply filters (in relational algebra: selection)
	 * 
	 * 3. step projection means apply the select clause.
	 * 
	 * select ?name where ?person gender "male" ?person hasChild "Pat" ?person
	 * lastName ?name
	 * 
	 * 
	 * select ?person where ?person gender "male" ?person hasChild "Pat"
	 * 
	 * @param select
	 * @param where
	 * @param store TODO
	 */
	public Query(List<Variable> select,
			List<PDChange<GUID, Object, GUID>> where, FilterExpression filter,
			List<Query> optionals, PDStoreI<GUID, Object, GUID> store) {
		if (select == null)
			this.select = new ArrayList<Variable>();
		else
			this.select = select;
		this.where = where;
		this.filter = filter;
		this.optionals = optionals;
		this.store = store;
	}

	public Query(List<Variable> select,
			List<PDChange<GUID, Object, GUID>> where, FilterExpression filter, PDStoreI<GUID, Object, GUID> store) {
		this(select, where, filter, new ArrayList<Query>(),store);
	}

	public Query(List<Variable> select, List<PDChange<GUID, Object, GUID>> where, PDStoreI<GUID, Object, GUID> store) {
		this(select, where, null,store);
	}

	/**
	 * Creates a query object from a SPARQL query string.
	 * 
	 * @param queryString
	 */
	public Query(String queryString) {
		// TODO: not implemented yet
		throw new UnsupportedOperationException();
	}

	/**
	 * Gives the SPARQL query as a formatted String.
	 */
	@Override
	public String toString() {
		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT" + " ");
		if (select.size() == 0)
			queryString.append("*");
		else {
			for (Variable variable : select) {
				queryString.append(variable.toString() + " ");
			}
		}
		queryString.append("\r\n");
		queryString.append("WHERE" + "\r\n");
		queryString.append("{" + "\r\n");
		for (PDChange<GUID, Object, GUID> change : where) {

			queryString.append(change.getChangeType() + " "
					+ change.getTransaction().toString() + " "
					+ change.getInstance1().toString() + " "
					+ change.getRole2().toString() + " "
					+ change.getInstance2().toString() + " . ");

		}
		if (optionals.size() != 0) {
			queryString.append("\r\n");
			queryString.append("OPTIONAL { ");
			for (Query optional : optionals) {
				for (PDChange<GUID, Object, GUID> change : optional.where) {
					queryString.append(change.getChangeType() + " "
							+ change.getTransaction().toString() + " "
							+ change.getInstance1().toString() + " "
							+ change.getRole2().toString() + " "
							+ change.getInstance2().toString() + " . ");
				}
			}
			queryString.append("}" + "\r\n");
		}
		if (filter != null) {
			queryString.append("\r\n");
			queryString.append("FILTER  " + filter.toString());
		}
		queryString.append("\r\n" + "}");
		return queryString.toString();
	}

	/**
	 * Executes a SPARQL query on PDStore.
	 * @param transactionId the transaction on which to execute the query 
	 * @return an iterator over the matching variable assignments 
	 */
	public Iterator<Map<Variable, Object>> execute(
			GUID transactionId) {
		Debug.println("Executing query...", "SPARQL");

		cartesianIterator = new CartesianIndexIterator<GUID, Object, GUID>(store, transactionId, where, this.filter, null);

		// if debugging is active pretty-print the combinations
		if (Debug.isDebugging("SPARQL")) {
			FilterIterator<List<PDChange<GUID, Object, GUID>>> debugIterator1 = new FilterIterator<List<PDChange<GUID, Object, GUID>>>(
					cartesianIterator) {
				public boolean filterCondition(
						List<PDChange<GUID, Object, GUID>> combination) {
					Debug.println(combination.toString(), "SPARQL");
					return true;
				}
			};
			cartesianIterator = debugIterator1;
		}

		// TODO: getChanges should do the filtering that the MatchIterator does,
		// so the MatchIterator can be removed
		MatchIterator matchIterator = new MatchIterator(cartesianIterator,this);

		// If there is no filter expression in the query, the pipeline of
		// Iterators ends here.
		if (filter == null)
			return matchIterator;

		// Otherwise, use the filter expression in a FilterIterator
		FilterIterator<Map<Variable, Object>> filterIterator = new FilterIterator<Map<Variable, Object>>(
				matchIterator) {
			public boolean filterCondition(Map<Variable, Object> element) {
				return filter.evaluate(element);
			}
		};

		// TODO: process optional clause, i.e. enrich data with optional values

		return filterIterator;
	}

	@SuppressWarnings("unchecked")
	public String getStatistics() {
		return ((CartesianIndexIterator<GUID, Object, GUID>) cartesianIterator)
				.getStatistics();
	}
}
