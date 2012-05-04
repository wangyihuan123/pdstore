package gldebug;

import gldebug.DebuggerProtocol.TimeStamp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import java.util.Collection;
import java.util.Enumeration;
import java.util.regex.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import pdstore.*;
import pdstore.generic.PDChange;

public class GLDebug {
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
	
	/* PDStore related varibales */
	private static PDStore mainStore;
	public PDStore store;
	protected GUID historyID;
	protected String sessionName;
	
	/* Debugger vars */
	// Store collections of these in the future, me thinks, so we can have multiple connections at any one time
	Socket clientSocket;
	DebuggerProtocol debugProtocol;
	DebuggerListenerThread debugListenerThread;
	int seq;
	
	
	/* GUI variables */
	EventHandler eventHandler;
	JFrame windowFrame;
	JPanel debuggerMessagePane, debuggerTopLevelPane, debuggerButtonsPane, singleViewTopLevelPane, singleViewComboBoxPane, singleViewTreeDataPane, singleViewVariabelViewPane;
	JScrollPane debuggerMessageScrollPane, singleViewTreeScrollPane, singlewViewValueArea1ScrollPane, singlewViewValueArea2ScrollPane;
	JSplitPane singleViewValueSplitPane;
	JTabbedPane topLevelTabbedPane;
	JButton debuggerRunButton, debuggerStopButton, debuggerContinueButton, debuggerRequestStateButton, debuggerKillButton, debuggerConnectButton;
	JTextField debuggerIPField, debuggerPortField, singleViewFilterField;
	JTextArea debuggerMessageArea, singleViewValueArea1, singleViewValueArea2;
	JComboBox singleViewSessionComboBox;
	JComboBox singleViewStateNumberComboBox;
	StateJTree singleViewStateTree;
	JList singleViewStateValueList;
	
	//GLTableModel glTableModel;
	//SessionComboBoxModel sessionComboBoxModel;
	//StateNumberComboBoxModel stateNumberComboBoxModel;
	
	FlowLayout debuggerButtonsLayout, singleViewComboBoxLayout;
	BorderLayout debuggerMessageLayout, singleViewTreeDataLayout, singleViewVariableViewLayout; // The treeData layout is used for the tree and data panel, while the variable view layout is a top level layout to make sure spacing is correct
	BoxLayout debuggerTopLevelLayout, singleViewTopLevelLayout;	

	//private Vector<Vector<Object>> currentTable;
	private String selectedSessionName; // The currently selected session name the table will show data for
	private Integer selectedStateNumber;  // The currently selected state from within the states the session has
	private String filterString;
	
	private boolean ignoreActionEvents;
	
	protected void updateDatabaseState(State stateRoot, String sessionName)
	{
		GUID transaction = store.begin();
		
		GUID sessionGUID;
		GUID stateGUID;
			
		if((sessionGUID = store.getId(transaction, sessionName)) == null)
		{ // If we don't have this session in our database yet we create an entry
			sessionGUID = new GUID();
			// Setup the new session
			store.setType(transaction, sessionGUID, sessionType);
			
			store.setName(transaction, sessionGUID, sessionName);
				
			// Setup a new state for the session
			stateGUID = new GUID();	//Create a new state for the incoming state
			
			store.setType(transaction, stateGUID, stateType);
			
			store.setLink(transaction, sessionGUID, stateRole, stateGUID);
			
			store.setName(transaction, stateGUID, sessionName + "-State");
		}
		else
		{ // Otherwise we get the state the session already has
			stateGUID = (GUID)store.getInstance(transaction, sessionGUID, stateRole);
		}
				
		recusiveAddStateToDatabase(stateRoot, stateGUID, sessionName + "-State", transaction);
		
		GUID transactionID = store.commit(transaction);
		
		transaction = store.begin();
		
		store.addLink(transaction, sessionGUID, transactionIDRole, transactionID);
		
		store.commit(transaction);
		
		refreshSessionComboBox(); // incase we have a new session
		refreshStateNumberComboBox(); // Incase we have a new state for the current session
		//glTableModel.fireTableDataChanged(); // Don't think this is needed anymore
	}
	
	protected void recusiveAddStateToDatabase(State state, GUID stateVariableGUID, String concatStateVariableName, GUID transaction)
	{
		String currentStateVariableName = concatStateVariableName + "-" + state.name;
		
		if(!state.data.equals(store.getInstance(transaction, stateVariableGUID, stateVariableValueRole)))
		{ // If data has changed
			store.setLink(transaction, stateVariableGUID, stateVariableValueRole, state.data);
		}
		
		for(State s : state.children)
		{
			GUID childStateVariableGUID;
			
			if((childStateVariableGUID = (GUID)store.getId(transaction, currentStateVariableName + "-" + s.name)) == null)
			{ // Child's role doesn't exist yet
				// Create, name, and link the child stateVariable
				childStateVariableGUID = new GUID();
				store.setType(transaction, childStateVariableGUID, stateVariableType);
				store.setName(transaction, childStateVariableGUID, currentStateVariableName + "-" + s.name);
				store.addLink(transaction, stateVariableGUID, childStateVariableRole, childStateVariableGUID);
				store.addLink(transaction, childStateVariableGUID, stateVariableNonuniqueNameRole, s.name);
			}
			
			recusiveAddStateToDatabase(s, childStateVariableGUID, currentStateVariableName, transaction);
		}
	}
	
	private void refreshSessionComboBox()
	{
		String previousSessionName = (String)singleViewSessionComboBox.getSelectedItem();
		
		ignoreActionEvents = true;
		
		singleViewSessionComboBox.removeAllItems();
		
		GUID[] sessionGUIDs;
		
		GUID transaction = store.begin();
		
		sessionGUIDs = store.getAllInstancesOfType(transaction, sessionType).toArray(new GUID[0]); // Get the session data of the correct index
		
		int i = 0;
		int selectedIndex = -1;
		String sessionName;
		for(GUID guid: sessionGUIDs)
		{
			sessionName = (String)store.getName(transaction, guid);
			singleViewSessionComboBox.addItem(sessionName);
			
			if(sessionName.equals(previousSessionName))
				selectedIndex = i;
			
			++i;
		}
		
		store.commit(transaction);
		
		ignoreActionEvents = false;
		
		singleViewSessionComboBox.setSelectedIndex(selectedIndex);
		
	}
	
	private void refreshStateNumberComboBox()
	{
		Integer previousSelection = singleViewStateNumberComboBox.getSelectedIndex();
		
		ignoreActionEvents = true;
		
		singleViewStateNumberComboBox.removeAllItems();
		
		if(selectedSessionName == null || selectedSessionName.equals(""))
		{
			ignoreActionEvents = false;
			return;
		}
		
		GUID transaction = store.begin();
		
		int size =
				store.getInstances(transaction,
					store.getId(transaction, selectedSessionName),
					transactionIDRole
				).size();
		
		store.commit(transaction);
		
		for(int i = 0; i < size; ++i)
		{
			singleViewStateNumberComboBox.addItem(i);
		}
		
		ignoreActionEvents = false;
		
		if(previousSelection < singleViewStateNumberComboBox.getItemCount())
			singleViewStateNumberComboBox.setSelectedIndex(previousSelection);
	}
	
	protected class StateJTree extends JTree
	{
		public StateJTree()
		{
			//Set a null root to avoid the silly Swing defaults
			super(new DefaultTreeModel(null));
		}
		
		public String convertValueToText(Object value,
                boolean selected,
                boolean expanded,
                boolean leaf,
                int row,
                boolean hasFocus)
		{
			GUID objectGUID;
			DefaultMutableTreeNode test;
			
			try
			{
				test = (DefaultMutableTreeNode)value;
				objectGUID = (GUID)test.getUserObject();
			}
			catch(ClassCastException e)
			{
				System.err.println("Error casting while printing tree data");
				return "Error";
			}
			
			GUID transaction = store.begin();
			
			String returnString = (String)store.getInstance(transaction, objectGUID, stateVariableNonuniqueNameRole);
			
			// DEBUG CODE TODO: remove
			//returnString = returnString + ": " + (String)store.getInstance(transaction, objectGUID, stateVariableValueRole); 
			//if(returnString.length() > 50)
			//	returnString = returnString.substring(0, 50);
			// End DEBUG CODE
			
			store.commit(transaction);
			
			return returnString;
		}
	}
	
	// Creates the root of the tree and recursively creates the child nodes
	protected MutableTreeNode createTreeRoot(String sessionName, int stateNum)
	{
		GUID transaction = store.begin();
		
		GUID sessionGUID = store.getId(transaction, sessionName);
		
		GUID[] transactionGUIDs = store.getInstances(transaction, sessionGUID, transactionIDRole).toArray(new GUID[0]);
		
		store.commit(transaction);
		
		GUID stateGUID = (GUID)store.getInstance(transactionGUIDs[stateNum], sessionGUID, stateRole);
		
		DefaultMutableTreeNode root = recursivelyCreateTreeNodes(stateGUID, transactionGUIDs[stateNum]);
			
		
		
		return root;
	}
	
	protected DefaultMutableTreeNode recursivelyCreateTreeNodes(GUID stateVariableGUID, GUID transaction)
	{
		DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(stateVariableGUID);
		
		Collection<GUID> childStateVariables = (Collection<GUID>)(Object)store.getInstances(transaction, stateVariableGUID, childStateVariableRole);
		
		for(GUID guid: childStateVariables)
		{
			currentNode.add(recursivelyCreateTreeNodes(guid, transaction));
		}
		
		return currentNode;
	}

	protected void refreshVariableStateTree()
	{
		if(selectedSessionName == null || selectedStateNumber == null || selectedStateNumber == -1)
		{
			((DefaultTreeModel)(singleViewStateTree.getModel())).setRoot(null);
			return;
		}
			
		((DefaultTreeModel)(singleViewStateTree.getModel())).setRoot(createTreeRoot(selectedSessionName, selectedStateNumber));
		
		refreshStateVariableValueList();
	}
	
	protected void recursivelyFillStateVariableValueList(DefaultMutableTreeNode treeNode, DefaultListModel listModel, GUID transaction)
	{
		final int maxValueLength = 70;
		String variableValue;
		
		variableValue = (String)store.getInstance(transaction, treeNode.getUserObject(), stateVariableValueRole);
		if(variableValue == null || variableValue.equals(""))
		{
			listModel.addElement("<Empty>"); // Need to add a string as the list culls empty ones TODO: maybe alter in future
		}
		else
		{
			if(variableValue.length() > maxValueLength)
			{
				variableValue = variableValue.substring(0, maxValueLength);
				variableValue += "...";
			}
			listModel.addElement(variableValue);
		}
		
		
		// If this node is not expanded we cannot see and do not deal with its children
		if(!singleViewStateTree.isExpanded(new TreePath(treeNode.getPath())))
			return;
		
		Enumeration<DefaultMutableTreeNode> children = treeNode.children();
		
		while(children.hasMoreElements())
		{
			recursivelyFillStateVariableValueList(children.nextElement(), listModel, transaction);
		}
	}
	
	protected void refreshStateVariableValueList()
	{
		DefaultListModel newModel = new DefaultListModel();
		singleViewStateValueList.setModel(newModel);
		
		GUID transaction = store.begin();
		
		recursivelyFillStateVariableValueList((DefaultMutableTreeNode)singleViewStateTree.getModel().getRoot(), newModel, transaction);
		
		store.commit(transaction);
	}
	
	protected void refreshValueAreaText()
	{
		String variableValue;
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)singleViewStateTree.getLastSelectedPathComponent();

		if (treeNode == null)
			return;
		
		GUID transaction = store.begin();
		
		variableValue = (String)store.getInstance(transaction, treeNode.getUserObject(), stateVariableValueRole);
		if(variableValue == null || variableValue.equals(""))
		{
			singleViewValueArea1.setText("<Empty>");
		}
		else
		{
			singleViewValueArea1.setText(variableValue);
		}
		
		store.commit(transaction);
	}
	
	private String getStateName(GUID transaction, GUID stateVariableGUID)
	{
		if(transaction == null || stateVariableGUID == null)
			return null;
		
		String nameString = "";
		
		nameString += store.getName(transaction, stateVariableGUID);
		
		String parentName = getStateParentName(transaction, stateVariableGUID);
		
		if(parentName != null)
			nameString = parentName + nameString;
		
		return nameString;
	}
	
	// Does not check to see if parent exists
	private String getStateParentName(GUID transaction, GUID stateVariableGUID)
	{
		if(store.getInstance(transaction, stateVariableGUID, childStateVariableRole.getPartner()) != null)
			return getStateName(transaction, (GUID)store.getInstance(transaction, stateVariableGUID, childStateVariableRole.getPartner()));
		
		return null;
	}
	
	protected class EventHandler implements ActionListener, TreeExpansionListener, TreeSelectionListener, DebuggerProtocol.StateTreeRecievedListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(ignoreActionEvents)
				return;
			
			if(event.getSource() == debuggerRunButton)
			{
				if(debugProtocol != null)
				{
					debuggerMessageArea.append("Sending run request\n");
					debugProtocol.sendRun(seq++);
				}
				else
				{
					debuggerMessageArea.append("Error, debugger not connected\n");
				}
			}
			else if(event.getSource() == debuggerStopButton)
			{
				if(debugProtocol != null)
				{
					debuggerMessageArea.append("Sending stop request\n");
					debugProtocol.sendAsync(seq++);
				}
				else
				{
					debuggerMessageArea.append("Error, debugger not connected\n");
				}
			}
			else if(event.getSource() == debuggerContinueButton)
			{
				if(debugProtocol != null)
				{
					debuggerMessageArea.append("Sending continue request\n");
					debugProtocol.sendContinue(seq++);
				}
				else
				{
					debuggerMessageArea.append("Error, debugger not connected\n");
				}
			}
			else if(event.getSource() == debuggerRequestStateButton)
			{
				if(debugProtocol != null)
				{
					debuggerMessageArea.append("Sending state request\n");
					debugProtocol.sendStateTree(seq++);
				}
				else
				{
					debuggerMessageArea.append("Error, debugger not connected\n");
				}
			}
			else if(event.getSource() == debuggerKillButton)
			{
				if(debugProtocol != null)
				{
					debuggerMessageArea.append("Sending kill request\n");
					debugProtocol.sendQuit(seq++);
					try
					{
						debugListenerThread.cease(); // Doesn't work at the mo
						clientSocket.close();
					}
					catch(IOException e)
					{
						debuggerMessageArea.append("Wanring, couldn't close client socket\n");
					}
					debugProtocol = null;
					debugListenerThread = null;
					clientSocket = null;
					seq = 0;
				}
				else
				{
					debuggerMessageArea.append("Error, debugger not connected\n");
				}
			}
			else if(event.getSource() == debuggerConnectButton)
			{
				debuggerMessageArea.append("Connecting to " + debuggerIPField.getText() + ":" + debuggerPortField.getText() + "\n");
				try
				{
					clientSocket = new Socket(InetAddress.getByName(debuggerIPField.getText()), Integer.parseInt(debuggerPortField.getText()));
					debugProtocol = new DebuggerProtocol(clientSocket);
					debugProtocol.addStateTreeRecievedListener(eventHandler);
					debugListenerThread = new DebuggerListenerThread(debugProtocol);
					debugListenerThread.start();
					debuggerMessageArea.append("Connected!\n");
				}
				catch(NumberFormatException e)
				{
					debuggerMessageArea.append("Couldn't parse that port as a number, you should probably make it a number\n");
				}
				catch(IOException e)
				{
					debuggerMessageArea.append("Error, could not connect to host\n");
				}
			}
			else if(event.getSource() == singleViewSessionComboBox)
			{
				if(singleViewSessionComboBox.getSelectedItem() == null)
				{
					selectedSessionName = null;
				}
				else
				{
					selectedSessionName = singleViewSessionComboBox.getSelectedItem().toString();
				}
				
				refreshStateNumberComboBox();
				refreshVariableStateTree();
			}
			else if(event.getSource() == singleViewStateNumberComboBox)
			{
				selectedStateNumber = singleViewStateNumberComboBox.getSelectedIndex();
				refreshVariableStateTree();
			}
			else if(event.getSource() == singleViewFilterField)
			{
				filterString = singleViewFilterField.getText();
				refreshVariableStateTree();
			}
		}

		public void treeExpanded(TreeExpansionEvent event) {
			refreshStateVariableValueList();
		}

		public void treeCollapsed(TreeExpansionEvent event) {
			refreshStateVariableValueList();
		}
		
		public void valueChanged(TreeSelectionEvent e) {
			refreshValueAreaText();
		}

		public void stateTreeRecieved(State stateRoot, TimeStamp timeStamp) {
			if(stateRoot != null)
				updateDatabaseState(stateRoot, "TestSesh");
			else
				System.err.println("Error, could not update database, null state given");
			
		}
	}

	public GLDebug(String sessionName, PDStore store, GUID historyID)
	{
		this.sessionName = sessionName;
		this.store = store;
		
		ignoreActionEvents = false;
		
		debugProtocol = null;
		debugListenerThread = null;
		clientSocket = null;
		seq = 0;
		
		eventHandler = new EventHandler();
		
		windowFrame = new JFrame("GLDebug - " + sessionName);
		windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create debugger view
		debuggerTopLevelPane = new JPanel();
		debuggerButtonsPane = new JPanel();
		debuggerMessageScrollPane = new JScrollPane();
		debuggerMessagePane = new JPanel();
		
		debuggerRunButton = new JButton("Run");
		debuggerStopButton = new JButton("Stop");
		debuggerContinueButton = new JButton("Continue");
		debuggerRequestStateButton = new JButton("Request State");
		debuggerKillButton = new JButton("Kill");
		debuggerConnectButton =  new JButton("Connect");
		
		debuggerIPField = new JTextField("", 14);
		debuggerPortField = new JTextField("", 8 );
		
		debuggerMessageArea = new JTextArea();
		debuggerMessageArea.setEditable(false);
		
		debuggerTopLevelLayout = new BoxLayout(debuggerTopLevelPane, BoxLayout.PAGE_AXIS);
		debuggerTopLevelPane.setLayout(debuggerTopLevelLayout);
		
		debuggerButtonsLayout = new FlowLayout();
		debuggerButtonsPane.setLayout(debuggerButtonsLayout);
		debuggerButtonsPane.add(debuggerRunButton);
		debuggerButtonsPane.add(debuggerStopButton);
		debuggerButtonsPane.add(debuggerContinueButton);
		debuggerButtonsPane.add(debuggerRequestStateButton);
		debuggerButtonsPane.add(debuggerKillButton);
		debuggerButtonsPane.add(new JLabel("Address:"));
		debuggerButtonsPane.add(debuggerIPField);
		debuggerButtonsPane.add(new JLabel(":"));
		debuggerButtonsPane.add(debuggerPortField);
		debuggerButtonsPane.add(debuggerConnectButton);
		
		debuggerRunButton.addActionListener(eventHandler);
		debuggerStopButton.addActionListener(eventHandler);
		debuggerContinueButton.addActionListener(eventHandler);
		debuggerRequestStateButton.addActionListener(eventHandler);
		debuggerKillButton.addActionListener(eventHandler);
		debuggerConnectButton.addActionListener(eventHandler);
		
		debuggerMessageLayout = new BorderLayout();
		debuggerMessagePane.setLayout(debuggerMessageLayout);
		
		debuggerMessageScrollPane.setViewportView(debuggerMessageArea);
		debuggerMessagePane.add(debuggerMessageScrollPane, BorderLayout.CENTER);
		
		debuggerTopLevelPane.add(debuggerButtonsPane);
		debuggerTopLevelPane.add(debuggerMessagePane);
		
		// Create single view
		singleViewTopLevelPane = new JPanel();
		singleViewComboBoxPane = new JPanel();
		singleViewTreeDataPane = new JPanel();
		singleViewVariabelViewPane = new JPanel();
		
		singleViewTopLevelLayout = new BoxLayout(singleViewTopLevelPane, BoxLayout.PAGE_AXIS);
		singleViewTopLevelPane.setLayout(singleViewTopLevelLayout);
		
		singleViewComboBoxLayout = new FlowLayout();
		singleViewComboBoxPane.setLayout(singleViewComboBoxLayout);
		singleViewSessionComboBox = new JComboBox();
		singleViewSessionComboBox.setPrototypeDisplayValue("IAmASessionThatHasSelectedAVeryLongName"); // Set value to allow for long session names
		singleViewStateNumberComboBox = new JComboBox();
		singleViewStateNumberComboBox.setPrototypeDisplayValue(10000); // Set value to allow for large numbers of states
		singleViewFilterField = new JTextField("", 20);
		singleViewStateTree = new StateJTree();
		singleViewStateValueList = new JList();
		
		singleViewStateValueList.setCellRenderer(new DefaultListCellRenderer() { // Make list elements not selectable
			 
		    public Component getListCellRendererComponent(JList list, Object value, int index,
		            boolean isSelected, boolean cellHasFocus) {
		        super.getListCellRendererComponent(list, value, index, false, false);
		 
		        return this;
		    }
		});
		
		singleViewComboBoxPane.add(new JLabel("Session:"));
		singleViewComboBoxPane.add(singleViewSessionComboBox);
		singleViewComboBoxPane.add(new JLabel("State:"));
		singleViewComboBoxPane.add(singleViewStateNumberComboBox);
		singleViewComboBoxPane.add(new JLabel("Filter Expression (regex):"));
		singleViewComboBoxPane.add(singleViewFilterField);
		
		singleViewSessionComboBox.addActionListener(eventHandler);
		singleViewStateNumberComboBox.addActionListener(eventHandler);
		singleViewFilterField.addActionListener(eventHandler);
		singleViewStateTree.addTreeExpansionListener(eventHandler);
		singleViewStateTree.addTreeSelectionListener(eventHandler);
		
		singleViewValueArea1 = new JTextArea();
		singleViewValueArea1.setRows(5);
		singleViewValueArea2 = new JTextArea();
		singleViewValueArea2.setRows(5);
		singlewViewValueArea1ScrollPane = new JScrollPane(singleViewValueArea1);
		singlewViewValueArea2ScrollPane = new JScrollPane(singleViewValueArea2);
		singleViewValueSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, singlewViewValueArea1ScrollPane, singlewViewValueArea2ScrollPane);
		singleViewValueSplitPane.setResizeWeight(0.5); // Extra space is allocated evenly between the split components
		
		singleViewTreeDataLayout = new BorderLayout();
		singleViewTreeDataPane.setLayout(singleViewTreeDataLayout);
		
		singleViewTreeDataPane.add(singleViewStateTree, BorderLayout.LINE_START);
		singleViewTreeDataPane.add(singleViewStateValueList, BorderLayout.CENTER);
		
		
		singleViewTreeScrollPane = new JScrollPane(singleViewTreeDataPane);
		singleViewTreeScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		singleViewVariableViewLayout = new BorderLayout();
		singleViewVariabelViewPane.setLayout(singleViewVariableViewLayout);
		
		singleViewVariabelViewPane.add(singleViewTreeScrollPane, BorderLayout.CENTER);
		//singleViewVariabelViewPane.add(singlewViewValueArea1ScrollPane, BorderLayout.PAGE_END);
		singleViewVariabelViewPane.add(singleViewValueSplitPane, BorderLayout.PAGE_END);
		
		singleViewTopLevelPane.add(singleViewComboBoxPane);
		singleViewTopLevelPane.add(singleViewVariabelViewPane);
		
		refreshSessionComboBox();
		refreshStateNumberComboBox();
		refreshVariableStateTree();
		
		// Create the top level tabbed frame
		topLevelTabbedPane = new JTabbedPane();
		topLevelTabbedPane.addTab("Debugger Control", debuggerTopLevelPane);
		topLevelTabbedPane.addTab("Single View", singleViewTopLevelPane);

		windowFrame.getContentPane().add(topLevelTabbedPane);

		windowFrame.pack();
		windowFrame.setVisible(true);
	}

	public static void main(String[] args)
	{
		try {
			Class.forName("diagrameditor.dal.PDHistory");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
		
		GUID historyID;
		
		mainStore = new PDStore("MyGlStateDatabase");
		historyID = new GUID();

		GLDebug debug1 = new GLDebug("A", mainStore, historyID);
		//GLDebug debug2 = new GLDebug("B", workingCopy2, historyID);
	}
}
