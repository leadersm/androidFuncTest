package com.independentsoft.xml.stream;

import com.independentsoft.xml.stream.util.XMLEventAllocator;
import com.independentsoft.xml.transform.Source;
import java.io.InputStream;
import java.io.Reader;

public abstract class XMLInputFactory
{
  public static final String IS_NAMESPACE_AWARE = "com.independentsoft.xml.stream.isNamespaceAware";
  public static final String IS_VALIDATING = "com.independentsoft.xml.stream.isValidating";
  public static final String IS_COALESCING = "com.independentsoft.xml.stream.isCoalescing";
  public static final String IS_REPLACING_ENTITY_REFERENCES = "com.independentsoft.xml.stream.isReplacingEntityReferences";
  public static final String IS_SUPPORTING_EXTERNAL_ENTITIES = "com.independentsoft.xml.stream.isSupportingExternalEntities";
  public static final String SUPPORT_DTD = "com.independentsoft.xml.stream.supportDTD";
  public static final String REPORTER = "com.independentsoft.xml.stream.reporter";
  public static final String RESOLVER = "com.independentsoft.xml.stream.resolver";
  public static final String ALLOCATOR = "com.independentsoft.xml.stream.allocator";

  public static XMLInputFactory newInstance()
    throws FactoryConfigurationError
  {
    return (XMLInputFactory)FactoryFinder.find("com.independentsoft.xml.stream.XMLInputFactory", "com.bea.xml.stream.MXParserFactory");
  }

  public static XMLInputFactory newInstance(String factoryId, ClassLoader classLoader)
    throws FactoryConfigurationError
  {
    return (XMLInputFactory)FactoryFinder.find(factoryId, "com.bea.xml.stream.MXParserFactory", classLoader);
  }

  public abstract XMLStreamReader createXMLStreamReader(Reader paramReader)
    throws XMLStreamException;

  public abstract XMLStreamReader createXMLStreamReader(Source paramSource)
    throws XMLStreamException;

  public abstract XMLStreamReader createXMLStreamReader(InputStream paramInputStream)
    throws XMLStreamException;

  public abstract XMLStreamReader createXMLStreamReader(InputStream paramInputStream, String paramString)
    throws XMLStreamException;

  public abstract XMLStreamReader createXMLStreamReader(String paramString, InputStream paramInputStream)
    throws XMLStreamException;

  public abstract XMLStreamReader createXMLStreamReader(String paramString, Reader paramReader)
    throws XMLStreamException;

  public abstract XMLEventReader createXMLEventReader(Reader paramReader)
    throws XMLStreamException;

  public abstract XMLEventReader createXMLEventReader(String paramString, Reader paramReader)
    throws XMLStreamException;

  public abstract XMLEventReader createXMLEventReader(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException;

  public abstract XMLEventReader createXMLEventReader(Source paramSource)
    throws XMLStreamException;

  public abstract XMLEventReader createXMLEventReader(InputStream paramInputStream)
    throws XMLStreamException;

  public abstract XMLEventReader createXMLEventReader(InputStream paramInputStream, String paramString)
    throws XMLStreamException;

  public abstract XMLEventReader createXMLEventReader(String paramString, InputStream paramInputStream)
    throws XMLStreamException;

  public abstract XMLStreamReader createFilteredReader(XMLStreamReader paramXMLStreamReader, StreamFilter paramStreamFilter)
    throws XMLStreamException;

  public abstract XMLEventReader createFilteredReader(XMLEventReader paramXMLEventReader, EventFilter paramEventFilter)
    throws XMLStreamException;

  public abstract XMLResolver getXMLResolver();

  public abstract void setXMLResolver(XMLResolver paramXMLResolver);

  public abstract XMLReporter getXMLReporter();

  public abstract void setXMLReporter(XMLReporter paramXMLReporter);

  public abstract void setProperty(String paramString, Object paramObject)
    throws IllegalArgumentException;

  public abstract Object getProperty(String paramString)
    throws IllegalArgumentException;

  public abstract boolean isPropertySupported(String paramString);

  public abstract void setEventAllocator(XMLEventAllocator paramXMLEventAllocator);

  public abstract XMLEventAllocator getEventAllocator();
}