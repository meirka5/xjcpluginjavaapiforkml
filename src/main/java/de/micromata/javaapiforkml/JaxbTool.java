package de.micromata.javaapiforkml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public final class JaxbTool<T>
{
  private static final Logger LOG = Logger.getLogger(JaxbTool.class.getName());

  private final static String SCHEMA_LOCATION = "src/main/resources/schema/ogckml/ogckml22.xsd";

  private String schemaValidationFile = SCHEMA_LOCATION;

  private JAXBContext jc = null;

  private Marshaller m = null;

  private Unmarshaller u = null;

  private final Class<T> declaredType;

  public JAXBContext getJaxbContext() throws JAXBException
  {
    if (jc == null) {
      jc = JAXBContext.newInstance(declaredType);
    }
    return jc;
  }

  public Marshaller getMarshaller() throws JAXBException
  {
    if (m == null) {
      m = getJaxbContext().createMarshaller();
    }
    return m;
  }

  public Unmarshaller getUnmarshaller() throws JAXBException
  {
    if (u == null) {
      u = getJaxbContext().createUnmarshaller();
    }
    return u;
  }

  /**
   * create a JAXBContext capable of handling classes generated into the given package
   * 
   * @param contextPath
   * @throws JAXBException
   */
  public JaxbTool(final Class<T> clazz)
  {
    this.declaredType = clazz;
  }

  /**
   * Java to XML https://jaxb.dev.java.net/guide/Different_ways_of_marshalling.html
   * 
   * @param elements
   * @param T
   * @throws JAXBException
   * @throws FileNotFoundException
   */
  public void marshal(final T plainRootObject, final OutputStream outputStream) throws FileNotFoundException
  {
    try {
      final JAXBElement<T> jaxbRootElement = prepareJaxbElement(plainRootObject);
      m = prepareMarshaller();
      m.marshal(jaxbRootElement, outputStream);
    } catch (final JAXBException _x) {
      _x.printStackTrace();
    }
  }

  public void marshal(final T plainRootObject, final File output)
  {
    try {
      final JAXBElement<T> jaxbRootElement = prepareJaxbElement(plainRootObject);
      m = prepareMarshaller();
      m.marshal(jaxbRootElement, output);
    } catch (final JAXBException _x) {
      _x.printStackTrace();
    }
  }

  public void marshal(final T plainRootObject, final java.io.Writer writer)
  {
    try {
      final JAXBElement<T> jaxbRootElement = prepareJaxbElement(plainRootObject);
      m = prepareMarshaller();
      m.marshal(jaxbRootElement, writer);
    } catch (final JAXBException _x) {
      _x.printStackTrace();
    }
  }

  private JAXBElement<T> prepareJaxbElement(final T plainRootObject)
  {
    String name = plainRootObject.getClass().getSimpleName();
    if ("Kml".equals(name)) {
      name = name.toLowerCase();
    }
//    LOG.info("prepareJaxbElement: " + name);
    // TODO: only 'static' for kml
    // due to the fact, that declaredType.getSimpleName().toLowerCase() could return kmltype
    // find a better solution. perhaps move to api and give each major kml-element the power
    // to marshal and unmarshall by itself
    final JAXBElement<T> jaxbRootElement = new JAXBElement<T>(new QName("http://www.opengis.net/kml/2.2", name), declaredType, plainRootObject);
    return jaxbRootElement;
  }

  private Marshaller prepareMarshaller() throws JAXBException
  {
    // create context and marshaller objects
    // create a Marshaller and marshal to a file
    // Marshaller m = this.jc.createMarshaller();
    m = this.getMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    return m;
  }

  /**
   * XML to Java https://jaxb.dev.java.net/guide/_XmlRootElement_and_unmarshalling.html
   * 
   * @param <T>
   * @throws JAXBException
   */
  public T unmarshal(final File file)
  {
    try {
      u = this.getUnmarshaller();
      final JAXBElement<T> jaxbRootElement = u.unmarshal(new StreamSource(file), declaredType);
      return jaxbRootElement.getValue();
 
//      return u.unmarshal(new StreamSource(file), declaredType).getValue();
    } catch (final JAXBException _x) {
      _x.printStackTrace();
    }

    return null;
  }

  public T unmarshal(final InputStream is)
  {
    try {
      u = this.getUnmarshaller();
      final JAXBElement<T> jaxbRootElement = u.unmarshal(new StreamSource(is), declaredType);
      return jaxbRootElement.getValue();
 
//      return u.unmarshal(new StreamSource(file), declaredType).getValue();
    } catch (final JAXBException _x) {
      _x.printStackTrace();
    }

    return null;
  }

  public T unmarshal(final Reader reader)
  {
    try {
      u = this.getUnmarshaller();
      final JAXBElement<T> jaxbRootElement = u.unmarshal(new StreamSource(reader), declaredType);
      return jaxbRootElement.getValue();
 
//      return u.unmarshal(new StreamSource(file), declaredType).getValue();
    } catch (final JAXBException _x) {
      _x.printStackTrace();
    }

    return null;
 }

  public T unmarshal(final URL url)
  {
    try {
      u = this.getUnmarshaller();
      final JAXBElement<T> jaxbRootElement = u.unmarshal(new StreamSource(url.toExternalForm()), declaredType);
      return jaxbRootElement.getValue();
 
//      return u.unmarshal(new StreamSource(file), declaredType).getValue();
    } catch (final JAXBException _x) {
      _x.printStackTrace();
    }

    return null;
 }

  public T unmarshal(final Source source)
  {
    try {
      u = this.getUnmarshaller();
      final JAXBElement<T> jaxbRootElement = u.unmarshal(source, declaredType);
      return jaxbRootElement.getValue();
 
//      return u.unmarshal(new StreamSource(file), declaredType).getValue();
    } catch (final JAXBException _x) {
      _x.printStackTrace();
    }

    return null;
 }

  private boolean validate()
  {
    try {
      u = this.getUnmarshaller();

      final SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      final File schemaFile = new File(schemaValidationFile);
      final Schema schema = sf.newSchema(schemaFile);
      u.setSchema(schema);
      return true;
    } catch (final JAXBException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final SAXException _x) {
      _x.printStackTrace();
    }
    return false;
  }

  public void generateSchema(final String schemaName) throws IOException
  {
    final SystemOutSchemaOutput sysout = new SystemOutSchemaOutput();
    try {
      this.getJaxbContext().generateSchema(sysout);
      sysout.saveToFile(schemaName);
      LOG.info("--------------");
    } catch (final JAXBException _x) {
      _x.printStackTrace();
    }

  }

  public String getSchemaValidationFile()
  {
    return schemaValidationFile;
  }

  public void setSchemaValidationFile(final String schemaValidationFile)
  {
    this.schemaValidationFile = schemaValidationFile;
  }

  
  
}
