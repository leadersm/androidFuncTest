package com.bea.xml.stream;
public class XmlPullParserException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3614579343866398020L;
	protected Throwable detail;
	protected int row = -1;
	protected int column = -1;

	/*
	 * public XmlPullParserException() { }
	 */

	public XmlPullParserException(String s) {
		super(s);
	}

	/*
	 * public XmlPullParserException(String s, Throwable thrwble) { super(s);
	 * this.detail = thrwble; }
	 * 
	 * public XmlPullParserException(String s, int row, int column) { super(s);
	 * this.row = row; this.column = column; }
	 */

	public XmlPullParserException(String msg, XmlPullParser parser,
			Throwable chain) {
		super((msg == null ? "" : msg + " ")
				+ (parser == null ? "" : "(position:"
						+ parser.getPositionDescription() + ") ")
				+ (chain == null ? "" : "caused by: " + chain));

		if (parser != null) {
			row = parser.getLineNumber();
			column = parser.getColumnNumber();
		}
		detail = chain;
	}

	public int getColumnNumber() {
		return column;
	}

	public Throwable getDetail() {
		return detail;
	}

	// public void setDetail(Throwable cause) { this.detail = cause; }
	public int getLineNumber() {
		return row;
	}

	/*
	 * public String getMessage() { if(detail == null) return
	 * super.getMessage(); else return super.getMessage() +
	 * "; nested exception is: \n\t" + detail.getMessage(); }
	 */

	// NOTE: code that prints this and detail is difficult in J2ME
	@Override
	public void printStackTrace() {
		if (detail == null) {
			super.printStackTrace();
		} else {
			synchronized (System.err) {
				System.err.println(super.getMessage()
						+ "; nested exception is:");
				detail.printStackTrace();
			}
		}
	}

}