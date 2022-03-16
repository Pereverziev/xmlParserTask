public class ApplicationMain {

    public static void main(String[] args) {
        final XmlParser xmlParser = new XmlParser("C:\\big.xml");
        System.out.println("Sales for client is:" + xmlParser.getTotalSalesForClientId(128));
    }

}
