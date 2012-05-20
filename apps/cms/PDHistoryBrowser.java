package cms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import pdstore.GUID;
import pdstore.PDStoreException;
import pdstore.dal.PDInstance;
import pdstore.dal.PDWorkingCopy;

import cms.dal.PDCMSOperation;
import cms.dal.PDDocument;
import cms.dal.PDDocumentOperation;
import cms.dal.PDHistory;

/**
 * Tree implementation of the history view.
 * 
 * The intention is that users can filter the history they see e.g by Document, User, Operation type etc.
 * History is scanned to build a history tree.
 * 
 * @author Sina Masoud-Ansari (s.ansari@auckland.ac.nz)
 *
 */
public class PDHistoryBrowser extends JTree {

	private static final long serialVersionUID = 1L;
	private PDWorkingCopy wc;
	private ContentManagementSystem cms;

	/**
	 * 	
	 * @param root the root of the tree
	 * @param wc the PDStore working copy
	 * @param cms the Content Management System this history view applies to.
	 */
	public PDHistoryBrowser(DefaultMutableTreeNode root, PDWorkingCopy wc, ContentManagementSystem cms){
		super(root);
		this.wc = wc;
		this.cms = cms;

	}

	/**
	 * Refreshes i.e rebuilds the history tree view given an operation list.
	 * 
	 * @param opHistory the list of Content Management System operations made by all collaborators.
	 */
	public void refreshTree(CMSOperationList opHistory){

		if (opHistory == null){
			return;
		}

		// Clean tree
		DefaultTreeModel m_model = (DefaultTreeModel) this.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) m_model.getRoot(); //searchNode("Root");
		root.removeAllChildren();

		// Iterate over opHistory and filter by criterion e.g User, Date, File, Operation, Document

		// Default Filter: Document - Sorted  by Time
		HashMap<String, ArrayList<PDCMSOperation>> docs = mapDocumentOpertions(opHistory);
		if (docs == null){
			return;
		}
		DefaultMutableTreeNode parent, child;
		for (String s : docs.keySet()){
			parent = new DefaultMutableTreeNode(s);
			root.add(parent);
			ArrayList<PDCMSOperation> oplist = docs.get(s);
			for (PDCMSOperation op: docs.get(s)) {			
				child = new DefaultMutableTreeNode(op.getDocumentOp().getName());
				parent.add(child);
			}
		}

		// Notify tree changed
		m_model.nodeStructureChanged(root);

	}

	/**
	 * Maps the operations in history to a HashMap for a given filter. In this case PDDocuments.
	 * The resulting map is used to build the tree structure in the history view.
	 * 
	 * @param opHistory the list of Content Management System operations made by all collaborators.
	 * @return a HashMap that represents the history of each Document.
	 */
	public HashMap<String, ArrayList<PDCMSOperation>> mapDocumentOpertions(CMSOperationList opHistory){

		Collection<PDInstance> instances = wc.getAllInstancesOfType(PDDocument.typeId);

		HashMap<String, ArrayList<PDCMSOperation>> map = new HashMap<String, ArrayList<PDCMSOperation>>();
		// Enter map keys
		PDDocument d;
		ArrayList<PDCMSOperation> ops;
		for (PDInstance i: instances){
			d = (PDDocument) i;
			String s = d.getDocumentFileName();
			ops = new ArrayList<PDCMSOperation>();

			// Enter key values
			//CMSOperationList opcopy = copyOperationList(opHistory);		
			synchronized (opHistory){
				try {
					for (int j = 0; j < opHistory.size(); j++){
						PDCMSOperation op = opHistory.get(j);
						if (op == null){
							return null;
						}
						if (op.getOpType().getId().equals(PDDocumentOperation.typeId)){
							PDDocumentOperation dop = op.getDocumentOp();
							if (dop == null){
								return null;
							}
							if (dop.getOpDocument().getDocumentFileName().equals(s)){
								ops.add(op);
							}
						}
					}
				} catch (Exception e) {
					return null;
				}
				/*	
				} catch (ClassCastException e) {
					mapDocumentOpertions(opHistory);
				} catch (PDStoreException e) {
					mapDocumentOpertions(opHistory);
				} catch (ArrayIndexOutOfBoundsException e) {
					mapDocumentOpertions(opHistory);
				} catch (ConcurrentModificationException e) {
					mapDocumentOpertions(opHistory);
				} finally {
					System.err.println("Exception thrown in PDHistoryBrowser.mapDocumentOperations");
				}
				 */
			}
			map.put(s, ops);
		}

		return map;
	}


	/** 
	 * This method takes the node string and traverses the tree till it finds the node 
	 * matching the string. If the match is found the node is returned else null is returned.
	 * 
	 * May be required later.
	 *  
	 * @param nodeStr node string to search for 
	 * @return tree node  
	 * @author Rahul Sapkal(rahul@javareference.com)
	 */ 
	public DefaultMutableTreeNode searchNode(String nodeStr) 
	{ 
		DefaultMutableTreeNode node = null; 

		//Get the enumeration 
		Enumeration en = ((DefaultMutableTreeNode)(this.getModel().getRoot())).breadthFirstEnumeration(); 

		//iterate through the enumeration 
		while(en.hasMoreElements()) 
		{ 
			//get the node 
			node = (DefaultMutableTreeNode)en.nextElement(); 

			//match the string with the user-object of the node 
			if(nodeStr.equals(node.getUserObject().toString())) 
			{ 
				//tree node with string found 
				return node;                          
			} 
		} 

		//tree node with string node found return null 
		return null; 
	} 	



}
