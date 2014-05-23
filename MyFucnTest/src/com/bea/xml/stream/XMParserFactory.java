package com.bea.xml.stream;

import java.io.InputStream;
import java.io.Reader;

import com.independentsoft.xml.stream.EventFilter;
import com.independentsoft.xml.stream.StreamFilter;
import com.independentsoft.xml.stream.XMLEventReader;
import com.independentsoft.xml.stream.XMLInputFactory;
import com.independentsoft.xml.stream.XMLReporter;
import com.independentsoft.xml.stream.XMLResolver;
import com.independentsoft.xml.stream.XMLStreamException;
import com.independentsoft.xml.stream.XMLStreamReader;
import com.independentsoft.xml.stream.util.XMLEventAllocator;
import com.independentsoft.xml.transform.Source;

public class XMParserFactory extends XMLInputFactory {

	@Override
	public XMLStreamReader createXMLStreamReader(Reader paramReader)throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLStreamReader createXMLStreamReader(Source paramSource)
			throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLStreamReader createXMLStreamReader(InputStream paramInputStream)
			throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLStreamReader createXMLStreamReader(InputStream paramInputStream,
			String paramString) throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLStreamReader createXMLStreamReader(String paramString,
			InputStream paramInputStream) throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLStreamReader createXMLStreamReader(String paramString,
			Reader paramReader) throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLEventReader createXMLEventReader(Reader paramReader)
			throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLEventReader createXMLEventReader(String paramString,
			Reader paramReader) throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLEventReader createXMLEventReader(
			XMLStreamReader paramXMLStreamReader) throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLEventReader createXMLEventReader(Source paramSource)
			throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLEventReader createXMLEventReader(InputStream paramInputStream)
			throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLEventReader createXMLEventReader(InputStream paramInputStream,
			String paramString) throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLEventReader createXMLEventReader(String paramString,
			InputStream paramInputStream) throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLStreamReader createFilteredReader(
			XMLStreamReader paramXMLStreamReader, StreamFilter paramStreamFilter)
			throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLEventReader createFilteredReader(
			XMLEventReader paramXMLEventReader, EventFilter paramEventFilter)
			throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLResolver getXMLResolver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setXMLResolver(XMLResolver paramXMLResolver) {
		// TODO Auto-generated method stub

	}

	@Override
	public XMLReporter getXMLReporter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setXMLReporter(XMLReporter paramXMLReporter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProperty(String paramString, Object paramObject)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getProperty(String paramString)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPropertySupported(String paramString) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setEventAllocator(XMLEventAllocator paramXMLEventAllocator) {
		// TODO Auto-generated method stub

	}

	@Override
	public XMLEventAllocator getEventAllocator() {
		// TODO Auto-generated method stub
		return null;
	}

}
