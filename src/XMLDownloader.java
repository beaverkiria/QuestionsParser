import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by kirillcherepanov on 03/02/15.
 */
public class XMLDownloader {
    private URL rootURL;
    private String path;

    /**
     * Constructs an instance of XMLDownloader using provided root URL
     *
     * @param rootURL an URL of root XML
     */
    public XMLDownloader(URL rootURL) {
        this.rootURL = rootURL;
    }

    /**
     * Constructs an instance of XMLDownloader using provided string representation of root URL
     * @param rootURL an URL of root XML
     * @throws MalformedURLException if URL is bad formatted
     */
    public XMLDownloader(String rootURL) throws MalformedURLException {
        this.rootURL = new URL(rootURL);
    }

    /**
     * Gets URL of root XML file
     *
     * @return URL of root XML file
     */
    public URL getRootURL() {
        return rootURL;
    }

    /**
     * Gets path to save files
     *
     * @return path to save files
     */
    public String getPath() {
        return path;
    }

    /**
     * Parses a XML to {@link org.w3c.dom.Document} from provided URL
     *
     * @param url provided URL
     * @return a parsed {@link org.w3c.dom.Document}
     * @throws IOException if any I/O error occurs
     */
    public Document getDocumentByURL(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        Document document = null;
        try {
            document = parseXML(connection.getInputStream());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * parses a XML to {@link org.w3c.dom.Document} from provided URL
     *
     * @param url provided URL
     * @return a parsed {@link org.w3c.dom.Document}
     * @throws IOException if any I/O error occurs
     */
    public Document getDocumentByURL(String url) throws IOException {
        return getDocumentByURL(new URL(url));
    }

    /**
     * Reads a XML file using URL of root XML and returns {@link org.w3c.dom.NodeList} by provided tag
     *
     * @param tag provided tag
     * @return a {@link org.w3c.dom.NodeList} which contains elements with provided tag
     * @throws IOException if any I/O error occurs
     */
    public NodeList getElementsByTag(String tag) throws IOException {
        return getElementsByTag(rootURL, tag);
    }

    /**
     * Reads a XML file using provided URL and returns {@link org.w3c.dom.NodeList} by provided tag
     *
     * @param url a XML URL
     * @param tag provided tag
     * @return a {@link org.w3c.dom.NodeList} which contains elements with provided tag
     * @throws IOException if any I/O errors occurs
     */
    public NodeList getElementsByTag(URL url, String tag) throws IOException {
        Document document = getDocumentByURL(url);
        return document.getElementsByTagName(tag);
    }

    /**
     * Parses XML file from provided {@link java.io.InputStream}
     * @param inputStream provided {@link java.io.InputStream}
     * @return a {@link org.w3c.dom.Document} which was parsed from provided {@link java.io.InputStream}
     * @throws ParserConfigurationException if a DocumentBuilder
     *   cannot be created which satisfies the configuration requested.
     * @throws IOException if any I/O error occurs
     * @throws SAXException if any parse error occurs
     */
    private Document parseXML(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(inputStream);
    }

    /**
     * Returns a name of XML file from provided URL
     *
     * @param url provided URL
     * @return a name of XML file represented as a {@link java.lang.String}
     */
    private String getNameOfXMLFileFromURL(URL url) {
        return getNameOfXMLFileFromURL(url.toString());
    }

    /**
     * Returns a name of XML file from provided URL
     *
     * @param url provided URL
     * @return a name of XML file represented as a {@link java.lang.String}
     */
    private String getNameOfXMLFileFromURL(String url) {
        String result = "";
        int startIndex = url.indexOf("tour/") + "tour/".length();
        int endIndex = url.lastIndexOf("/xml");
        result = url.substring(startIndex, endIndex);
        if (result.length() == 0) {
            return "root";
        }
        return result;
    }

    /**
     * Downloads a XML file from provided URL and saves it at provided path
     *
     * @param url  a URL of XML file
     * @param path path to save
     * @throws IOException if any I/O error occurs
     */
    public void downloadXMLFile(URL url, String path) throws IOException {
        Document document = getDocumentByURL(url);
        saveDocument(document, path, getNameOfXMLFileFromURL(url));
    }

    /**
     * Saves provided {@link org.w3c.dom.Document} at provided path and names it with provided name
     *
     * @param document {@link org.w3c.dom.Document} to save
     * @param path     save file path
     * @param name     name of file
     */
    public void saveDocument(Document document, String path, String name) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(new File(path + "/" + name));
            Source input = new DOMSource(document);
            transformer.transform(input, output);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Downloads a XML file from provided URL and saves it at provided path
     *
     * @param url  a URL of XML file
     * @param path path to save
     * @throws IOException if any I/O error occurs
     */
    public void downloadXMLFile(String url, String path) throws IOException {
        downloadXMLFile(new URL(url), path);
    }
}
