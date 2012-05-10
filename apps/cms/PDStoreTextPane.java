package cms;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import cms.dal.PDHistory;
import cms.dal.PDUser;

import pdstore.GUID;
import pdstore.dal.PDInstance;
import pdstore.dal.PDWorkingCopy;
import pdstore.generic.PDChange;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListener;

public class PDStoreTextPane extends RSyntaxTextArea {
	
	private static final long serialVersionUID = 1L;
	private PDWorkingCopy wc;
	private PDUser user;
	private ArrayList<UserCaret> carets;
	private JTextPane htmlEditor;

	public PDStoreTextPane(PDWorkingCopy wc, PDUser user, PDHistory history, JTextPane htmlEditor){
		super();
		this.wc = wc;
		this.user = user;
		this.htmlEditor = htmlEditor;
		
		carets = new ArrayList<UserCaret>();
		setUserCarets();
		
		AbstractDocument doc;
        Document styledDoc = getDocument();
        if (styledDoc instanceof AbstractDocument) {
            doc = (AbstractDocument)styledDoc;
            doc.setDocumentFilter(new PDStoreDocumentFilter(wc, user, history));
            doc.addDocumentListener(new DocumentUpdateListener());
        } else {
            System.err.println("Text pane's document isn't an AbstractDocument");
            System.exit(126);
        }
        
        // Add listeners
        GUID role2 = PDUser.roleCaretPositionId;
        PDCaretListener listener = new PDCaretListener(role2);
        this.addCaretListener(listener);
		wc.getStore().getDetachedListenerList().add(listener);
        
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
				
			int dot = event.getDot();
			carets.get(carets.size()-1).setPosition(dot);
			
			// Inform others
			user.setCaretPosition(new Long(dot));
			wc.commit();
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
	
	protected class DocumentUpdateListener implements DocumentListener{

		@Override
		public void changedUpdate(DocumentEvent dev) {
			if (htmlEditor != null){
				Document d = dev.getDocument();
				try {
					htmlEditor.setText(d.getText(0, d.getLength()));
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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


