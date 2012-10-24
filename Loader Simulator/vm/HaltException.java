package vm;

/**
 * Creates a new exception, by extending Exception, which should be thrown when
 * execution of the Wi12 Machine needs to stop.
 * 
 * @author Dragon Slayer
 */
public class HaltException extends Exception {
	//exception
	String trace;
	
	public void setTrace(String trace) {
		this.trace = trace;
	}

	public String getTrace() {
		return trace;
	}

	private static final long serialVersionUID = -6017087318017069593L;

}
