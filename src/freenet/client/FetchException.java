/* This code is part of Freenet. It is distributed under the GNU General
 * Public License, version 2 (or at your option any later version). See
 * http://www.gnu.org/ for further details of the GPL. */
package freenet.client;

import freenet.keys.FreenetURI;
import freenet.support.Logger;

/**
 * Generic exception thrown by a Fetcher. All other exceptions are converted to one of
 * these to tell the client.
 */
public class FetchException extends Exception {

	private static final long serialVersionUID = -1106716067841151962L;
	
	public final int mode;
	
	public final FreenetURI newURI;
	
	public final long expectedSize;
	
	String expectedMimeType;
	
	boolean finalizedSizeAndMimeType;
	
	public String getExpectedMimeType() {
		return expectedMimeType;
	}

	public boolean finalizedSize() {
		return finalizedSizeAndMimeType;
	}
	
	/** For collection errors */
	public final FailureCodeTracker errorCodes;
	
	public final String extraMessage;
	
	/** Get the failure mode. */
	public int getMode() {
		return mode;
	}
	
	public FetchException(int m) {
		super(getMessage(m));
		extraMessage = null;
		mode = m;
		errorCodes = null;
		newURI = null;
		expectedSize = -1;
		if(Logger.shouldLog(Logger.MINOR, this)) 
			Logger.minor(this, "FetchException("+getMessage(mode)+ ')', this);
	}

	public FetchException(int m, long expectedSize, boolean finalizedSize, String expectedMimeType) {
		super(getMessage(m));
		extraMessage = null;
		this.finalizedSizeAndMimeType = finalizedSize;
		mode = m;
		errorCodes = null;
		newURI = null;
		this.expectedSize = expectedSize;
		this.expectedMimeType = expectedMimeType;
		if(Logger.shouldLog(Logger.MINOR, this)) 
			Logger.minor(this, "FetchException("+getMessage(mode)+ ')', this);
	}
	
	public FetchException(int m, long expectedSize, boolean finalizedSize, String expectedMimeType, FreenetURI uri) {
		super(getMessage(m));
		extraMessage = null;
		this.finalizedSizeAndMimeType = finalizedSize;
		mode = m;
		errorCodes = null;
		newURI = uri;
		this.expectedSize = expectedSize;
		this.expectedMimeType = expectedMimeType;
		if(Logger.shouldLog(Logger.MINOR, this)) 
			Logger.minor(this, "FetchException("+getMessage(mode)+ ')', this);
	}
	
	public FetchException(MetadataParseException e) {
		super(getMessage(INVALID_METADATA)+": "+e.getMessage());
		extraMessage = e.getMessage();
		mode = INVALID_METADATA;
		errorCodes = null;
		initCause(e);
		newURI = null;
		expectedSize = -1;
		if(Logger.shouldLog(Logger.MINOR, this))
			Logger.minor(this, "FetchException("+getMessage(mode)+"): "+e,e);
	}

	public FetchException(ArchiveFailureException e) {
		super(getMessage(ARCHIVE_FAILURE)+": "+e.getMessage());
		extraMessage = e.getMessage();
		mode = ARCHIVE_FAILURE;
		errorCodes = null;
		newURI = null;
		initCause(e);
		expectedSize = -1;
		if(Logger.shouldLog(Logger.MINOR, this))
			Logger.minor(this, "FetchException("+getMessage(mode)+"): "+e,e);
	}

	public FetchException(ArchiveRestartException e) {
		super(getMessage(ARCHIVE_RESTART)+": "+e.getMessage());
		extraMessage = e.getMessage();
		mode = ARCHIVE_FAILURE;
		errorCodes = null;
		initCause(e);
		newURI = null;
		expectedSize = -1;
		if(Logger.shouldLog(Logger.MINOR, this))
			Logger.minor(this, "FetchException("+getMessage(mode)+"): "+e,e);	}

	public FetchException(int mode, Throwable t) {
		super(getMessage(mode)+": "+t.getMessage());
		extraMessage = t.getMessage();
		this.mode = mode;
		errorCodes = null;
		initCause(t);
		newURI = null;
		expectedSize = -1;
		if(Logger.shouldLog(Logger.MINOR, this))
			Logger.minor(this, "FetchException("+getMessage(mode)+"): "+t.getMessage(),t);
	}

	public FetchException(int mode, FailureCodeTracker errorCodes) {
		super(getMessage(mode));
		extraMessage = null;
		this.mode = mode;
		this.errorCodes = errorCodes;
		newURI = null;
		expectedSize = -1;
		if(Logger.shouldLog(Logger.MINOR, this))
			Logger.minor(this, "FetchException("+getMessage(mode)+ ')');
	}
	
	public FetchException(int mode, String msg) {
		super(getMessage(mode)+": "+msg);
		extraMessage = msg;
		errorCodes = null;
		this.mode = mode;
		newURI = null;
		expectedSize = -1;
		if(Logger.shouldLog(Logger.MINOR, this))
			Logger.minor(this, "FetchException("+getMessage(mode)+"): "+msg,this);
	}

	public FetchException(int mode, FreenetURI newURI) {
		super(getMessage(mode));
		extraMessage = null;
		this.mode = mode;
		errorCodes = null;
		this.newURI = newURI;
		expectedSize = -1;
		if(Logger.shouldLog(Logger.MINOR, this))
			Logger.minor(this, "FetchException("+getMessage(mode)+") -> "+newURI, this);
	}
	
	public FetchException(int mode, String msg, FreenetURI uri) {
		super(getMessage(mode)+": "+msg);
		extraMessage = msg;
		errorCodes = null;
		this.mode = mode;
		newURI = uri;
		expectedSize = -1;
		if(Logger.shouldLog(Logger.MINOR, this))
			Logger.minor(this, "FetchException("+getMessage(mode)+"): "+msg,this);
	}

	public static String getShortMessage(int mode) {
		switch(mode) {
		case TOO_DEEP_ARCHIVE_RECURSION:
			return "Too deep archive recursion";
		case UNKNOWN_SPLITFILE_METADATA:
			return "Unknown splitfile metadata";
		case TOO_MANY_REDIRECTS:
			return "Too many redirects";
		case UNKNOWN_METADATA:
			return "Unknown metadata";
		case INVALID_METADATA:
			return "Invalid metadata";
		case ARCHIVE_FAILURE:
			return "Archive failure";
		case BLOCK_DECODE_ERROR:
			return "Block decode error";
		case TOO_MANY_METADATA_LEVELS:
			return "Too many metadata levels";
		case TOO_MANY_ARCHIVE_RESTARTS:
			return "Too many archive restarts";
		case TOO_MUCH_RECURSION:
			return "Too much recursion";
		case NOT_IN_ARCHIVE:
			return "Not in archive";
		case TOO_MANY_PATH_COMPONENTS:
			return "Too many path components";
		case BUCKET_ERROR:
			return "Temporary files error";
		case DATA_NOT_FOUND:
			return "Data not found";
		case ROUTE_NOT_FOUND:
			return "Route not found";
		case REJECTED_OVERLOAD:
			return "Timeout or overload";
		case INTERNAL_ERROR:
			return "Internal error";
		case TRANSFER_FAILED:
			return "Transfer failed";
		case SPLITFILE_ERROR:
			return "Splitfile error";
		case INVALID_URI:
			return "Invalid URI";
		case TOO_BIG:
			return "Too big";
		case TOO_BIG_METADATA:
			return "Metadata too big";
		case TOO_MANY_BLOCKS_PER_SEGMENT:
			return "Too many blocks per segment";
		case NOT_ENOUGH_PATH_COMPONENTS:
			return "Not enough meta-strings"; // FIXME better description
		case CANCELLED:
			return "Cancelled";
		case ARCHIVE_RESTART:
			return "Archive restarted";
		case PERMANENT_REDIRECT:
			return "New URI";
		default:
			return "Unknown code "+mode;
		}
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer(200);
		sb.append("FetchException:");
		sb.append(getShortMessage(mode));
		sb.append(':');
		sb.append(newURI);
		sb.append(':');
		sb.append(expectedSize);
		sb.append(':');
		sb.append(expectedMimeType);
		sb.append(':');
		sb.append(finalizedSizeAndMimeType);
		sb.append(':');
		sb.append(errorCodes);
		sb.append(':');
		sb.append(extraMessage);
		return sb.toString();
	}
	
	public static String getMessage(int mode) {
		switch(mode) {
		case TOO_DEEP_ARCHIVE_RECURSION:
			return "Too many levels of recursion into archives";
		case UNKNOWN_SPLITFILE_METADATA:
			return "Don't know what to do with splitfile";
		case TOO_MANY_REDIRECTS:
			return "Too many redirects - loop?";
		case UNKNOWN_METADATA:
			return "Don't know what to do with metadata";
		case INVALID_METADATA:
			return "Failed to parse metadata";
		case ARCHIVE_FAILURE:
			return "Failure in extracting files from an archive";
		case BLOCK_DECODE_ERROR:
			return "Failed to decode a block";
		case TOO_MANY_METADATA_LEVELS:
			return "Too many levels of split metadata";
		case TOO_MANY_ARCHIVE_RESTARTS:
			return "Request was restarted too many times due to archives changing";
		case TOO_MUCH_RECURSION:
			return "Too many redirects (too much recursion)"; // FIXME: ???
		case NOT_IN_ARCHIVE:
			return "File not in archive";
		case TOO_MANY_PATH_COMPONENTS:
			return "Too many path components - not a manifest? Try removing one";
		case BUCKET_ERROR:
			return "Internal temp files error, maybe disk full or permissions problem?";
		case DATA_NOT_FOUND:
			return "Data not found";
		case ROUTE_NOT_FOUND:
			return "Route not found - could not find enough nodes to be sure the data doesn't exist";
		case REJECTED_OVERLOAD:
			return "A node was overloaded or timed out";
		case INTERNAL_ERROR:
			return "Internal error, probably a bug";
		case TRANSFER_FAILED:
			return "Found the file, but lost it while receiving the data";
		case SPLITFILE_ERROR:
			return "Splitfile error";
		case INVALID_URI:
			return "Invalid URI";
		case TOO_BIG:
			return "Too big";
		case TOO_BIG_METADATA:
			return "Metadata too big";
		case TOO_MANY_BLOCKS_PER_SEGMENT:
			return "Too many blocks per segment";
		case NOT_ENOUGH_PATH_COMPONENTS:
			return "Give more metastrings (path components) in URI";
			// FIXME better description for above
		case CANCELLED:
			return "Cancelled by caller";
		case ARCHIVE_RESTART:
			return "Archive restarted";
		case PERMANENT_REDIRECT:
			return "Permanent redirect: use the new URI";
		default:
			return "Unknown fetch error code: "+mode;
		}
	}
	
	// FIXME many of these are not used any more
	
	/** Too many levels of recursion into archives */
	public static final int TOO_DEEP_ARCHIVE_RECURSION = 1;
	/** Don't know what to do with splitfile */
	public static final int UNKNOWN_SPLITFILE_METADATA = 2;
	/** Too many redirects */
	public static final int TOO_MANY_REDIRECTS = 16;
	/** Don't know what to do with metadata */
	public static final int UNKNOWN_METADATA = 3;
	/** Got a MetadataParseException */
	public static final int INVALID_METADATA = 4;
	/** Got an ArchiveFailureException */
	public static final int ARCHIVE_FAILURE = 5;
	/** Failed to decode a block */
	public static final int BLOCK_DECODE_ERROR = 6;
	/** Too many split metadata levels */
	public static final int TOO_MANY_METADATA_LEVELS = 7;
	/** Too many archive restarts */
	public static final int TOO_MANY_ARCHIVE_RESTARTS = 8;
	/** Too deep recursion */
	public static final int TOO_MUCH_RECURSION = 9;
	/** Tried to access an archive file but not in an archive */
	public static final int NOT_IN_ARCHIVE = 10;
	/** Too many meta strings. E.g. requesting CHK@blah,blah,blah as CHK@blah,blah,blah/filename.ext */
	public static final int TOO_MANY_PATH_COMPONENTS = 11;
	/** Failed to read from or write to a bucket; a kind of internal error */
	public static final int BUCKET_ERROR = 12;
	/** Data not found */
	public static final int DATA_NOT_FOUND = 13;
	/** Route not found */
	public static final int ROUTE_NOT_FOUND = 14;
	/** Downstream overload */
	public static final int REJECTED_OVERLOAD = 15;
	/** An internal error occurred */
	public static final int INTERNAL_ERROR = 17;
	/** The node found the data but the transfer failed */
	public static final int TRANSFER_FAILED = 18;
	/** Splitfile error. This should be a SplitFetchException. */
	public static final int SPLITFILE_ERROR = 19;
	/** Invalid URI. */
	public static final int INVALID_URI = 20;
	/** Too big */
	public static final int TOO_BIG = 21;
	/** Metadata too big */
	public static final int TOO_BIG_METADATA = 22;
	/** Splitfile has too big segments */
	public static final int TOO_MANY_BLOCKS_PER_SEGMENT = 23;
	/** Not enough meta strings in URI given and no default document */
	public static final int NOT_ENOUGH_PATH_COMPONENTS = 24;
	/** Explicitly cancelled */
	public static final int CANCELLED = 25;
	/** Archive restart */
	public static final int ARCHIVE_RESTART = 26;
	/** There is a more recent version of the USK, ~= HTTP 301; FProxy will turn this into a 301 */
	public static final int PERMANENT_REDIRECT = 27;

	/** Is an error fatal i.e. is there no point retrying? */
	public boolean isFatal() {
		return isFatal(mode);
	}

	public static boolean isFatal(int mode) {
		switch(mode) {
		// Problems with the data as inserted, or the URI given. No point retrying.
		case ARCHIVE_FAILURE:
		case BLOCK_DECODE_ERROR:
		case TOO_MANY_PATH_COMPONENTS:
		case NOT_ENOUGH_PATH_COMPONENTS:
		case INVALID_METADATA:
		case NOT_IN_ARCHIVE:
		case TOO_DEEP_ARCHIVE_RECURSION:
		case TOO_MANY_ARCHIVE_RESTARTS:
		case TOO_MANY_METADATA_LEVELS:
		case TOO_MANY_REDIRECTS:
		case TOO_MUCH_RECURSION:
		case UNKNOWN_METADATA:
		case UNKNOWN_SPLITFILE_METADATA:
		case INVALID_URI:
		case TOO_BIG:
			return true;

		// Low level errors, can be retried
		case DATA_NOT_FOUND:
		case ROUTE_NOT_FOUND:
		case REJECTED_OVERLOAD:
		case TRANSFER_FAILED:
			return false;
			
		case BUCKET_ERROR:
		case INTERNAL_ERROR:
			// Maybe fatal
			return false;
			
		case SPLITFILE_ERROR:
			// Fatal, because there are internal retries
			return true;
			
			// Wierd ones
		case CANCELLED:
		case ARCHIVE_RESTART:
			// Fatal
			return true;
			
		default:
			Logger.error(FetchException.class, "Do not know if error code is fatal: "+getMessage(mode));
			return false; // assume it isn't
		}
	}
}
