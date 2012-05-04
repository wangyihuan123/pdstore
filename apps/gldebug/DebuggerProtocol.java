package gldebug;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

// This file is largely a Java reworking of some of the methods used by the gldb-gui debugger included with BuGLe
public class DebuggerProtocol
{
	static final int RESP_ANS =                     0xabcd0000;
	static final int RESP_BREAK =                   0xabcd0001;
	static final int RESP_BREAK_EVENT =             0xabcd0002;
	static final int RESP_STOP =                    0xabcd0003;  /* Obsolete */
	static final int RESP_STATE =                   0xabcd0004;  /* Obsolete */
	static final int RESP_ERROR =                   0xabcd0005;
	static final int RESP_RUNNING =                 0xabcd0006;
	static final int RESP_SCREENSHOT =              0xabcd0007;  /* Obsolete */
	static final int RESP_STATE_NODE_BEGIN =        0xabcd0008;
	static final int RESP_STATE_NODE_END =          0xabcd0009;
	static final int RESP_DATA =                    0xabcd000a;
	static final int RESP_STATE_NODE_BEGIN_RAW_OLD =0xabcd000b;  /* Obsolete */
	static final int RESP_STATE_NODE_END_RAW =      0xabcd000c;
	static final int RESP_STATE_NODE_BEGIN_RAW =    0xabcd000d;
	static final int RESP_CALL = 					0xabcd0010; /* Bryce defined type for sending state */
	static final int RESP_STATE_HEADER = 			0xabcd0011; /* Bryce defined type for sending timestamp header */

	static final int REQ_RUN =                      0xdcba0000;
	static final int REQ_CONT =                     0xdcba0001;
	static final int REQ_STEP =                     0xdcba0002;
	static final int REQ_BREAK =                    0xdcba0003;
	static final int REQ_BREAK_ERROR =              0xdcba0004;  /* Obsolete, replaced by REQ_BREAK_EVENT */
	static final int REQ_STATE =                    0xdcba0005;  /* Obsolete */
	static final int REQ_QUIT =                     0xdcba0006;
	static final int REQ_ASYNC =                    0xdcba0007;
	static final int REQ_SCREENSHOT =               0xdcba0008;  /* Obsolete */
	static final int REQ_ACTIVATE_FILTERSET =       0xdcba0009;
	static final int REQ_DEACTIVATE_FILTERSET =     0xdcba000a;
	static final int REQ_STATE_TREE =               0xdcba000b;
	static final int REQ_DATA =                     0xdcba000c;
	static final int REQ_STATE_TREE_RAW_OLD =       0xdcba000d;  /* Obsolete */
	static final int REQ_STATE_TREE_RAW =           0xdcba000e;
	static final int REQ_BREAK_EVENT =              0xdcba000f;

	static final int REQ_DATA_TEXTURE =             0xedbc0000;
	static final int REQ_DATA_SHADER =              0xedbc0001;
	static final int REQ_DATA_FRAMEBUFFER =         0xedbc0002;
	static final int REQ_DATA_INFO_LOG =            0xedbc0003;
	static final int REQ_DATA_BUFFER =              0xedbc0004;

	static final int REQ_EVENT_GL_ERROR =           0x00000000;
	static final int REQ_EVENT_COMPILE_ERROR =      0x00000001;
	static final int REQ_EVENT_LINK_ERROR =         0x00000002;
	/* Count of events - increment as events are added */
	static final int REQ_EVENT_COUNT =              0x00000003;

	public interface StateTreeRecievedListener
	{
		void stateTreeRecieved(State stateRoot, TimeStamp timeStamp);
	}
	
	enum DebuggerStatus
	{
		GLDB_STATUS_DEAD,
		GLDB_STATUS_STARTED,
		GLDB_STATUS_RUNNING,
		GLDB_STATUS_STOPPED
	}
	
	class Response
	{
		public int code;
		public int id;
	}
	
	class ResponseAns extends Response
	{
		public int value;
	}

	class ResponseBreak extends Response
	{
		public String call;
	}

	class ResponseBreakEvent extends Response
	{
		public String call;
		public String event;
	}

	class ResponseStop extends Response
	{
		public String call;
	}

	class ResponseState extends Response
	{
		public String state;
	}

	class ResponseError extends Response
	{
		public int errorCode;
		public String error;
	}

	class ResponseRunning  extends Response
	{
	}

	class ResponseScreenshot extends Response
	{
		public char[] data; // TODO: need an appropriate datatype
		public int length;
	}

	class ResponseStateTree extends Response
	{
		public TimeStamp stamp;
		public State root;
	} 

	class ResponseDataTexture extends Response
	{
		public int subtype;
		public char[] data;
		public int length;
		public int width;
		public int height;
		public int depth;
	} 

	class ResponseDataFramebuffer extends Response
	{
		public int subtype;
		public char[] data;
		public int length;
		public int width;
		public int height;
	}

	class ResponseDataShader extends Response
	{
		public int subtype;
		public char[] data;
		public int length;
	}

	class ResponseDataInfoLog extends Response
	{
		public int subtype;
		public char[] data;
		public int length;
	}

	class ResponseDataBuffer extends Response
	{
		public int subtype;
		public char[] data;
		public int length;
	}

	class ResponseData extends Response
	{
		public int subtype;
		public char[] data;
		public int length;
	}  /* Generic form of gldb_response_data_* */
	
	class ResponseCall extends Response
	{
		public String call;
	}
	
	class TimeStamp
	{
		public int sec;
		public int min;
		public int hour;
		public int mday;
		public int mon;
		public int year;
	}

	protected BaseProtocol baseProtocol;
	
	protected boolean[] breakOnEvent;
	protected Hashtable<String, Character> breakOn;
	protected DebuggerStatus status;
	protected State state;
	protected List<StateTreeRecievedListener> stateRecievedListeners;
	
	public DebuggerProtocol(Socket sock)
	{
		baseProtocol = new BaseProtocol(sock);
		breakOnEvent = new boolean[REQ_EVENT_COUNT];
		for (int i = 0; i < REQ_EVENT_COUNT; ++i)
		{
			breakOnEvent[i] = true;
		}
		breakOn = new Hashtable<String, Character>();
		
		stateRecievedListeners = new ArrayList<StateTreeRecievedListener>();
	}

	/*void gldb_program_clear(void)
	{
		int i;

		for (i = 0; i < GLDB_PROGRAM_SETTING_COUNT; i++)
		{
			bugle_free(prog_settings[i]);
			prog_settings[i] = NULL;
		}
		prog_type = GLDB_PROGRAM_TYPE_LOCAL;
	}*/
	
	public Response getResponseAns(int code, int id) throws IOException
	{
		ResponseAns r = new ResponseAns();

		r.code = code;
		r.id = id;
		r.value = baseProtocol.recieveCode();
		return (Response)r;
	}
	
	public Response getResponseBreak(int code, int id) throws IOException
	{
		ResponseBreak r = new ResponseBreak();

		r.code = code;
		r.id = id;
		r.call = baseProtocol.recieveString();
		return (Response)r;
	}

	public Response getResponseBreakEvent(int code, int id) throws IOException
	{
		ResponseBreakEvent r = new ResponseBreakEvent();

		r.code = code;
		r.id = id;
		r.call = baseProtocol.recieveString();
		r.event = baseProtocol.recieveString();
		return (Response)r;
	}

	public Response getResponseState(int code, int id) throws IOException
	{
		ResponseState r = new ResponseState();

		r.code = code;
		r.id = id;
		r.state = baseProtocol.recieveString();
		return (Response)r;
	}
	
	public Response getResponseStateTree(int code, int id) throws IOException
	{
		ResponseStateTree r = new ResponseStateTree();

		r.code = code;
		r.id = id;
		r.stamp = recieveTime();
		if(baseProtocol.recieveCode() != RESP_STATE_NODE_BEGIN)
		{
			System.err.println("Unexpected code in getResponseStateTree, no beginning node after header");
			// TODO: throw exception
		}
		if(baseProtocol.recieveCode() != id)
		{
			System.err.println("Unexpected id in getResponseStateTree, no beginning node after header");
			// TODO: throw exception
		}
		r.root = recieveStateTree();
		for(StateTreeRecievedListener listener : stateRecievedListeners)
			listener.stateTreeRecieved(r.root, r.stamp);
		
		return (Response)r;
	}

	public Response getResponseError(int code, int id) throws IOException
	{
		ResponseError r = new ResponseError();

		r.code = code;
		r.id = id;
		r.errorCode = baseProtocol.recieveCode();
		r.error = baseProtocol.recieveString();
		return (Response)r;
	}

	public Response getResponseRunning(int code, int id)
	{
		ResponseRunning r = new ResponseRunning();

		r.code = code;
		r.id = id;
		return (Response)r;
	}

	public Response getResponseScreenshot(int code, int id) throws IOException
	{
		ResponseScreenshot r = new ResponseScreenshot();

		r.code = code;
		r.id = id;
		r.data = baseProtocol.recieveString().toCharArray(); //TODO: This may need tweaking;
		return (Response)r;
	}
	
	public Response getResponseCall(int code, int id) throws IOException
	{
		ResponseCall r = new ResponseCall();
		r.code = code;
		r.id = -1; // TODO: in future maybe use this ID
		r.call = baseProtocol.recieveString();
		return (Response)r;
	}
	
	boolean sendRun(int id)
	{
		String hashtableKey;
		int event;

		/* Send breakpoints */
		for (event = 0; event < REQ_EVENT_COUNT; event++)
		{
			baseProtocol.sendCode(REQ_BREAK_EVENT);
			baseProtocol.sendCode(0);
			baseProtocol.sendCode(event);
			baseProtocol.sendCode(breakOnEvent[event] ? 1 : 0);
		}
		
		for (String key : breakOn.keySet()) // This for loop needs more investigation to check if it behaves as intended
		{
			baseProtocol.sendCode(REQ_BREAK);
			baseProtocol.sendCode(0);
			baseProtocol.sendString((String)key); // Can send key as code? More investigation?
			baseProtocol.sendCode(breakOn.get(key) - '0'); // Convert to an int...
		}
		baseProtocol.sendCode(REQ_RUN);
		baseProtocol.sendCode(id);
		setStatus(DebuggerStatus.GLDB_STATUS_STARTED);
		return true;
	}

	void sendContinue(int id)
	{
		assert(status == DebuggerStatus.GLDB_STATUS_STOPPED);
		setStatus(DebuggerStatus.GLDB_STATUS_RUNNING);
		baseProtocol.sendCode(REQ_CONT);
		baseProtocol.sendCode(id);
	}

	void sendStep(int id)
	{
		assert(status == DebuggerStatus.GLDB_STATUS_STOPPED);
		setStatus(DebuggerStatus.GLDB_STATUS_RUNNING);
		baseProtocol.sendCode(REQ_STEP);
		baseProtocol.sendCode(id);
	}

	void sendQuit(int id)
	{
		assert(status != DebuggerStatus.GLDB_STATUS_DEAD);
		baseProtocol.sendCode(REQ_QUIT);
		baseProtocol.sendCode(id);
	}

	void sendEnableDisable(int id, String filterset, boolean enable)
	{
		assert(status != DebuggerStatus.GLDB_STATUS_DEAD);
		baseProtocol.sendCode(enable ? REQ_ACTIVATE_FILTERSET : REQ_DEACTIVATE_FILTERSET);
		baseProtocol.sendCode(id);
		baseProtocol.sendString(filterset);
	}

	void sendScreenshot(int id)
	{
		assert(status != DebuggerStatus.GLDB_STATUS_DEAD);
		baseProtocol.sendCode(REQ_SCREENSHOT);
		baseProtocol.sendCode(id);
	}

	void sendAsync(int id)
	{
		assert(status != DebuggerStatus.GLDB_STATUS_DEAD && status != DebuggerStatus.GLDB_STATUS_STOPPED);
		baseProtocol.sendCode(REQ_ASYNC);
		baseProtocol.sendCode(id);
	}

	void sendStateTree(int id)
	{
		assert(status != DebuggerStatus.GLDB_STATUS_DEAD);
		baseProtocol.sendCode(REQ_STATE_TREE);
		baseProtocol.sendCode(id);
	}

	void sendDataTexture(int id, int texId, int target,
								int face, int level, int format,
								int type)
	{
		assert(status != DebuggerStatus.GLDB_STATUS_DEAD);
		baseProtocol.sendCode(REQ_DATA);
		baseProtocol.sendCode(id);
		baseProtocol.sendCode(REQ_DATA_TEXTURE);
		baseProtocol.sendCode(texId);
		baseProtocol.sendCode(target);
		baseProtocol.sendCode(face);
		baseProtocol.sendCode(level);
		baseProtocol.sendCode(format);
		baseProtocol.sendCode(type);
	}

	void sendDataFramebuffer(int id, int fboId, int target,
									int buffer, int format, int type)
	{
		assert(status != DebuggerStatus.GLDB_STATUS_DEAD);
		baseProtocol.sendCode(REQ_DATA);
		baseProtocol.sendCode(id);
		baseProtocol.sendCode(REQ_DATA_FRAMEBUFFER);
		baseProtocol.sendCode(fboId);
		baseProtocol.sendCode(target);
		baseProtocol.sendCode(buffer);
		baseProtocol.sendCode(format);
		baseProtocol.sendCode(type);
	}

	void sendDataShader(int id, int shaderId, int target)
	{
		assert(status != DebuggerStatus.GLDB_STATUS_DEAD);
		baseProtocol.sendCode(REQ_DATA);
		baseProtocol.sendCode(id);
		baseProtocol.sendCode(REQ_DATA_SHADER);
		baseProtocol.sendCode(shaderId);
		baseProtocol.sendCode(target);
	}

	void sendDataInfoLog(int id, int objectId, int target)
	{
		assert(status != DebuggerStatus.GLDB_STATUS_DEAD);
		baseProtocol.sendCode(REQ_DATA);
		baseProtocol.sendCode(id);
		baseProtocol.sendCode(REQ_DATA_INFO_LOG);
		baseProtocol.sendCode(objectId);
		baseProtocol.sendCode(target);
	}

	void sendDataBuffer(int id, int objectId)
	{
		assert(status != DebuggerStatus.GLDB_STATUS_DEAD);
		baseProtocol.sendCode(REQ_DATA);
		baseProtocol.sendCode(id);
		baseProtocol.sendCode(REQ_DATA_BUFFER);
		baseProtocol.sendCode(objectId);
	}

	boolean getBreakEvent(int event)
	{
		assert(event < REQ_EVENT_COUNT);
		return breakOnEvent[event];
	}

	void setBreakEvent(int id, int event, boolean brk)
	{
		assert(event < REQ_EVENT_COUNT);
		breakOnEvent[event] = brk;
		if (status != DebuggerStatus.GLDB_STATUS_DEAD)
		{
			baseProtocol.sendCode(REQ_BREAK_EVENT);
			baseProtocol.sendCode(id);
			baseProtocol.sendCode(event);
			baseProtocol.sendCode(brk ? 1 : 0);
		}
	}

	void setBreak(int id, String function, boolean brk)
	{
		breakOn.put(function, brk ? '1' : '0');
		//bugle_hash_set(&break_on, function, brk ? "1" : "0");
		if (status != DebuggerStatus.GLDB_STATUS_DEAD)
		{
			baseProtocol.sendCode(REQ_BREAK);
			baseProtocol.sendCode(id);
			baseProtocol.sendString(function);
			baseProtocol.sendCode(brk ? 1 : 0);
		}
	}

	public State recieveStateTree() throws IOException
	{
		int resp = RESP_STATE_NODE_END;
		State state = new State();
		State child = new State();
		state.name = baseProtocol.recieveString();
		state.numericName = baseProtocol.recieveCode();
		state.enumName = baseProtocol.recieveCode();
		state.data = baseProtocol.recieveString(); // Data
		if(state.length == -2)
		{ //if(length == -2) > invalid data
			System.err.println("Length is -2, that's non ideal");
		}
		do
		{
			try
			{
				resp = baseProtocol.recieveCode();
				int id = baseProtocol.recieveCode();
				switch(resp)
				{
				case RESP_STATE_NODE_BEGIN:
					child = recieveStateTree();
					state.children.add(child);
					break;
				case RESP_STATE_NODE_END:
					break;
				default:
					System.err.println("Unexpected code in " + resp + " in state tree"); // TODO: fix formatting on resp
					break;
				}
			}
			catch(Exception e) // TODO: More specific error handling in fututre
			{
				System.err.println("Error recieving state");
			}
		} while(resp != RESP_STATE_NODE_END);
		
		return state;
	}
	
	public TimeStamp recieveTime() throws IOException
	{
		TimeStamp stamp = new TimeStamp();
		
		stamp.sec = baseProtocol.recieveCode();
		stamp.min = baseProtocol.recieveCode();
		stamp.hour = baseProtocol.recieveCode();
		stamp.mday = baseProtocol.recieveCode();
		stamp.mon = baseProtocol.recieveCode();
		stamp.year = baseProtocol.recieveCode();
		
		return stamp;
	}
	
	void setStatus(DebuggerStatus s)
	{
		if (status == s) return;
		status = s;
		switch (status)
		{
		case GLDB_STATUS_RUNNING:
		case GLDB_STATUS_STOPPED:
			// Destroy state tree
			break;
		case GLDB_STATUS_STARTED:
			break;
		case GLDB_STATUS_DEAD:
			// TODO: Close connections
			//child_pid = 0;
			break;
		}
	}
	
	public void processResponse(Response r)
	{
		switch (r.code)
		{
		case DebuggerProtocol.RESP_BREAK:
		case DebuggerProtocol.RESP_BREAK_EVENT:
			setStatus(DebuggerStatus.GLDB_STATUS_STOPPED);
			break;
		case DebuggerProtocol.RESP_RUNNING:
			setStatus(DebuggerStatus.GLDB_STATUS_RUNNING);
			break;
		case DebuggerProtocol.RESP_STATE_HEADER:
			this.state = ((ResponseStateTree)r).root;
			break;
		case DebuggerProtocol.RESP_CALL:
			// TODO: modify
			System.out.println(((ResponseCall)r).call);
			break;
		default:
			break;
		}
	}
	
	public BaseProtocol getBaseProtocol()
	{
		return baseProtocol;
	}
	
	// TODO: remove
	public State getStateRoot()
	{
		return state;
	}
	
	public void addStateTreeRecievedListener(StateTreeRecievedListener listener)
	{
		stateRecievedListeners.add(listener);
	}
	
	public void removeStateTreeRecievedListener(StateTreeRecievedListener listener)
	{
		stateRecievedListeners.remove(listener);
	}
}
