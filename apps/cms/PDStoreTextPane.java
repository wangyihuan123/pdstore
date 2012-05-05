package cms;

import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.StyledDocument;

import cms.dal.PDHistory;
import cms.dal.PDUser;

import pdstore.GUID;
import pdstore.dal.PDWorkingCopy;

public class PDStoreTextPane extends JTextPane {
	
	private static final long serialVersionUID = 1L;

	public PDStoreTextPane(PDWorkingCopy wc, PDUser user, PDHistory history){
		super();
		
		AbstractDocument doc;
        StyledDocument styledDoc = getStyledDocument();
        if (styledDoc instanceof AbstractDocument) {
            doc = (AbstractDocument)styledDoc;
            doc.setDocumentFilter(new PDStoreDocumentFilter(wc, user, history));
        } else {
            System.err.println("Text pane's document isn't an AbstractDocument");
            System.exit(126);
        }
	}
	
}
