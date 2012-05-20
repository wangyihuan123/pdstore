package cms;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.lobobrowser.html.gui.HtmlPanel;

import cms.PDStoreTextPane.DocumentUpdateListener;
import cms.dal.PDCMSOperation;
import cms.dal.PDDocument;
import cms.dal.PDDocumentOperation;
import cms.dal.PDHistory;
import cms.dal.PDUser;

import pdstore.GUID;
import pdstore.PDStoreException;
import pdstore.dal.PDInstance;
import pdstore.dal.PDWorkingCopy;
import pdstore.generic.PDChange;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListener;

/**
 * The is an implementation of the RSyntaxTextArea to show collaborative edits in real-time.
 * 
 * @author Sina Masoud-Ansari (s.ansari@auckland.ac.nz)
 *
 */
public class PDStoreRTextPane extends RSyntaxTextArea {

	private static final long serialVersionUID = 1L;
	private PDWorkingCopy wc;
	private PDUser user;
	private ArrayList<UserCaret> carets;
	private PDHtmlPanel htmlEditor;
	private ContentManagementSystem cms;
	private AbstractDocument doc;
	private PDHistory history;	
	private AtomicInteger histPoint;


	/**
	 * 	
	 * @param wc the PDStore working copy shared by multiple users.
	 * @param user the PDUser of the Content Management System.
	 * @param history the PDHistory shared between users.
	 * @param htmlTextArea the html view of the text contents.
	 * @param cms the Content Management System this text area is displayed in.
	 */
	public PDStoreRTextPane(PDWorkingCopy wc, PDUser user, PDHistory history, PDHtmlPanel htmlTextArea, ContentManagementSystem cms){
		super();
		this.wc = wc;
		this.user = user;
		this.htmlEditor = htmlTextArea;
		this.cms = cms;
		this.history = history;

		histPoint = new AtomicInteger(0);
		carets = new ArrayList<UserCaret>();
		setUserCarets();

		setupDoc();

		// Add listeners
		GUID role2 = PDUser.roleCaretPositionId;
		PDCaretListener listener = new PDCaretListener(role2);
		this.addCaretListener(listener);
		wc.getStore().getDetachedListenerList().add(listener);

	}

	/**
	 * Perfoms basic initialisation of a Document when it has been selected by a user.
	 */
	protected void setupDoc(){

		Document styledDoc = getDocument();
		if (styledDoc instanceof AbstractDocument) {
			doc = (AbstractDocument)styledDoc; 
			histPoint.set(0);
			doc.setDocumentFilter(new PDStoreDocumentFilter(wc, user, history, cms));
			doc.addDocumentListener(new DocumentUpdateListener());
		} else {
			System.err.println("Text pane's document isn't an AbstractDocument");
			System.exit(126);
		}	
	}	

	/**
	 * This method is used to rebuild the document to reflect changes made by a group of collaborators.
	 * 
	 * @param opHistory the operations history linked list.
	 */
	protected void updateFromHistory(CMSOperationList opHistory){
		synchronized (opHistory){
			try {
				for (int j = histPoint.get(); j < opHistory.size(); j++){
					PDCMSOperation op = opHistory.get(j);
					if (op == null){
						return;
					}
					if (op.getOpType().getId().equals(PDDocumentOperation.typeId)){
						PDDocumentOperation dop = op.getDocumentOp();
						if (dop == null){
							return;
						}
						PDDocument pddoc = dop.getOpDocument();
						if (pddoc == null){
							return;
						}
						PDDocument userdoc = user.getCurrentDocument();
						if (userdoc == null) {
							return;
						}
						if (pddoc.getId().equals(userdoc.getId())){
							SwingUtilities.invokeLater(new DocumentOperationRunnable(dop));	
							histPoint.incrementAndGet();
						}
					}
				}
			} catch (Exception e) {
				return;
			}
			/*
			} catch (ClassCastException e) {
				//updateFromHistory(opHistory);
			} catch (PDStoreException e) {
				//updateFromHistory(opHistory);				
			} catch (ArrayIndexOutOfBoundsException e) {
				//updateFromHistory(opHistory);
			} catch (ConcurrentModificationException e) {
				//updateFromHistory(opHistory);
			} finally {
				System.err.println("Exception thrown in PDStoreRTextPane.updateFromHistory");
			}
			 */
			// Buffered update of htmlview i.e not for every operation
			/*
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					try {
						htmlEditor.render(doc.getText(0, doc.getLength()));
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});		
			 */	
		}		
	}	

	/**
	 * Updated the Document carets of multiple authors on a document.
	 */
	private void setUserCarets(){

		Collection<PDInstance> users = wc.getAllInstancesOfType(PDUser.typeId);
		UserCaret mainUser = null;
		int r, g, b, pos;
		Color c;
		for (Object o : users){
			PDUser u = (PDUser) o;

			// Assign caret colors
			r = u.getCaretColorR().intValue();
			g = u.getCaretColorG().intValue();
			b = u.getCaretColorB().intValue();
			c = new Color(r, g, b);

			String username = u.getName();
			// Set aside main user
			if (username.equals(user.getName())) {
				mainUser = new UserCaret(username, this.getCaretPosition(), c);
			} else {
				// Add other user carets
				pos = u.getCaretPosition().intValue();
				pos = pos < 0 ? 0 : pos;
				carets.add(new UserCaret(username, pos, c));
			}
		}
		// Make sure main user is last so it gets the blink
		carets.add(mainUser);
	}

	/**
	 * 
	 * @return the list of users working on a document and the positions of their carets
	 */
	public ArrayList<UserCaret>getUserCarets(){
		return carets;
	}

	public UserCaret getUserCaret(String username){
		for (UserCaret uc : carets){
			if (uc.username.equals(username)){
				return uc;
			}
		}
		return null;
	}

	/**
	 * Listens for changes in caret positions and notifies other users.
	 * 
	 * @author Sina Masoud-Ansari (s.ansari@auckland.ac.nz)
	 *
	 */
	class PDCaretListener implements CaretListener, PDListener<GUID, Object, GUID> {

		private GUID role2;

		public PDCaretListener(GUID role2){
			this.role2 = role2;
		}

		@Override
		public void caretUpdate(CaretEvent event) {
			//System.out.println("View: "+user.getName()+"User: "+user.getName()+" caret updated: "+event.getDot());
			try {	
				int dot = event.getDot();
				carets.get(carets.size()-1).setPosition(dot);

				// Inform others
				user.setCaretPosition(new Long(dot));
				//wc.commit();
			} catch (PDStoreException e) {
				throw e;
			}
		}

		@Override
		public void transactionCommitted(
				List<PDChange<GUID, Object, GUID>> transaction,
				List<PDChange<GUID, Object, GUID>> matchedChanges,
				PDCoreI<GUID, Object, GUID> core) {

			for (PDChange<GUID, Object, GUID> change : transaction) {
				if (change.getRole2().equals(role2)){
					PDUser other = PDUser.load(wc, (GUID)change.getInstance1());
					//while (other.getName() == null);
					String username = other.getName();
					// Update local view of other caret positions
					if (!user.getName().equals(username)) {
						int position = other.getCaretPosition().intValue();
						UserCaret uc = getUserCaret(username);
						if (uc != null){
							//System.out.println("View: "+user.getName()+"User: "+other.getName()+" caret updated: "+position);
							uc.setPosition(position);
						}						
					}

				}
			}

		}

		@Override
		public Collection<PDChange<GUID, Object, GUID>> getMatchingTemplates() {
			return null;
		}

	}

	/**
	 * Represents properties of a user caret, e.g the owner of th caret, its position, color and 
	 * bounding rectangle.
	 * 
	 * @author Sina Masoud-Ansari (s.ansari@auckland.ac.nz)
	 *
	 */
	protected class UserCaret {

		private String username;
		private int position;
		private Color color;
		private Rectangle r;

		/**
		 * 
		 * @param username which user the caret belongs to
		 * @param position the position of the caret
		 * @param color the color of the caret
		 */
		public UserCaret(String username, int position, Color color) {

			this.username = username;
			this.position = position;
			this.color = color;
		}

		/**
		 * 
		 * @return the current position of the caret.
		 */
		public int getPosition(){
			return position;
		}		

		/**
		 * 
		 * @param p the position of the caret.
		 */
		public void setPosition(int p){
			position = p;
		}

		/**
		 * 
		 * @return the current color of the caret.
		 */
		public Color getColor(){
			return color;
		}

		/**
		 * 
		 * @return the name of the user who owns the caret.
		 */
		public String getName(){
			return username;
		}

		/**
		 * Used for fitting a caret into the text.
		 * 
		 * @param r the bounding rectange of the caret.
		 */
		public void setRect(Rectangle r){
			this.r = r;
		}

		/**
		 * 
		 * @return the bounding rectangle of the caret.
		 */
		public Rectangle getRect(){
			return r;
		}		

	}

	/**
	 * Used to perform an operation on a document e.g add or remove strings.
	 * 
	 * @author Sina Masoud-Ansari (s.ansari@auckland.ac.nz)
	 *
	 */
	protected class DocumentOperationRunnable implements Runnable {

		PDDocumentOperation op;

		public DocumentOperationRunnable(PDDocumentOperation op){
			this.op = op;
		}

		@Override
		public void run() {

			PDStoreDocumentFilter filter = (PDStoreDocumentFilter) doc.getDocumentFilter();
			
			// seems the get method can return null sometimes
			if (op.getOpType() == null){
				return;
			}

			long type = op.getOpType();
			long offset = op.getOpOffset();
			long length = 0;
			String str = op.getOpString();

			// Turn off routing of operations to PDStore and execute them locally.
			filter.setFilter(false);
			
			// Do something appropriate given the OpType
			switch ((int)type){	
			case PDStoreDocumentFilter.REMOVE:
				length = op.getOpLength();
				try {
					doc.remove((int) offset, (int) length);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				break;
			case PDStoreDocumentFilter.INSERT:
				try {
					doc.insertString((int) offset, str, null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}					
				break;
			case PDStoreDocumentFilter.REPLACE:	
				length = op.getOpLength();
				try {
					doc.replace((int) offset, (int) length, str, null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}			
				break;		
			}

			filter.setFilter(true);
		}

	}


	/**
	 * Listens for changes in a document and udates the html rendering of the document text.
	 * 
	 * @author Sina Masoud-Ansari (s.ansari@auckland.ac.nz)
	 *
	 */
	protected class DocumentUpdateListener implements DocumentListener{

		@Override
		public void changedUpdate(DocumentEvent dev) {
			if (htmlEditor != null){
				Document d = dev.getDocument();

				try {
					htmlEditor.render(d.getText(0, d.getLength()));
					//System.out.println("Updating html view ...");
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					throw new NullPointerException();
				}

			}

		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

}


