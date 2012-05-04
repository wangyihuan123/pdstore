package pdstore.ui.graphview;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import nz.ac.auckland.se.genoupe.tools.Debug;

import diagrameditor.DrawPanel;
import diagrameditor.HistoryPanel;
import diagrameditor.Menu;
import diagrameditor.OperationList;
import diagrameditor.OperationPanel;
import diagrameditor.StatusBar;
import diagrameditor.dal.PDHistory;
import diagrameditor.dal.PDOperation;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.dal.PDSimpleWorkingCopy;
import pdstore.dal.PDWorkingCopy;
import pdstore.sparql.Variable;
import pdstore.ui.graphview.dal.PDGraph;
import pdstore.ui.graphview.dal.PDNode;

public class GraphEditor extends JFrame {

	PDStore store;
	PDWorkingCopy copy;

	GraphView graphView;

	public GraphEditor() {
		super();

		// make sure PDNode is registered as a DAL class
		PDNode.register();

		store = new PDStore("GraphView");
		copy = new PDSimpleWorkingCopy(store);
		PDGraph graph = new PDGraph(copy);
		graphView = new GraphView(graph);

		setTitle("Graph Editor - " + graph.getLabel());

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 700);
		this.setMinimumSize(new Dimension(100, 10));
		this.setLayout(new BorderLayout());

		// setting up the menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu graphMenu = new JMenu("Graph");
		menuBar.add(graphMenu);

		JMenuItem openFileItem = new JMenuItem("Open from file...");
		openFileItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ask for file
				JFileChooser fileChooser = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter(
						"PDStore database file", "pds");
				fileChooser.addChoosableFileFilter(filter);
				int returnVal = fileChooser.showOpenDialog(graphView);
				if (returnVal != JFileChooser.APPROVE_OPTION)
					return;

				String fileName = fileChooser.getSelectedFile().getName();
				store = new PDStore(fileName);
				copy = new PDSimpleWorkingCopy(store);

				selectGraph();
			}
		});
		graphMenu.add(openFileItem);

		JMenuItem selectGraphItem = new JMenuItem("Select...");
		selectGraphItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectGraph();
			}
		});
		graphMenu.add(selectGraphItem);

		JMenuItem renameGraphItem = new JMenuItem("Rename...");
		final PDGraph theGraph = graphView.getGraph();
		renameGraphItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String oldName = theGraph.getName();
				String newName = JOptionPane.showInputDialog(null,
						"New name for " + theGraph.getLabel() + "\":", oldName);

				if (newName.equals(oldName))
					return;

				theGraph.setName(newName);
				theGraph.getPDWorkingCopy().commit();
				graphView.repaint();

				setTitle("Graph Editor - " + theGraph.getLabel());
			}
		});
		graphMenu.add(renameGraphItem);

		StatusBar statusBar = new StatusBar();

		// setting up the overall layout
		Container contentPane = this.getContentPane();
		contentPane.add(graphView, BorderLayout.CENTER);
		contentPane.add(statusBar, BorderLayout.SOUTH);
		this.setVisible(true);
	}

	void selectGraph() {
		GUID transaction = store.begin();
		Collection<Object> graphs = store.getInstances(transaction,
				PDGraph.typeId, PDStore.HAS_TYPE_ROLEID.getPartner());
		store.commit(transaction);

		ArrayList<Object> graphLabels = new ArrayList<Object>();
		ArrayList<GUID> graphInstances = new ArrayList<GUID>();
		for (Object graphID : graphs) {
			graphLabels.add(copy.getLabel(graphID));
			graphInstances.add((GUID) graphID);
		}

		Object[] choices = graphLabels.toArray();
		String selectedLabel = (String) JOptionPane.showInputDialog(null,
				"Please select a graph to open:", "Open Graph",
				JOptionPane.PLAIN_MESSAGE, null, choices, null);

		if (selectedLabel == null || selectedLabel.length() < 0)
			return;

		int selection = graphLabels.indexOf(selectedLabel);
		PDGraph graph = PDGraph.load(graphView.copy,
				graphInstances.get(selection));
		graphView.setGraph(graph);

		setTitle("Graph Editor - " + graph.getLabel());
	}

	public static void main(String[] args) {
		GraphEditor graphEditor = new GraphEditor();
	}

	public PDGraph getExampleGraph() {
		PDWorkingCopy copy = new PDSimpleWorkingCopy(new PDStore("GraphView"));

		PDGraph graph = new PDGraph(copy);

		PDNode node1 = new PDNode(copy);
		node1.addInstance(PDStore.MODEL_TYPEID);
		node1.addX(30.);
		node1.addY(50.);
		graph.addNode(node1);

		PDNode node2 = new PDNode(copy);
		node2.addInstance(PDStore.TYPE_TYPEID);
		node2.addX(60.);
		node2.addY(50.);
		graph.addNode(node2);

		PDNode node3 = new PDNode(copy);
		node3.addInstance(PDStore.ROLE_TYPEID);
		node3.addX(100.);
		node3.addY(100.);
		graph.addNode(node3);

		PDNode node4 = new PDNode(copy);
		node4.addInstance(PDStore.OWNED_ROLE_ROLEID);
		node4.addX(150.);
		node4.addY(150.);
		graph.addNode(node4);

		PDNode node5 = new PDNode(copy);
		node5.addInstance(PDStore.OWNER_TYPE_ROLEID);
		node5.addX(150.);
		node5.addY(200.);
		graph.addNode(node5);

		copy.commit();
		return graph;
	}

}
