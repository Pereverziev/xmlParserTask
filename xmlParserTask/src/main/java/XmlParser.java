import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.math.BigDecimal;

public class XmlParser extends DefaultHandler {

    private static final SAXParserFactory SAX_PARSER_FACTORY = SAXParserFactory.newInstance();
    private final String FILE_PATH;
    private final StringBuilder sectionContent = new StringBuilder();
    private final Logger LOGGER = LoggerFactory.getLogger(XmlParser.class);
    private SAXParser saxParser = null;
    private Integer clientId = null;
    private Boolean clientSection = false;
    private BigDecimal sales = BigDecimal.ZERO;

    XmlParser(String filePath) {
        FILE_PATH = filePath;
        try {
            saxParser = SAX_PARSER_FACTORY.newSAXParser();
        } catch (Exception exc) {
            LOGGER.error("Something went wrong while creating sax parser", exc);
        }
    }

    BigDecimal getTotalSalesForClientId(Integer id) {
        clientId = id;
        try {
            saxParser.parse(FILE_PATH, this);
        } catch (Exception exc) {
            LOGGER.error("Something went wrong while parsing file ", exc);
        }
        return sales;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        sectionContent.setLength(0);
        if (qName.equalsIgnoreCase("client") && attributes.getValue("id").equals(clientId.toString())) {
            clientSection = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("client")) {
            clientSection = false;
        }
        if (qName.equalsIgnoreCase("amount") && clientSection) {
            sales = sales.add(new BigDecimal(sectionContent.toString()));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        sectionContent.append(ch, start, length);
    }
}
