package com.amiabledata.golferview;

/*
 * An exception for when an array parameter has wrong length.
 */
@SuppressWarnings("serial")
class InvalidArrayLengthException extends RuntimeException {
	public InvalidArrayLengthException() {
		super("Array length does not conform to the specification.");
	}
}
