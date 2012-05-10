package cms;
import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DropMode;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class DndTree {
  public static void main(String args[]) {
    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel top = new JPanel(new BorderLayout());
    JLabel dragLabel = new JLabel("Drag me:");
    JTextField text = new JTextField();
    text.setDragEnabled(true);
    top.add(dragLabel, BorderLayout.WEST);
    top.add(text, BorderLayout.CENTER);
    f.add(top, BorderLayout.NORTH);

    final JTree tree = new JTree();
    final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
    tree.setTransferHandler(new TransferHandler() {
      public boolean canImport(TransferHandler.TransferSupport support) {
        if (!support.isDataFlavorSupported(DataFlavor.stringFlavor) || !support.isDrop()) {
          return false;
        }
        JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
        return dropLocation.getPath() != null;
      }

      public boolean importData(TransferHandler.TransferSupport support) {
        if (!canImport(support)) {
          return false;
        }
        JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
        TreePath path = dropLocation.getPath();
        Transferable transferable = support.getTransferable();

        String transferData;
        try {
          transferData = (String) transferable.getTransferData(DataFlavor.stringFlavor);
        } catch (IOException e) {
          return false;
        } catch (UnsupportedFlavorException e) {
          return false;
        }

        int childIndex = dropLocation.getChildIndex();
        if (childIndex == -1) {
          childIndex = model.getChildCount(path.getLastPathComponent());
        }

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(transferData);
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        model.insertNodeInto(newNode, parentNode, childIndex);

        TreePath newPath = path.pathByAddingChild(newNode);
        tree.makeVisible(newPath);
        tree.scrollRectToVisible(tree.getPathBounds(newPath));
        return true;
      }
    });

    JScrollPane pane = new JScrollPane(tree);
    f.add(pane, BorderLayout.CENTER);

    tree.setDropMode(DropMode.INSERT);
    f.setSize(300, 400);
    f.setVisible(true);
  }
}