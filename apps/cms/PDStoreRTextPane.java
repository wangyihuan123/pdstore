package cms;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
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

	protected void setupDoc(){

		Document styledDoc = getDocument();
		if (styledDoc instanceof AbstractDocument) {
			doc = (AbstractDocument)styledDoc;    		
			doc.setDocumentFilter(new PDStoreDocumentFilter(wc, user, history, cms));
			doc.addDocumentListener(new DocumentUpdateListener());
		} else {
			System.err.println("Text pane's document isn't an AbstractDocument");
			System.exit(126);
		}	
	}	

	protected void updateFromHistory(CMSOperationList opHistory){
		synchronized (opHistory){
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
					if (dop.getOpDocument().getId().equals(user.getCurrentDocument().getId())){
						SwingUtilities.invokeLater(new DocumentOperationRunnable(dop));	
						histPoint.incrementAndGet();
					}
				}
			}
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

	protected class UserCaret {

		private String username;
		private int position;
		private Color color;
		private Rectangle r;

		public UserCaret(String username, int position, Color color) {

			this.username = username;
			this.position = position;
			this.color = color;
		}

		public int getPosition(){
			return position;
		}		

		public void setPosition(int p){
			position = p;
		}

		public Color getColor(){
			return color;
		}

		public String getName(){
			return username;
		}

		public void setRect(Rectangle r){
			this.r = r;
		}

		public Rectangle getRect(){
			return r;
		}		

	}

	protected class DocumentOperationRunnable implements Runnable {

		PDDocumentOperation op;

		public DocumentOperationRunnable(PDDocumentOperation op){
			this.op = op;
		}

		@Override
		public void run() {

			//AbstractDocument doc = (AbstractDocument)cms.textEditor.getDocument();
			PDStoreDocumentFilter filter = (PDStoreDocumentFilter) doc.getDocumentFilter();
			if (doc == null || filter == null){
				//return;
			}

			//while (op.getOpType() == null); // seems the get method can return null at first

			if (op.getOpType() == null){
				//return;
			}

			long type = op.getOpType();
			long offset = op.getOpOffset();
			long length = 0;
			String str = op.getOpString();

			// Do something appropriate given the OpType
			filter.setFilter(false);
			//try {
			switch ((int)type){	
			case PDStoreDocumentFilter.REMOVE:
				length = op.getOpLength();
				try {
					doc.remove((int) offset, (int) length);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case PDStoreDocumentFilter.INSERT:
				try {
					doc.insertString((int) offset, str, null);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
				break;
			case PDStoreDocumentFilter.REPLACE:	
				length = op.getOpLength();
				try {
					doc.replace((int) offset, (int) length, str, null);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
				break;		
			}
			//} catch (BadLocationException e){
			//System.out.println("Bad location in DoucmentListener");
			//}
			filter.setFilter(true);
		}

	}


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


