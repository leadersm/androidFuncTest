package com.bea.xml.stream;
import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.independentsoft.xml.stream.EventFilter;
import com.independentsoft.xml.stream.EventFilterSupport;
import com.independentsoft.xml.stream.PropertyManager;
import com.independentsoft.xml.stream.StreamFilter;
import com.independentsoft.xml.stream.XMLEventReader;
import com.independentsoft.xml.stream.XMLEventReaderImpl;
import com.independentsoft.xml.stream.XMLInputFactory;
import com.independentsoft.xml.stream.XMLReaderImpl;
import com.independentsoft.xml.stream.XMLReporter;
import com.independentsoft.xml.stream.XMLResolver;
import com.independentsoft.xml.stream.XMLStreamException;
import com.independentsoft.xml.stream.XMLStreamFilterImpl;
import com.independentsoft.xml.stream.XMLStreamReader;
import com.independentsoft.xml.stream.util.XMLEventAllocator;
import com.independentsoft.xml.stream.xerces.xni.parser.XMLInputSource;
import com.independentsoft.xml.transform.Source;
import com.independentsoft.xml.transform.stream.StreamSource;

/**
 * This class is used to create implementations of XML Pull Parser defined in
 * XMPULL V1 API. The name of actual factory class will be determined based on
 * several parameters. It works similar to JAXP but tailored to work in J2ME
 * environments (no access to system properties or file system) so name of
 * parser class factory to use and its class used for loading (no class loader -
 * on J2ME no access to context class loaders) must be passed explicitly. If no
 * name of parser factory was passed (or is null) it will try to find name by
 * searching in CLASSPATH for
 * META-INF/services/org.xmlpull.v1.XmlPullParserFactory resource that should
 * contain a comma separated list of class names of factories or parsers to try
 * (in order from left to the right). If none found, it will throw an exception.
 * 
 * <br />
 * <strong>NOTE:</strong>In J2SE or J2EE environments, you may want to use
 * <code>newInstance(property, classLoaderCtx)</code> where first argument is
 * <code>System.getProperty(XmlPullParserFactory.PROPERTY_NAME)</code> and
 * second is <code>Thread.getContextClassLoader().getClass()</code> .
 * 
 * @see XmlPullParser
 * 
 * @author <a href="http://www.extreme.indiana.edu/~aslom/">Aleksander
 *         Slominski</a>
 * @author Stefan Haustein
 */

public class XmlPullParserFactory extends XMLInputFactory{
	/** used as default class to server as context class in newInstance() */
	final static Class<? extends XmlPullParserFactory> referenceContextClass;

	static {
		final XmlPullParserFactory f = new XmlPullParserFactory();
		referenceContextClass = f.getClass();
	}

	/**
	 * Name of the system or midlet property that should be used for a system
	 * property containing a comma separated list of factory or parser class
	 * names (value: org.xmlpull.v1.XmlPullParserFactory).
	 */

	public static final String PROPERTY_NAME = "org.xmlpull.v1.XmlPullParserFactory";

	private static final String RESOURCE_NAME = "/META-INF/services/"
			+ PROPERTY_NAME;

	// public static final String DEFAULT_PROPERTY =
	// "org.xmlpull.xpp3.XmlPullParser,org.kxml2.io.KXmlParser";

	/**
	 * Create a new instance of a PullParserFactory that can be used to create
	 * XML pull parsers (see class description for more details).
	 * 
	 * @return a new instance of a PullParserFactory, as returned by newInstance
	 *         (null, null);
	 */
	public static XmlPullParserFactory newInstance(String classNames,
			Class<? extends XmlPullParserFactory> context)
			throws XmlPullParserException {

		if (context == null) {
			// NOTE: make sure context uses the same class loader as API classes
			// this is the best we can do without having access to context
			// classloader in J2ME
			// if API is in the same classloader as implementation then this
			// will work
			context = referenceContextClass;
		}

		String classNamesLocation = null;

		if (classNames == null || classNames.length() == 0
				|| "DEFAULT".equals(classNames)) {
			try {
				final InputStream is = context
						.getResourceAsStream(RESOURCE_NAME);

				if (is == null) {
					throw new XmlPullParserException(
							"resource not found: "
									+ RESOURCE_NAME
									+ " make sure that parser implementing XmlPull API is available");
				}
				final StringBuffer sb = new StringBuffer();

				while (true) {
					final int ch = is.read();
					if (ch < 0) {
						break;
					} else if (ch > ' ') {
						sb.append((char) ch);
					}
				}
				is.close();

				classNames = sb.toString();
			} catch (final Exception e) {
				throw new XmlPullParserException(null, null, e);
			}
			classNamesLocation = "resource " + RESOURCE_NAME
					+ " that contained '" + classNames + "'";
		} else {
			classNamesLocation = "parameter classNames to newInstance() that contained '"
					+ classNames + "'";
		}

		XmlPullParserFactory factory = null;
		final Vector<Class<?>> parserClasses = new Vector<Class<?>>();
		final Vector<Class<?>> serializerClasses = new Vector<Class<?>>();
		int pos = 0;

		while (pos < classNames.length()) {
			int cut = classNames.indexOf(',', pos);

			if (cut == -1) {
				cut = classNames.length();
			}
			final String name = classNames.substring(pos, cut);

			Class<?> candidate = null;
			Object instance = null;

			try {
				candidate = Class.forName(name);
				// necessary because of J2ME .class issue
				instance = candidate.newInstance();
			} catch (final Exception e) {
			}

			if (candidate != null) {
				boolean recognized = false;
				if (instance instanceof XmlPullParser) {
					parserClasses.addElement(candidate);
					recognized = true;
				}
				if (instance instanceof XmlSerializer) {
					serializerClasses.addElement(candidate);
					recognized = true;
				}
				if (instance instanceof XmlPullParserFactory) {
					if (factory == null) {
						factory = (XmlPullParserFactory) instance;
					}
					recognized = true;
				}
				if (!recognized) {
					throw new XmlPullParserException("incompatible class: "
							+ name);
				}
			}
			pos = cut + 1;
		}

		if (factory == null) {
			factory = new XmlPullParserFactory();
		}
		factory.parserClasses = parserClasses;
		factory.serializerClasses = serializerClasses;
		factory.classNamesLocation = classNamesLocation;
		return factory;
	}

	protected Vector<Class<?>> parserClasses;

	protected String classNamesLocation;

	protected Vector<Class<?>> serializerClasses;

	// features are kept there
	protected Hashtable<String, Boolean> features = new Hashtable<String, Boolean>();

	/**
	 * Protected constructor to be called by factory implementations.
	 */

	protected XmlPullParserFactory() {
	}

	/**
	 * Return the current value of the feature with given name.
	 * <p>
	 * <b>NOTE:</b> factory features are not used for XML Serializer.
	 * 
	 * @param name
	 *            The name of feature to be retrieved.
	 * @return The value of named feature. Unknown features are
	 *         <string>always</strong> returned as false
	 */

	public boolean getFeature(String name) {
		final Boolean value = features.get(name);
		return value != null ? value.booleanValue() : false;
	}

	/**
	 * Indicates whether or not the factory is configured to produce parsers
	 * which are namespace aware (it simply set feature
	 * XmlPullParser.FEATURE_PROCESS_NAMESPACES to true or false).
	 * 
	 * @return true if the factory is configured to produce parsers which are
	 *         namespace aware; false otherwise.
	 */

	public boolean isNamespaceAware() {
		return getFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES);
	}

	/**
	 * Indicates whether or not the factory is configured to produce parsers
	 * which validate the XML content during parse.
	 * 
	 * @return true if the factory is configured to produce parsers which
	 *         validate the XML content during parse; false otherwise.
	 */

	public boolean isValidating() {
		return getFeature(XmlPullParser.FEATURE_VALIDATION);
	}

	/**
	 * Creates a new instance of a XML Pull Parser using the currently
	 * configured factory features.
	 * 
	 * @return A new instance of a XML Pull Parser.
	 * @throws XmlPullParserException
	 *             if a parser cannot be created which satisfies the requested
	 *             configuration.
	 */

	public XmlPullParser newPullParser() throws XmlPullParserException {

		if (parserClasses == null) {
			throw new XmlPullParserException(
					"Factory initialization was incomplete - has not tried "
							+ classNamesLocation);
		}

		if (parserClasses.size() == 0) {
			throw new XmlPullParserException(
					"No valid parser classes found in " + classNamesLocation);
		}

		final StringBuffer issues = new StringBuffer();

		for (int i = 0; i < parserClasses.size(); i++) {
			final Class<?> ppClass = parserClasses.elementAt(i);
			try {
				final XmlPullParser pp = (XmlPullParser) ppClass.newInstance();
				// if( ! features.isEmpty() ) {
				// Enumeration keys = features.keys();
				// while(keys.hasMoreElements()) {

				for (final Enumeration<String> e = features.keys(); e
						.hasMoreElements();) {
					final String key = e.nextElement();
					final Boolean value = features.get(key);
					if (value != null && value.booleanValue()) {
						pp.setFeature(key, true);
					}
				}
				return pp;

			} catch (final Exception ex) {
				issues.append(ppClass.getName() + ": " + ex.toString() + "; ");
			}
		}

		throw new XmlPullParserException("could not create parser: " + issues);
	}

	/**
	 * Creates a new instance of a XML Serializer.
	 * 
	 * <p>
	 * <b>NOTE:</b> factory features are not used for XML Serializer.
	 * 
	 * @return A new instance of a XML Serializer.
	 * @throws XmlPullParserException
	 *             if a parser cannot be created which satisfies the requested
	 *             configuration.
	 */

	public XmlSerializer newSerializer() throws XmlPullParserException {

		if (serializerClasses == null) {
			throw new XmlPullParserException(
					"Factory initialization incomplete - has not tried "
							+ classNamesLocation);
		}
		if (serializerClasses.size() == 0) {
			throw new XmlPullParserException(
					"No valid serializer classes found in "
							+ classNamesLocation);
		}

		final StringBuffer issues = new StringBuffer();

		for (int i = 0; i < serializerClasses.size(); i++) {
			final Class<?> ppClass = serializerClasses.elementAt(i);
			try {
				final XmlSerializer ser = (XmlSerializer) ppClass.newInstance();

				// for (Enumeration e = features.keys (); e.hasMoreElements ();)
				// {
				// String key = (String) e.nextElement();
				// Boolean value = (Boolean) features.get(key);
				// if(value != null && value.booleanValue()) {
				// ser.setFeature(key, true);
				// }
				// }
				return ser;

			} catch (final Exception ex) {
				issues.append(ppClass.getName() + ": " + ex.toString() + "; ");
			}
		}

		throw new XmlPullParserException("could not create serializer: "
				+ issues);
	}

	/**
	 * Set the features to be set when XML Pull Parser is created by this
	 * factory.
	 * <p>
	 * <b>NOTE:</b> factory features are not used for XML Serializer.
	 * 
	 * @param name
	 *            string with URI identifying feature
	 * @param state
	 *            if true feature will be set; if false will be ignored
	 */

	public void setFeature(String name, boolean state)
			throws XmlPullParserException {

		features.put(name, new Boolean(state));
	}

	/**
	 * Specifies that the parser produced by this factory will provide support
	 * for XML namespaces. By default the value of this is set to false.
	 * 
	 * @param awareness
	 *            true if the parser produced by this code will provide support
	 *            for XML namespaces; false otherwise.
	 */

	public void setNamespaceAware(boolean awareness) {
		features.put(XmlPullParser.FEATURE_PROCESS_NAMESPACES, new Boolean(
				awareness));
	}

	/**
	 * Specifies that the parser produced by this factory will be validating (it
	 * simply set feature XmlPullParser.FEATURE_VALIDATION to true or false).
	 * 
	 * By default the value of this is set to false.
	 * 
	 * @param validating
	 *            - if true the parsers created by this factory must be
	 *            validating.
	 */

	public void setValidating(boolean validating) {
		features.put(XmlPullParser.FEATURE_VALIDATION, new Boolean(validating));
	}

	  private PropertyManager fPropertyManager = new PropertyManager(1);
	  private static final boolean DEBUG = false;
	  private XMLReaderImpl fTempReader = null;

	  boolean fPropertyChanged = false;

	  boolean fReuseInstance = true;

	  void initEventReader()
	  {
	    this.fPropertyChanged = true;
	  }

	  public XMLEventReader createXMLEventReader(InputStream inputstream)
	    throws XMLStreamException
	  {
	    initEventReader();

	    return new XMLEventReaderImpl(createXMLStreamReader(inputstream));
	  }

	  public XMLEventReader createXMLEventReader(Reader reader) throws XMLStreamException {
	    initEventReader();

	    return new XMLEventReaderImpl(createXMLStreamReader(reader));
	  }

	  public XMLEventReader createXMLEventReader(Source source) throws XMLStreamException {
	    initEventReader();

	    return new XMLEventReaderImpl(createXMLStreamReader(source));
	  }

	  public XMLEventReader createXMLEventReader(String systemId, InputStream inputstream) throws XMLStreamException {
	    initEventReader();

	    return new XMLEventReaderImpl(createXMLStreamReader(systemId, inputstream));
	  }

	  public XMLEventReader createXMLEventReader(InputStream stream, String encoding) throws XMLStreamException {
	    initEventReader();

	    return new XMLEventReaderImpl(createXMLStreamReader(stream, encoding));
	  }

	  public XMLEventReader createXMLEventReader(String systemId, Reader reader) throws XMLStreamException {
	    initEventReader();

	    return new XMLEventReaderImpl(createXMLStreamReader(systemId, reader));
	  }

	  public XMLEventReader createXMLEventReader(XMLStreamReader reader)
	    throws XMLStreamException
	  {
	    initEventReader();

	    return new XMLEventReaderImpl(reader);
	  }

	  public XMLStreamReader createXMLStreamReader(Reader reader) throws XMLStreamException {
	    return createXMLStreamReader(null, reader);
	  }

	  public XMLStreamReader createXMLStreamReader(String systemId, Reader reader) throws XMLStreamException {
	    XMLInputSource inputSource = new XMLInputSource(null, systemId, null, reader, null);
	    return getXMLStreamReaderImpl(inputSource);
	  }

	  public XMLStreamReader createXMLStreamReader(Source source) throws XMLStreamException {
	    return new XMLReaderImpl(jaxpSourcetoXMLInputSource(source), new PropertyManager(this.fPropertyManager));
	  }

	  public XMLStreamReader createXMLStreamReader(InputStream inputStream) throws XMLStreamException
	  {
	    return createXMLStreamReader(null, inputStream, null);
	  }

	  public XMLStreamReader createXMLStreamReader(String systemId, InputStream inputStream) throws XMLStreamException {
	    return createXMLStreamReader(systemId, inputStream, null);
	  }

	  public XMLStreamReader createXMLStreamReader(InputStream inputStream, String encoding) throws XMLStreamException
	  {
	    return createXMLStreamReader(null, inputStream, encoding);
	  }

	  public XMLStreamReader createXMLStreamReader(String systemId, InputStream inputStream, String encoding)
	    throws XMLStreamException
	  {
	    XMLInputSource inputSource = new XMLInputSource(null, systemId, null, inputStream, encoding);
	    return getXMLStreamReaderImpl(inputSource);
	  }

	  public XMLEventAllocator getEventAllocator() {
	    return (XMLEventAllocator)getProperty("com.independentsoft.xml.stream.allocator");
	  }

	  public XMLReporter getXMLReporter() {
	    return (XMLReporter)this.fPropertyManager.getProperty("com.independentsoft.xml.stream.reporter");
	  }

	  public XMLResolver getXMLResolver() {
	    Object object = this.fPropertyManager.getProperty("com.independentsoft.xml.stream.resolver");
	    return (XMLResolver)object;
	  }

	  public void setXMLReporter(XMLReporter xmlreporter)
	  {
	    this.fPropertyManager.setProperty("com.independentsoft.xml.stream.reporter", xmlreporter);
	  }

	  public void setXMLResolver(XMLResolver xmlresolver) {
	    this.fPropertyManager.setProperty("com.independentsoft.xml.stream.resolver", xmlresolver);
	  }

	  public XMLEventReader createFilteredReader(XMLEventReader reader, EventFilter filter)
	    throws XMLStreamException
	  {
	    return new EventFilterSupport(reader, filter);
	  }

	  public XMLStreamReader createFilteredReader(XMLStreamReader reader, StreamFilter filter)
	    throws XMLStreamException
	  {
	    if ((reader != null) && (filter != null)) {
	      return new XMLStreamFilterImpl(reader, filter);
	    }
	    return null;
	  }

	  public Object getProperty(String name)
	    throws IllegalArgumentException
	  {
	    if (name == null) {
	      throw new IllegalArgumentException("Property not supported");
	    }
	    if (this.fPropertyManager.containsProperty(name))
	      return this.fPropertyManager.getProperty(name);
	    throw new IllegalArgumentException("Property not supported");
	  }

	  public boolean isPropertySupported(String name)
	  {
	    if (name == null) {
	      return false;
	    }
	    return this.fPropertyManager.containsProperty(name);
	  }

	  public void setEventAllocator(XMLEventAllocator allocator)
	  {
	    this.fPropertyManager.setProperty("com.independentsoft.xml.stream.allocator", allocator);
	  }

	  public void setProperty(String name, Object value)
	    throws IllegalArgumentException
	  {
	    if ((name == null) || (value == null) || (!this.fPropertyManager.containsProperty(name))) {
	      throw new IllegalArgumentException("Property " + name + " is not supported");
	    }
	    if ((name == "reuse-instance") || (name.equals("reuse-instance"))) {
	      this.fReuseInstance = ((Boolean)value).booleanValue();
	    }
	    else
	    {
	      this.fPropertyChanged = true;
	    }

	    this.fPropertyManager.setProperty(name, value);
	  }

	  XMLStreamReader getXMLStreamReaderImpl(XMLInputSource inputSource) throws XMLStreamException
	  {
	    if (this.fTempReader == null) {
	      this.fPropertyChanged = false;

	      return this.fTempReader = new XMLReaderImpl(inputSource, new PropertyManager(this.fPropertyManager));
	    }

	    if ((this.fReuseInstance) && (this.fTempReader.canReuse()) && (!this.fPropertyChanged))
	    {
	      this.fTempReader.reset();
	      this.fTempReader.setInputSource(inputSource);
	      this.fPropertyChanged = false;
	      return this.fTempReader;
	    }
	    this.fPropertyChanged = false;

	    return this.fTempReader = new XMLReaderImpl(inputSource, new PropertyManager(this.fPropertyManager));
	  }

	  XMLInputSource jaxpSourcetoXMLInputSource(Source source)
	  {
	    if (source instanceof StreamSource) {
	      StreamSource stSource = (StreamSource)source;
	      String systemId = stSource.getSystemId();
	      String publicId = stSource.getPublicId();
	      InputStream istream = stSource.getInputStream();
	      Reader reader = stSource.getReader();

	      if (istream != null) {
	        return new XMLInputSource(publicId, systemId, null, istream, null);
	      }
	      if (reader != null) {
	        return new XMLInputSource(publicId, systemId, null, reader, null);
	      }
	      return new XMLInputSource(publicId, systemId, null);
	    }
	    if (source instanceof Source) {
	      return new XMLInputSource(null, source.getSystemId(), null);
	    }

	    throw new UnsupportedOperationException(source.getClass().getName() + " type is not supported");
	  }
}