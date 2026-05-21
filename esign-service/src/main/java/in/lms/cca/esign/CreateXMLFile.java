package in.lms.cca.esign;

import java.time.LocalDateTime;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class CreateXMLFile {

	
	public void createXML(String ih,
						  String txnID,
						  String resUrl,
						  String hashDocId,
						  String xmlPath,
						  String timestamp) {
		
		
		
		final String xmlFilePath = xmlPath;
		String inputH = ih;
		String dated = timestamp;
		
		
		/*
		 # Format to generate 
		 
		 <Esign ver="" sc="" ts="" txn="" ekycId="" ekycIdType="" aspId="" AuthMode="" responseSigType=" responseUrl="">
		 	<Docs>
				<InputHash id="" hashAlgorithm="" docInfo="">Document Hash in Hex</InputHash>
			</Docs>
			<Signature>Digital signature of ASP</Signature>
		</Esign>
	   */
		
		

		String eSignVersion="2.1";
		String signatoryConsent="Y";
		String requestTimestamp = dated;
		String transactionId = txnID;
		String eKYCIdType = "A";
		String ekycID = ""; // optional
		String aspID = "CDCD-900";
		String authenticationMode = "1"; // 1 - OTP, 2 - Fingerprint, 3 - IRIS
		String responseSignatureType = "pkcs7"; //pkcs7,rawrsa,rawecdsa
		String responseURL = resUrl;
		String hashingAlgorithm = "SHA256";
		String documentInfo = "My Document";
		
		try {
			 
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
 
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
 
            Document document = documentBuilder.newDocument();
 
            // root element
            Element root = document.createElement("Esign");
            document.appendChild(root);
            
         // set an attribute to staff element
            Attr ver = document.createAttribute("ver");
            ver.setValue(eSignVersion);
            root.setAttributeNode(ver);
         // set an attribute to staff element
            Attr sc = document.createAttribute("sc");
            sc.setValue(signatoryConsent);
            root.setAttributeNode(sc);
         // set an attribute to staff element
            Attr ts = document.createAttribute("ts");
            ts.setValue(requestTimestamp);
            root.setAttributeNode(ts);
         // set an attribute to staff element
            Attr txn = document.createAttribute("txn");
            txn.setValue(transactionId);
            root.setAttributeNode(txn);
         // set an attribute to staff element
            Attr ekycId = document.createAttribute("ekycId");
            ekycId.setValue(ekycID);
            root.setAttributeNode(ekycId);
         // set an attribute to staff element
            Attr ekycIdType = document.createAttribute("ekycIdType");
            ekycIdType.setValue(eKYCIdType);
            root.setAttributeNode(ekycIdType);
         // set an attribute to staff element
            Attr aspId = document.createAttribute("aspId");
            aspId.setValue(aspID);
            root.setAttributeNode(aspId);
         // set an attribute to staff element
            Attr authMode = document.createAttribute("AuthMode");
            authMode.setValue(authenticationMode);
            root.setAttributeNode(authMode);
         // set an attribute to staff element
            Attr responseSigType = document.createAttribute("responseSigType");
            responseSigType.setValue(responseSignatureType);
            root.setAttributeNode(responseSigType);
         // set an attribute to staff element
            Attr responseUrl = document.createAttribute("responseUrl");
            responseUrl.setValue(responseURL);
            root.setAttributeNode(responseUrl);
            
         // Docs element
            Element docs = document.createElement("Docs");
            root.appendChild(docs);
            
         // InputHash element
            Element InputHash = document.createElement("InputHash");
            //InputHash.appendChild(document.createTextNode("Document Hash in Hex"));
            InputHash.appendChild(document.createTextNode(inputH));
            docs.appendChild(InputHash);
 
            // set an attribute to staff element
            Attr attrId = document.createAttribute("id");
            attrId.setValue(hashDocId);
            InputHash.setAttributeNode(attrId);
            
         // set an attribute to staff element
            Attr hashAlgo = document.createAttribute("hashAlgorithm");
            hashAlgo.setValue(hashingAlgorithm);
            InputHash.setAttributeNode(hashAlgo);
            
         // set an attribute to staff element
            Attr attrDocInfo = document.createAttribute("docInfo");
            attrDocInfo.setValue(documentInfo);
            InputHash.setAttributeNode(attrDocInfo);
            
           
 
            //you can also use staff.setAttribute("id", "1") for this
 
            
         // Signature element
            //Element Signature = document.createElement("Signature");
           // Signature.appendChild(document.createTextNode("Digital signature of ASP"));
            //root.appendChild(Signature);
 
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));
 
            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging 
 
            transformer.transform(domSource, streamResult);
 
            System.out.println("Done creating XML File");
 
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
		
		
	}
	
	
	public void writeToXmlFile(String xmlIn, String fileName) {
		try {
			Document doc;
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xmlIn)));

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			
			
           // Create the file at server
		       File serverFile = new File(fileName);
 
		       
		       StringWriter writer = new StringWriter();
		       StreamResult result = new StreamResult(writer);
		       TransformerFactory tf = TransformerFactory.newInstance();
		       Transformer transformerTemp = tf.newTransformer();
		       transformerTemp.transform(source, result);
		       
		       
               BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
               stream.write(writer.toString().getBytes());
               stream.close();

            } catch (Exception e) {
            	e.printStackTrace();
            }
            
	}
	
	
}
