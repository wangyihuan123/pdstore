package cms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;

import pdstore.GUIDGen;
import pdstore.dal.PDInstance;
import pdstore.dal.PDWorkingCopy;

import cms.dal.PDUser;

public class CMSCaret extends DefaultCaret {


	private static final long serialVersionUID = 1L;
	JTextComponent component;
	int dot;
	private double aspectRatio = 0.2;
	private int caretWidth = 1;
	transient Position.Bias dotBias;
	PDWorkingCopy wc;
	PDUser user;


	public CMSCaret(PDWorkingCopy wc, PDUser user) // Or just the username
	{
		super();
		super.setBlinkRate(500); // TODO: maybe just set to 0
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
	public void paint(Graphics g)
	{
		if(isVisible()) 
		{
			try 
			{
				
				//Collection<Object> users = wc.getStore().getAllInstancesOfType(GUIDGen.generateGUIDs(1).remove(0), PDUser.typeId);
				Collection<PDInstance> users = wc.getAllInstancesOfType(PDUser.typeId);
				//ArrayList<Integer> users = new ArrayList<Integer>();
				//users.add(getDot());
				//int dot2 = getDot() - 3; dot2 = dot2 < 0?0:dot2;
				//users.add(dot2);
				
				JTextComponent component = getComponent();
				TextUI mapper = component.getUI();

				ArrayList<Rectangle> damages = new ArrayList<Rectangle>();
				Rectangle clip = g.getClipBounds();
				Rectangle r;
				int doRepaint = 0, getOut = 0;

				for (Object o : users){
					PDUser u = (PDUser) o;
					if (u.getName().equals(user.getName())) {
						r = mapper.modelToView(component, getDot(), Position.Bias.Backward); 
					} else {
						Object l = u.getCaretPosition();
						int pos = getDot();
						r = mapper.modelToView(component, pos, Position.Bias.Backward); 
					}
					if ((r == null) || ((r.width == 0) && (r.height == 0))) 
					{
						getOut++;			
					} else {
						if (width > 0 && height > 0 && !this.contains(r.x, r.y, r.width, r.height)) 
						{
							if (clip != null && !clip.contains(this)) 
							{
								doRepaint++;
							}
							damages.add(r);
						}	
						// Set color
						//long red = (long) u.getCaretColorR();
						//long green = (long) u.getCaretColorG().intValue();
						//long blue = (long) (u.getCaretColorB().intValue());
						//g.setColor(new Color((char)red, (char)green, (char)blue));
						g.setColor(new Color(0, 0, 0));
						
						// Paint caret
						int paintWidth = getCaretWidth(r.height);
						g.fillRect(r.x, r.y, paintWidth, r.height);
					}
				}
				if (getOut == users.size()){
					return;
				} else {
					if (doRepaint > 0){
						repaint();
					} else {
						damage(damages);
					}
				}
			} catch (BadLocationException e) 	{
				e.printStackTrace();

			}
		}
	}
}

