package UserInterface;

import java.awt.EventQueue;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.swing.JTextArea;

public class Climate extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	
	public JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Climate frame = new Climate();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Climate() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 513, 206);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(12, 0, 489, 166);
		contentPane.add(textArea);
		textArea.setEditable(false);
		
		pullClimate();
	}
	
	private void pullClimate() {
		try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            String url = "http://www.webservicex.net/globalweather.asmx";
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), url);
            
            textArea.setText(printSOAPResponse(soapResponse));
            
            soapConnection.close();
        } catch (Exception e) {
            System.err.println("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
        }
	}

    private static SOAPMessage createSOAPRequest() throws Exception {
    	MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();

        envelope.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
        envelope.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
        envelope.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        envelope.removeAttributeNS("http://schemas.xmlsoap.org/soap/envelope/", "SOAP-ENV");
        envelope.removeAttribute("xmlns:SOAP-ENV");
        envelope.setPrefix("soap");
        envelope.getHeader().detachNode();

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        soapBody.setPrefix("soap");
        SOAPElement soapBodyElem = soapBody.addChildElement("GetWeather", "", "http://www.webserviceX.NET");
        
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("CityName");
        soapBodyElem1.addTextNode("Don Miguel / Guadalaj");
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("CountryName");
        soapBodyElem2.addTextNode("Mexico");
        
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", "http://www.webserviceX.NET/GetWeather");

        soapMessage.saveChanges();
        
        /* Print the request message */
        System.out.print("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println();

        return soapMessage;
    }

    /**
     * Method used to print the SOAP Response
     */
    private static String printSOAPResponse(SOAPMessage soapResponse) throws Exception {
    	String s;
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);
        transformer.transform(sourceContent, result);
        StringBuffer sb = outWriter.getBuffer(); 
        s = sb.toString();
        System.out.println(s);
        s = parseClimate(s);
        System.out.println(s);
        return s;
    }
    
    public static String parseClimate(String input) {
    	String s = "";
    	s += "Location: " + input.split("&lt;")[3].replace("Location&gt;", "") + "\n";
    	s += "Time: " + input.split("&lt;")[5].replace("Time&gt;", "") + "\n";
    	s += "Wind: " + input.split("&lt;")[7].replace("Wind&gt;", "") + "\n";
    	s += "Visibility: " + input.split("&lt;")[9].replace("Visibility&gt;", "") + "\n";
    	s += "SkyConditions: " + input.split("&lt;")[11].replace("SkyConditions&gt;", "") + "\n";
    	s += "Temperature: " + input.split("&lt;")[13].replace("Temperature&gt;", "") + "\n";
    	s += "DewPoint: " + input.split("&lt;")[15].replace("DewPoint&gt;", "") + "\n";
    	s += "RelativeHumidity: " + input.split("&lt;")[17].replace("RelativeHumidity&gt;", "") + "\n";
    	s += "Pressure: " + input.split("&lt;")[19].replace("Pressure&gt;", "") + "\n";
    	return s;
    }
}
