package cms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.w3c.dom.Document;
import org.w3c.dom.html2.HTMLElement;
import org.xml.sax.SAXException;

/**
 * The web page renderer.
 * Mostly derived from the example at: http://lobobrowser.org/cobra/getting-started.jsp
 * 
 * @author Sina Masoud-Ansari (s.ansari@auckland.ac.nz)
 *
 */
public class PDHtmlPanel extends HtmlPanel{
	
	HtmlRendererContext rendererContext;
	DocumentBuilderImpl builder;

	public PDHtmlPanel(){
		super();
		UserAgentContext ucontext = new LocalUserAgentContext();
		rendererContext = new LocalHtmlRendererContext(this, ucontext);
		Logger.getLogger("org.lobobrowser").setLevel(Level.SEVERE);
		builder = new DocumentBuilderImpl(rendererContext.getUserAgentContext(), rendererContext);	
		//render("<html>Hello</html>");
	}
	
	/**
	 * Called by other classes to update the html view upon text edits
	 * 
	 * @param s the html text
	 */
	protected void render(String s){
		Document document = null;
		try {
			//document = builder.parse(new File("/Users/smas036/www/index.html"));
			// convert String into InputStream
			InputStream is = new ByteArrayInputStream(s.getBytes());
			document = builder.parse(is);
			is.close();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		setDocument(document, rendererContext);
	}

	private static class LocalUserAgentContext extends SimpleUserAgentContext {
		// Override methods from SimpleUserAgentContext to
		// provide more accurate information about application.

		public LocalUserAgentContext() {
		}

		public String getAppMinorVersion() {
			return "0";
		}

		public String getAppName() {
			return "BarebonesTest";
		}

		public String getAppVersion() {
			return "1";
		}

		public String getUserAgent() {
			return "Mozilla/4.0 (compatible;) CobraTest/1.0";
		}
	}


	private static class LocalHtmlRendererContext extends SimpleHtmlRendererContext {
		// Override methods from SimpleHtmlRendererContext 
		// to provide browser functionality to the renderer.

		public LocalHtmlRendererContext(HtmlPanel contextComponent, 
				UserAgentContext ucontext) {
			super(contextComponent, ucontext);
		}

		public void linkClicked(HTMLElement linkNode, 
				URL url, String target) {
			super.linkClicked(linkNode, url, target);
			// This may be removed: 
			System.out.println("## Link clicked: " + linkNode);
		}
	}	

}
