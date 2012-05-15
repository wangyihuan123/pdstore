package cms;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.Assert.*;

import cms.dal.PDDocument;

import pdstore.GUID;
import pdstore.GUIDGen;
import pdstore.PDStoreException;

@RunWith(value = Parameterized.class)
public class CMSPerformanceTest {

	private final static DecimalFormat DP3 = new DecimalFormat("#.###");
	
	final boolean NETWORK_ACCESS = false;	
	
	private int users;
	private int numTrials;
	private long sleep;
	private ArrayList<ContentManagementSystem> cmsList;
	private ExecutorService exec;
	private AtomicInteger badloc;
	private AtomicInteger nullp;
	private AtomicInteger pde;
	private AtomicInteger cmode;
	private AtomicInteger othere;
	
	public CMSPerformanceTest(int users, int numTrials, long sleep) {	
		this.users = users;
		this.numTrials = numTrials;
		this.sleep = sleep;
		initUserThreads();
		loadClasses();
	}
	
	private void loadClasses(){	
		// This is required
		try {
			Class.forName("cms.dal.PDDocument");
			Class.forName("cms.dal.PDHistory");
			Class.forName("cms.dal.PDCMSOperation");
			Class.forName("cms.dal.PDDocumentOperation");	
			Class.forName("cms.dal.PDFileOperation");
			Class.forName("cms.dal.PDUser");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail();
		}		
			
	}

	@Parameters
	public static Collection<Object[]> testParams() {
		
		// CMS propeties: {int users, int trials, int sleep}
		Object[][] params = new Object[][] { 
				{2, 100, 100}, {2, 100, 50}, {2, 100, 25}
		};
		return Arrays.asList(params);
	}
	
	@Before
	public void setUp(){
		
		badloc = new AtomicInteger(0);
		nullp = new AtomicInteger(0);	
		pde = new AtomicInteger(0);
		cmode = new AtomicInteger(0);
		othere = new AtomicInteger(0);
		
		try {
			CMSLoader cmsl = new CMSLoader(NETWORK_ACCESS, users);
			cmsl.init();
			cmsList = cmsl.cmsList;
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}		
	}

	
	@Test
	public void testTextEditPerformance() {
		
		// set same document
		GUID id = GUIDGen.generateGUIDs(1).remove(0);
		PDDocument curDoc; 
		for (ContentManagementSystem cms : cmsList){
			curDoc = PDDocument.load(cms.wc, id);
			cms.user.setCurrentDocument(curDoc);
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// num trials
		for (int i = 0; i < numTrials; i++){
			// submit random text edit op to each cms
			for (int j = 0; j < users; j++){
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				exec.execute(new TextEdit(cmsList.get(j)));
			}
		}
		
		exec.shutdown();
		try {
			exec.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int r = numTrials - badloc.get() - nullp.get() -pde.get() - cmode.get() - othere.get();
		double success = 100*((double) r / (double) numTrials);
		System.out.println("Users: "+users+", Sleep: "+sleep+" : Bad Locations: "+badloc.get()+", Null Pointers: "+nullp.get()+", PDStoreException: "+pde.get()+", ConcurrentModeExcpetion: "+cmode.get()+", Other: "+othere.get()+", Success: % "+DP3.format(success));
	}
	
	private void initUserThreads(){
		int limit = Runtime.getRuntime().availableProcessors();
		users = users > limit ? limit : users;
		exec = Executors.newFixedThreadPool(users);
	}
	
	class TextEdit implements Runnable {

		private final ContentManagementSystem cms;
		
		public TextEdit(final ContentManagementSystem cms) {
			this.cms = cms;
		}
		
		@Override
		public void run() {
			AbstractDocument doc = (AbstractDocument) cms.textEditor.getDocument();
			int random = (int) Math.random()*3;
			random = 0;
			try {
				switch (random){
				case 0:			
					doc.replace(doc.getLength(), 0, "p", null);	
					break;
				case 1:
					doc.remove(doc.getLength(), 1);
					break;
				case 2:
					doc.insertString(0, "i", null);
					break;
				}
			} catch (BadLocationException e) {
				//e.printStackTrace();
				badloc.incrementAndGet();
			} catch (NullPointerException e) {		
				//e.printStackTrace();
				nullp.incrementAndGet();	
			} catch (PDStoreException e) {		
				//e.printStackTrace();
				pde.incrementAndGet();		
			} catch (ConcurrentModificationException e) {
				//e.printStackTrace();
				cmode.incrementAndGet();								
			} catch (Exception e) {
				//e.printStackTrace();
				othere.incrementAndGet();				
			}
		}
		
	}
	
}