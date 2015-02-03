import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by kirillcherepanov on 03/02/15.
 */
public class XMLDownloader {
    URL rootURL;

    public XMLDownloader(URL rootURL) {
        this.rootURL = rootURL;
    }

    public XMLDownloader(String rootURL) throws MalformedURLException {
        this.rootURL = new URL(rootURL);
    }

    public NodeList getTreeIdNodeList() throws IOException {
        URLConnection connection = rootURL.openConnection();
        Document document = null;
        try {
            document = parseXML(connection.getInputStream());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return document.getElementsByTagName("TextId");
    }

    private Document parseXML(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(inputStream);
    }

}
