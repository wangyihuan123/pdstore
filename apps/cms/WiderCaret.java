package cms;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.plaf.TextUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;

/**
 * 
 * Creates a wider Caret for use in collaborative environments
 * 
 * @author https://forums.oracle.com/forums/thread.jspa?threadID=1356564
 *
 */
class WiderCaret extends DefaultCaret
{

	private static final long serialVersionUID = 1L;
	JTextComponent component;
	int dot;
	transient Position.Bias dotBias;


	public WiderCaret() 
	{
		super();
		super.setBlinkRate(500);
	}

	public void paint(Graphics g)
	{
		if(isVisible()) 
		{
			try 
			{
				JTextComponent component = getComponent();
				TextUI mapper = component.getUI();
				Rectangle r = mapper.modelToView(component, getDot(), Position.Bias.Forward);

				if ((r == null) || ((r.width == 0) && (r.height == 0))) 
				{
					return;
				}

				if (width > 0 && height > 0 && !this.contains(r.x, r.y, r.width, r.height)) 
				{
					Rectangle clip = g.getClipBounds();

					if (clip != null && !clip.contains(this)) 
					{
						repaint();
					}
					damage(r);
				}

				g.setColor(component.getCaretColor());
				g.fillRect(r.x, r.y, 5, r.height);

				Document doc = component.getDocument();
				if (doc instanceof AbstractDocument) 
				{
					Element bidi = ((AbstractDocument)doc).getBidiRootElement();
					if ((bidi != null) && (bidi.getElementCount() > 1)) 
					{
					}
				}
			} 
			catch (BadLocationException e) 
			{
			}
		}
	}
}
