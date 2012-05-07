package cms;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;

import org.fife.ui.rtextarea.ConfigurableCaret;

import pdstore.dal.PDWorkingCopy;

import cms.PDStoreTextPane.UserCaret;
import cms.dal.PDUser;

public class CMSCaret extends ConfigurableCaret {


	private static final long serialVersionUID = 1L;
	JTextComponent component;
	int dot;
	private double aspectRatio = 0.1;
	private int caretWidth = 1;
	transient Position.Bias dotBias;
	PDWorkingCopy wc;
	PDUser user;


	public CMSCaret(PDWorkingCopy wc, PDUser user)
	{
		super();
		super.setBlinkRate(500);
		this.wc = wc;
		this.user = user;
	}

	int getCaretWidth(int height) {
		if (aspectRatio > -1) {
			return (int) (aspectRatio * height) + 1;
		}

		if (caretWidth > -1) {
			return caretWidth;
		}

		return 1;
	}	

	protected synchronized void damage(ArrayList<Rectangle> list) {
		for (Rectangle r : list){
			if (r != null) {
				int damageWidth = getCaretWidth(r.height);
				x = r.x - 4 - (damageWidth >> 1);
				y = r.y;
				width = 9 + damageWidth;
				height = r.height;

			}
		}
		repaint();
	}


	@Override
	public void paint(Graphics g) {

		PDStoreTextPane component = (PDStoreTextPane) getComponent();
		TextUI mapper = component.getUI();

		ArrayList<UserCaret> carets = component.getUserCarets();
		Rectangle clip = g.getClipBounds();
		Rectangle r = null;

		// Iterate over user carets
		for (UserCaret uc: carets) {

			try {
				r = mapper.modelToView(component, uc.getPosition(), Position.Bias.Forward);
			} catch (BadLocationException e) {} 

			if (! ((r == null) || ((r.width == 0) && (r.height == 0))) ) {

				if (width > 0 && height > 0 && !this.contains(r.x, r.y, r.width, r.height)) 
				{
					if (clip != null && !clip.contains(this)) 
					{
						repaint();
					}
					damage(r);
				}	
				uc.setRect(r);

			}										
		}
		// Draw successful carets
		for (UserCaret uc : carets){

			// if user caret
			if (uc.getName().equals(user.getName())){
				if (isVisible()){
					paintUserCaret(g, uc);
				}
			} else {
				// other caret shouldn't blink
				paintUserCaret(g, uc);
			}	
		}

	}

	private void paintUserCaret(Graphics g, UserCaret uc){
		Rectangle rect = uc.getRect();
		g.setColor(uc.getColor());
		int paintWidth = getCaretWidth(rect.height);
		g.fillRect(rect.x, rect.y, paintWidth, rect.height);		
	}
}