package in.lms.cca.controller;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.io.StringReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.security.auth.x500.X500Principal;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import in.lms.cca.config.CrossOrigins;
import in.lms.cca.dto.EsignRequestDTO;
import in.lms.cca.entity.ESignRequest;
import in.lms.cca.entity.ESignResponse;
import in.lms.cca.esign.CreateXMLFile;
import in.lms.cca.esign.PdfChecksum;
import in.lms.cca.esign.PdfEmbedder;
import in.lms.cca.esign.TransactionId;
import in.lms.cca.esign.XMLEncryption;
import in.lms.cca.esign.XmlAsString;
import in.lms.cca.esign.XmlSigning;
import in.lms.cca.repository.EsignRequestRepository;
import in.lms.cca.repository.EsignResponseRepository;
import in.lms.cca.service.ClientService;
import in.lms.cca.util.api.EsignServiceAPIs;
import in.lms.cca.util.golbal.EncryptionUtil;
import in.lms.cca.util.golbal.RealPath;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(EsignServiceAPIs.ESIGN_SERVICE_BASE_URL)
@CrossOrigin
public class EsignController {

//    @Autowired
//    private RestTemplate restTemplate;
    
    @Autowired
    private EsignRequestRepository requestRepo;
    
    @Autowired
    private EsignResponseRepository responseRepo;
    
    
    @Autowired
    private ClientService clientServ;
    
    
    
    
    @PostMapping(EsignServiceAPIs.ESIGN_NEW_REQUEST)
    public ResponseEntity<?> requestNewEsign(
    										 	@RequestParam(value = "files") MultipartFile file,
    										 	@RequestParam(value = "userName") String userNames,
    										 	@RequestParam(value = "fullNames") String fullNames,
    										 	@RequestParam(value = "serviceName") String serviceNames,
    										 	@RequestParam(value = "serviceUrl") String serviceUrls,
    										 	@RequestParam(value = "orgFileId") String orgFileIds,
    										 	@RequestParam(value = "documentPath") String documentPaths,
    										 	@RequestParam(value = "redirectUrl") String redirectUrls,
    										 	HttpServletRequest request
    										 ) throws IOException {
        
        
        
    	 
    	String userName = EncryptionUtil.decrypt(userNames);
    	String fullName = fullNames != null ? EncryptionUtil.decrypt(fullNames) : null;
	 	 String serviceName = EncryptionUtil.decrypt(serviceNames);
	 	 String serviceUrl = EncryptionUtil.decrypt(serviceUrls);
	 	 String orgFileId = EncryptionUtil.decrypt(orgFileIds);
	 	 String documentPath = EncryptionUtil.decrypt(documentPaths);
	 	 String redirectUrl = EncryptionUtil.decrypt(redirectUrls);
    	
    	
    	// Create XML File for eSign
        String hashDocId = "1";
        String transactionId = "";
        String resUrl = "http://localhost:7778/esign-service/esignresponse";
        String fileHash = "";
        String xmlData = "";
        String xmlasstring = "";
        String certPath = RealPath.REAL_PATH + "\\cert\\ssdp.key";
        
        
        
        PrivateKey rsaPrivateKey = null;

        CreateXMLFile CXF = new CreateXMLFile();
        PdfChecksum PdfChecksum = new PdfChecksum();
        TransactionId TXI = new TransactionId();
        XmlAsString XAS = new XmlAsString();
        XMLEncryption encryption = new XMLEncryption();
        
        try {
        	
//        	token = request != null ? request.getHeader("Authorization") : null;
        	//token - save for later use in eSign callback
        	String token = request != null ? request.getHeader("Authorization") : null;
        	
        	
        	
        	SimpleDateFormat dateFormats = new SimpleDateFormat("MMddyyyyHHmmssSSS");
	        String timestamp = dateFormats.format(new Date());
            String dfilename = "E" +userName.substring(8)+timestamp;
        
        	File pdfFilePath = new File(RealPath.REAL_PATH+"\\esignrequest\\document", dfilename +".pdf");
        	file.transferTo(pdfFilePath);
        	
        	rsaPrivateKey = encryption.getPrivateKey(certPath);
        	
            fileHash = PdfChecksum.getHashOfFile(pdfFilePath);
            transactionId = TXI.transactionId();

            LocalDateTime myDateObj = LocalDateTime.now();
            

            
            
            String xmlFilePath = RealPath.REAL_PATH + "\\esignrequest\\xml\\"+dfilename+".xml";
            
            CXF.createXML(fileHash, transactionId, resUrl, hashDocId, xmlFilePath, myDateObj.toString());
            xmlData = new XmlSigning().signXmlStringNew(xmlFilePath, rsaPrivateKey);
            CXF.writeToXmlFile(xmlData, xmlFilePath);
            xmlasstring = XAS.xmlAsString(xmlFilePath);
            
            
            //Save Esign Request Data
            
            ESignRequest reqData = new ESignRequest();
            reqData.setAuthMode("1");
            reqData.setDocInfo("My Document");
            reqData.setDocumentHash(fileHash);
            reqData.setDocumentPath(documentPath);
            reqData.setServiceUrl(serviceUrl);
            reqData.setUserName(userName);
            reqData.setServiceName(serviceName);
            reqData.seteKYCIdType("A");
            reqData.seteSignVersion("2.1");
            reqData.setRequestTimeStamp(Date.from(myDateObj.toInstant(ZoneOffset.UTC)));
            reqData.setTransactionId(transactionId);
            reqData.setResponseSigType("pkcs7");
            reqData.setFileName(dfilename);
            reqData.setOrgFileId(orgFileId);
            reqData.setRedirectUrl(redirectUrl);
            reqData.setFullname(fullName);
            reqData.setAuthToken(token); //here added the missing auth token 
            
            ESignRequest routput = requestRepo.save(reqData);
            
            EsignRequestDTO requestDTO = new EsignRequestDTO();
            requestDTO.setAspTxnID(routput.getTransactionId());
            requestDTO.setContentType("application/xml");
            requestDTO.seteSignRequest(xmlasstring);
            
    	    return new ResponseEntity<>(requestDTO, HttpStatus.OK);
    		
           
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Esign Failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(EsignServiceAPIs.ESIGN_RESPONSE)
    public ResponseEntity<?> eSignResponse(HttpServletRequest request, HttpServletResponse response) {
        
        String eSignResponse = request.getParameter("eSignResponse");
        String espTxnID = request.getParameter("espTxnID");
        
       
        
        ESignRequest ereqquest = requestRepo.findByTransactionId(espTxnID);
       
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true); 
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(eSignResponse)));

            
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            String errCode = xpath.evaluate("/EsignResp/@errCode", document);
            String errMsg = xpath.evaluate("/EsignResp/@errMsg", document);
            String resCode = xpath.evaluate("/EsignResp/@resCode", document);
            String status = xpath.evaluate("/EsignResp/@status", document);
            String ts = xpath.evaluate("/EsignResp/@ts", document);
            //String txn = xpath.evaluate("/EsignResp/@txn", document);

            XPathExpression digestValueExpr = xpath.compile("//*[local-name()='DigestValue']");
            String digestValue = (String) digestValueExpr.evaluate(document, XPathConstants.STRING);

            
            XPathExpression signatureValueExpr = xpath.compile("//*[local-name()='DocSignature']");
            String signatureValue = (String) signatureValueExpr.evaluate(document, XPathConstants.STRING);

            XPathExpression userX509Certificate = xpath.compile("//*[local-name()='UserX509Certificate']");
            String UserX509Certificate = (String) userX509Certificate.evaluate(document, XPathConstants.STRING);
     
            
            //Check for Signer name
            
            PdfEmbedder pdfSign = new PdfEmbedder(); 
            String signerName = PdfEmbedder.findSignerName(UserX509Certificate);
            
            //String signerName = pdfSign.findSignerName(UserX509Certificate);

            if( signerName != null && !ereqquest.getFullname().equals(signerName)) {
            	
            	// do not sign
            	
                String pid =  EncryptionUtil.encrypt(ereqquest.getRedirectUrl());
                String id = EncryptionUtil.encrypt("signer_mismatch");
                
                String urlToRedirect = CrossOrigins.Origins + "/esignresponse" + "?id="+id+"&pid="+pid;
                URI location = URI.create(urlToRedirect);
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(location);
                return new ResponseEntity<>(headers, HttpStatus.FOUND);

                
                
            }
            
            
            //
            
            CreateXMLFile CXF = new CreateXMLFile();
            SimpleDateFormat dateFormats = new SimpleDateFormat("MMddyyyyHHmmssSSS");
	        String timestamp = dateFormats.format(new Date());
            String dfilename = "E" +ereqquest.getUserName().substring(8)+timestamp;
            
            String xmlFilePath = RealPath.REAL_PATH + "\\esignresponse\\xml\\"+dfilename+".xml";
            
            
            //
            
            CXF.writeToXmlFile(eSignResponse, xmlFilePath);
           
            
            //
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            LocalDateTime localDateTime = LocalDateTime.parse(ts, formatter);
            Date transactionTimestamp = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

            
            
            ESignResponse esignResponse = new ESignResponse();
            
            esignResponse.setDigestValue(digestValue);
            esignResponse.setErrCode(errCode);
            esignResponse.setErrMsg(errMsg);
            esignResponse.seteSignRequestId(ereqquest);
            esignResponse.seteSignStatus(Integer.parseInt(status));
            esignResponse.setFileName(dfilename);
            esignResponse.setRescode(resCode);
            esignResponse.setResponseTimeStamp(transactionTimestamp);
            esignResponse.setSignatureValue(signatureValue);
            
            ESignResponse res = responseRepo.save(esignResponse);
            
            
            //Esign The Document
            
            if(Integer.parseInt(status)==1) {
            	
            	
            	String src = RealPath.REAL_PATH + "\\esignrequest\\document\\"+ereqquest.getFileName()+".pdf";
            	String dest = RealPath.REAL_PATH + "\\esignresponse\\document\\"+dfilename+".pdf";
            	
            	
            	
            	pdfSign.pdfSigner(src, dest, signatureValue, UserX509Certificate);
            	
            	
            	//clientServ.eSignDocument(ereqquest.getServiceName()+ereqquest.getServiceUrl(), ereqquest.getOrgFileId(), res.geteSignResponseId().toString(), token);

            	clientServ.eSignDocument(ereqquest.getServiceName()+ereqquest.getServiceUrl(), ereqquest.getOrgFileId(), res.geteSignResponseId().toString(), ereqquest.getAuthToken());
            	
            	
            	
            	String pid =  EncryptionUtil.encrypt(ereqquest.getRedirectUrl());
            	System.out.println("pid--->"+ereqquest.getRedirectUrl() + "  " + pid);
                String id = EncryptionUtil.encrypt("signed_success");
                
                String urlToRedirect = CrossOrigins.Origins + "/esignresponse" + "?id="+id+"&pid="+pid;
                URI location = URI.create(urlToRedirect);
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(location);
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
            	 
            	
            	
            	
            }else {
            	String pid =  EncryptionUtil.encrypt(ereqquest.getRedirectUrl());
                String id = EncryptionUtil.encrypt("failed");
                
                String urlToRedirect = CrossOrigins.Origins + "/esignresponse" + "?id="+id+"&pid="+pid;
                URI location = URI.create(urlToRedirect);
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(location);
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
            }
            
            
            
            
            
            

        } catch (Exception e) {
            e.printStackTrace();
            String pid =  EncryptionUtil.encrypt(ereqquest.getRedirectUrl());
            String id = EncryptionUtil.encrypt("failed");
            
            String urlToRedirect = CrossOrigins.Origins + "/esignresponse" + "?id="+id+"&pid="+pid;
            URI location = URI.create(urlToRedirect);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }

    }
    
    
    
    
    @GetMapping(EsignServiceAPIs.GET_DIGITALLY_SIGNED_DOCUMENT)
	public ResponseEntity<?> getDigitallySignedDocument(@RequestParam("id") String id)
	{
    	
    	try {
    	
    		String resid = EncryptionUtil.decrypt(id);
    		System.out.println("resId-=-->"+id+"   "+resid);
    		
    		ESignResponse res = responseRepo.getByEsignResponseId(Long.parseLong(resid));
    	
	    	 if(res != null && res.getFileName() != null) {
	    		 
	    	
	    		 String filepath = "esignresponse\\document";
	    		 
	    		 
	    		 Path filePath = Paths.get(RealPath.REAL_PATH, filepath).resolve(res.getFileName()+".pdf").normalize();
	    		 
	             Resource resource = new UrlResource(filePath.toUri());
	             
	             System.out.println("filePath-=-->"+filePath);
	             System.out.println("resource-=-->"+resource);
	             if (resource.exists() && resource.isReadable()) {
	                 String contentType = Files.probeContentType(filePath);
	
	                 return ResponseEntity.ok()
	                         .contentType(MediaType.parseMediaType(contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
	                         .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
	                         .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
	                         .body(resource);
	             } else {
	             	return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
	             }
	    		 
	    	 }else {
	    		 return new ResponseEntity<>("Invalid Id.", HttpStatus.BAD_REQUEST);
	    	 }
    	
    	
    	}catch(Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
    	}
    	
    	
	}
    

@GetMapping("/testesign")
    public ResponseEntity<?> test() throws Exception {
        
        		
    			String signatureValue = "MIIOZwYJKoZIhvcNAQcCoIIOWDCCDlQCAQExDzANBglghkgBZQMEAgEFADALBgkqhkiG9w0BBwGg\r\n"
    					+ "ggzdMIIDzzCCAregAwIBAgIJAPjGKCX3TQcFMA0GCSqGSIb3DQEBBQUAMH4xCzAJBgNVBAYTAklO\r\n"
    					+ "MRQwEgYDVQQIDAtNYWhhcmFzaHRyYTENMAsGA1UEBwwEUFVORTEOMAwGA1UECgwFQy1EQUMxIjAg\r\n"
    					+ "BgNVBAsMGVRlc3QgQ2VydGlmeWluZyBBdXRob3JpdHkxFjAUBgNVBAMMDVRlc3QgQy1EQUMgQ0Ew\r\n"
    					+ "HhcNMTYxMjEzMDg0MzQ2WhcNMjYxMjExMDg0MzQ2WjB+MQswCQYDVQQGEwJJTjEUMBIGA1UECAwL\r\n"
    					+ "TWFoYXJhc2h0cmExDTALBgNVBAcMBFBVTkUxDjAMBgNVBAoMBUMtREFDMSIwIAYDVQQLDBlUZXN0\r\n"
    					+ "IENlcnRpZnlpbmcgQXV0aG9yaXR5MRYwFAYDVQQDDA1UZXN0IEMtREFDIENBMIIBIjANBgkqhkiG\r\n"
    					+ "9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0MWSA0jiO4YINrvyONf3kfbF3fYr2zjgZOJJ80LiCdEC+ZTA\r\n"
    					+ "DzBr0AdKcOl4i64D+xvo7yUH5h78rFi6jnMDWE7sEGyq3RZf+dfA6ljqC2gdKEeHOMN9DYOy/j+P\r\n"
    					+ "k5CgY4CMShYPb2W4kSlq7v5MaC8mZBul+ByF8W/Wp60VOwQzWi3z4W+ItnKtztJ4LYKEQUXopiNl\r\n"
    					+ "USt1UZW80P1dTG9CWYiG3kU+Q21/TeKROoMTEO5kQFO31hHK7rETmL2/qWoAuGUWGL6avUH8+SS/\r\n"
    					+ "u3s0teq/v0O9N7xMpzcV96deEnKS/THtEuiIHsa3B7CDtVAtVDBooM27sm2knYDCtwIDAQABo1Aw\r\n"
    					+ "TjAdBgNVHQ4EFgQUDnyhldbazCDzKE+UvXfTofQwixYwHwYDVR0jBBgwFoAUDnyhldbazCDzKE+U\r\n"
    					+ "vXfTofQwixYwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOCAQEAGNH1EnjmtFCQCyEhO5vU\r\n"
    					+ "43xtXsxWnZyv7D1hPaIVTCo8UfeeQvrwjrCs1OsZOIdYDT4GZPXYi0FVeEfUqFWiFQ8+Zaeqcanf\r\n"
    					+ "MC182v414UPqWMLjziVgSbtOnzyGs22jAsTGdollyLSwH3kh5OvXU9PQZgfgZPLsXVEBZzNDkRCm\r\n"
    					+ "HOyNQDnSGwVtBKOT/aP7Y7nP8kWiuHQ3/3yhgxPsTSLAiBwQS3VpCcH0g8VMMYRlGOOFiTaB2/TJ\r\n"
    					+ "5xDtGlYN3S9nniGyaO3yJOYklzHfP37PFccQkoa1kNUU7dFte7dgbVhlaz7NgSlOIAHG/wbrJ2j4\r\n"
    					+ "wcnkvYSEF4aV7DHolTCCBGEwggNJoAMCAQICAwPNgzANBgkqhkiG9w0BAQsFADB+MQswCQYDVQQG\r\n"
    					+ "EwJJTjEUMBIGA1UECAwLTWFoYXJhc2h0cmExDTALBgNVBAcMBFBVTkUxDjAMBgNVBAoMBUMtREFD\r\n"
    					+ "MSIwIAYDVQQLDBlUZXN0IENlcnRpZnlpbmcgQXV0aG9yaXR5MRYwFAYDVQQDDA1UZXN0IEMtREFD\r\n"
    					+ "IENBMB4XDTI0MDYxMTExMzgyN1oXDTM0MDkxNzExMzgyN1owbDELMAkGA1UEBhMCSU4xFDASBgNV\r\n"
    					+ "BAgMC01haGFyYXNodHJhMQ0wCwYDVQQHDARQVU5FMQ4wDAYDVQQKDAVDLURBQzEoMCYGA1UEAwwf\r\n"
    					+ "VGVzdCBDLURBQyBTdWIgQ0EgZm9yIGVLWUMgMjAyMjBZMBMGByqGSM49AgEGCCqGSM49AwEHA0IA\r\n"
    					+ "BLivVTO1LXy6Uh0diOjNvfHqNqEMZjzOPQJ35BLYmSeBI6ro9XVab0UoDod6uOz9/0IZ7IhkNU53\r\n"
    					+ "mwtSv7yKzWejggHDMIIBvzAdBgNVHQ4EFgQUFJmMutgoTZIKwXYgQ3qsph5Go+8wHwYDVR0jBBgw\r\n"
    					+ "FoAUDnyhldbazCDzKE+UvXfTofQwixYwDwYDVR0TBAgwBgEB/wIBADAOBgNVHQ8BAf8EBAMCAQYw\r\n"
    					+ "ggESBgNVHSAEggEJMIIBBTCCAQEGB2CCZGQBCQIwgfUwMAYIKwYBBQUHAgEWJGh0dHBzOi8vZXNp\r\n"
    					+ "Z24uY2RhYy5pbi9jYS9DUFMvQ1BTLnBkZjCBwAYIKwYBBQUHAgIwgbMwPho6Q2VudHJlIGZvciBE\r\n"
    					+ "ZXZlbG9wbWVudCBvZiBBZHZhbmNlZCBDb21wdXRpbmcgKEMtREFDKSwgUHVuZTAAGnFUaGlzIENQ\r\n"
    					+ "UyBpcyBvd25lZCBieSBDLURBQyBhbmQgdXNlcnMgYXJlIHJlcXVlc3RlZCB0byByZWFkIENQUyBi\r\n"
    					+ "ZWZvcmUgdXNpbmcgdGhlIEMtREFDIENBJ3MgY2VydGlmaWNhdGlvbiBzZXJ2aWNlczBGBggrBgEF\r\n"
    					+ "BQcBAQQ6MDgwNgYIKwYBBQUHMAKGKmh0dHBzOi8vZXNpZ24uY2RhYy5pbi9jYS9DREFDU3RnQ0Ey\r\n"
    					+ "MDE2LmRlcjANBgkqhkiG9w0BAQsFAAOCAQEAUKt9myzyPv0EH1E8J2mCJ1P+jC9XcOAVXRC+tZJo\r\n"
    					+ "5g/UhD2FLIr82Ge7hPhScMK1QysyPBSjsG5EAxZkF33PA9Jg6hRVqg/zZCVFK+n5+kzca4CoWSD+\r\n"
    					+ "XZ07YdcoigzK1YiHds/MnreIpteDXdAWsukw3RamczKi72pUizLEZfkccH4lL4NoXX1gDzHre47h\r\n"
    					+ "RN+YU2U1RoLWs6Ry+qmRwtD8vyMDVa/iiMk53u7R0QwmiamzmKo6bG7Mu/ptnw5pYY6xHpZbQ9w2\r\n"
    					+ "/D4PBV/I0RcgA4yl6caYEXIRCAWzFFdYQ4QYhWE/VuYCsGNcbXRbHS8O+/I/cuxzgaodqF+dKDCC\r\n"
    					+ "BKEwggRHoAMCAQICAwRpEzAKBggqhkjOPQQDAjBsMQswCQYDVQQGEwJJTjEUMBIGA1UECAwLTWFo\r\n"
    					+ "YXJhc2h0cmExDTALBgNVBAcMBFBVTkUxDjAMBgNVBAoMBUMtREFDMSgwJgYDVQQDDB9UZXN0IEMt\r\n"
    					+ "REFDIFN1YiBDQSBmb3IgZUtZQyAyMDIyMB4XDTI0MDkyNzA4MTc1OVoXDTI0MDkyNzA4NDc1OVow\r\n"
    					+ "ggFHMQ4wDAYDVQQGEwVJbmRpYTEWMBQGA1UECBMNVXR0YXIgUHJhZGVzaDERMA8GA1UEChMIUGVy\r\n"
    					+ "c29uYWwxGzAZBgNVBAMTElN1bWl0IEt1bWFyIFNoYXJtYTEPMA0GA1UEERMGMjIxNDA0MVIwUAYD\r\n"
    					+ "VQQtA0kAMDEwMDA0MjdLcE0yalA4cStEM3BBOXdJcFl6dEdjazhCcUFBZjI4TGRCTC9zaE1qVGQw\r\n"
    					+ "c00wcERGdEdWaXlGbXkrOEdHMzBnMSkwJwYDVQRBEyA4ZTRiNmI2YmNkOWM0YzdjYTAyNjZjZjg1\r\n"
    					+ "ZjliYzZlOTENMAsGA1UEDBMENDY3MjFOMEwGA1UELhNFMTk5OU05NjcyZmU4MTZkYjBmMmRlMGY1\r\n"
    					+ "NzNkNDI1ZWY1N2I1MGUwMzRhMjQ5NjgzODU0YWVkMzVjMTk2N2M2MWQ5Y2UwMFkwEwYHKoZIzj0C\r\n"
    					+ "AQYIKoZIzj0DAQcDQgAESuNq3RmZ9zzn2yK95u+Cq1qmDZ5FuVeGzZSBX/Lz5zk8mMH4WXi9BjEp\r\n"
    					+ "9xCFD/bHD50u4sARtzIoNGFwtDqQXaOCAfkwggH1MAkGA1UdEwQCMAAwHQYDVR0OBBYEFDu/fcDe\r\n"
    					+ "RBiL0y3g97PQ0Ucq0LtQMB8GA1UdIwQYMBaAFBSZjLrYKE2SCsF2IEN6rKYeRqPvMA4GA1UdDwEB\r\n"
    					+ "/wQEAwIGwDA+BgNVHR8ENzA1MDOgMaAvhi1odHRwczovL2VzaWduLmNkYWMuaW4vY2EvQ0RBQ1N0\r\n"
    					+ "Z1N1YkNBMjAyMi5jcmwwggFWBgNVHSAEggFNMIIBSTCCAQEGB2CCZGQBCQIwgfUwMAYIKwYBBQUH\r\n"
    					+ "AgEWJGh0dHBzOi8vZXNpZ24uY2RhYy5pbi9jYS9DUFMvQ1BTLnBkZjCBwAYIKwYBBQUHAgIwgbMw\r\n"
    					+ "PhY6Q2VudHJlIGZvciBEZXZlbG9wbWVudCBvZiBBZHZhbmNlZCBDb21wdXRpbmcgKEMtREFDKSwg\r\n"
    					+ "UHVuZTAAGnFUaGlzIENQUyBpcyBvd25lZCBieSBDLURBQyBhbmQgdXNlcnMgYXJlIHJlcXVlc3Rl\r\n"
    					+ "ZCB0byByZWFkIENQUyBiZWZvcmUgdXNpbmcgdGhlIEMtREFDIENBJ3MgY2VydGlmaWNhdGlvbiBz\r\n"
    					+ "ZXJ2aWNlczBCBgdggmRkAgQBMDcwNQYIKwYBBQUHAgIwKRonQWFkaGFhciBvbmxpbmUgZUtZQyBP\r\n"
    					+ "VFAgLSBTaW5nbGUgRmFjdG9yMAoGCCqGSM49BAMCA0gAMEUCIDr2uTa411t/36aIzAoSFGdHeQN8\r\n"
    					+ "EWlf7LL4HiPlCVyMAiEAy4qJDsAfc7wmPOU794i2SFcFpGCAhUfI9S/FdaVJNNExggFOMIIBSgIB\r\n"
    					+ "ATBzMGwxCzAJBgNVBAYTAklOMRQwEgYDVQQIDAtNYWhhcmFzaHRyYTENMAsGA1UEBwwEUFVORTEO\r\n"
    					+ "MAwGA1UECgwFQy1EQUMxKDAmBgNVBAMMH1Rlc3QgQy1EQUMgU3ViIENBIGZvciBlS1lDIDIwMjIC\r\n"
    					+ "AwRpEzANBglghkgBZQMEAgEFAKBpMBgGCSqGSIb3DQEJAzELBgkqhkiG9w0BBwEwHAYJKoZIhvcN\r\n"
    					+ "AQkFMQ8XDTI0MDkyNzA4MTc1OVowLwYJKoZIhvcNAQkEMSIEIKwmmskWn2IGcRkbrc8AGyVpIrIC\r\n"
    					+ "/Mi376IXORsc5utyMAwGCCqGSM49BAMCBQAESDBGAiEA+NcnZgxTcCUMKYUsOnkZpVrST9Llu38A\r\n"
    					+ "1G85SsJuWS0CIQCmnRoczsw5g37h/9rUP3S1UM7NnLyjmrrqGDNd1IadIQ==";
    					
    			
    			String certificate="MIIEoTCCBEegAwIBAgIDBGkTMAoGCCqGSM49BAMCMGwxCzAJBgNVBAYTAklOMRQw\r\n"
    					+ "EgYDVQQIDAtNYWhhcmFzaHRyYTENMAsGA1UEBwwEUFVORTEOMAwGA1UECgwFQy1E\r\n"
    					+ "QUMxKDAmBgNVBAMMH1Rlc3QgQy1EQUMgU3ViIENBIGZvciBlS1lDIDIwMjIwHhcN\r\n"
    					+ "MjQwOTI3MDgxNzU5WhcNMjQwOTI3MDg0NzU5WjCCAUcxDjAMBgNVBAYTBUluZGlh\r\n"
    					+ "MRYwFAYDVQQIEw1VdHRhciBQcmFkZXNoMREwDwYDVQQKEwhQZXJzb25hbDEbMBkG\r\n"
    					+ "A1UEAxMSU3VtaXQgS3VtYXIgU2hhcm1hMQ8wDQYDVQQREwYyMjE0MDQxUjBQBgNV\r\n"
    					+ "BC0DSQAwMTAwMDQyN0twTTJqUDhxK0QzcEE5d0lwWXp0R2NrOEJxQUFmMjhMZEJM\r\n"
    					+ "L3NoTWpUZDBzTTBwREZ0R1ZpeUZteSs4R0czMGcxKTAnBgNVBEETIDhlNGI2YjZi\r\n"
    					+ "Y2Q5YzRjN2NhMDI2NmNmODVmOWJjNmU5MQ0wCwYDVQQMEwQ0NjcyMU4wTAYDVQQu\r\n"
    					+ "E0UxOTk5TTk2NzJmZTgxNmRiMGYyZGUwZjU3M2Q0MjVlZjU3YjUwZTAzNGEyNDk2\r\n"
    					+ "ODM4NTRhZWQzNWMxOTY3YzYxZDljZTAwWTATBgcqhkjOPQIBBggqhkjOPQMBBwNC\r\n"
    					+ "AARK42rdGZn3POfbIr3m74KrWqYNnkW5V4bNlIFf8vPnOTyYwfhZeL0GMSn3EIUP\r\n"
    					+ "9scPnS7iwBG3Mig0YXC0OpBdo4IB+TCCAfUwCQYDVR0TBAIwADAdBgNVHQ4EFgQU\r\n"
    					+ "O799wN5EGIvTLeD3s9DRRyrQu1AwHwYDVR0jBBgwFoAUFJmMutgoTZIKwXYgQ3qs\r\n"
    					+ "ph5Go+8wDgYDVR0PAQH/BAQDAgbAMD4GA1UdHwQ3MDUwM6AxoC+GLWh0dHBzOi8v\r\n"
    					+ "ZXNpZ24uY2RhYy5pbi9jYS9DREFDU3RnU3ViQ0EyMDIyLmNybDCCAVYGA1UdIASC\r\n"
    					+ "AU0wggFJMIIBAQYHYIJkZAEJAjCB9TAwBggrBgEFBQcCARYkaHR0cHM6Ly9lc2ln\r\n"
    					+ "bi5jZGFjLmluL2NhL0NQUy9DUFMucGRmMIHABggrBgEFBQcCAjCBszA+FjpDZW50\r\n"
    					+ "cmUgZm9yIERldmVsb3BtZW50IG9mIEFkdmFuY2VkIENvbXB1dGluZyAoQy1EQUMp\r\n"
    					+ "LCBQdW5lMAAacVRoaXMgQ1BTIGlzIG93bmVkIGJ5IEMtREFDIGFuZCB1c2VycyBh\r\n"
    					+ "cmUgcmVxdWVzdGVkIHRvIHJlYWQgQ1BTIGJlZm9yZSB1c2luZyB0aGUgQy1EQUMg\r\n"
    					+ "Q0EncyBjZXJ0aWZpY2F0aW9uIHNlcnZpY2VzMEIGB2CCZGQCBAEwNzA1BggrBgEF\r\n"
    					+ "BQcCAjApGidBYWRoYWFyIG9ubGluZSBlS1lDIE9UUCAtIFNpbmdsZSBGYWN0b3Iw\r\n"
    					+ "CgYIKoZIzj0EAwIDSAAwRQIgOva5NrjXW3/fpojMChIUZ0d5A3wRaV/ssvgeI+UJ\r\n"
    					+ "XIwCIQDLiokOwB9zvCY85Tv3iLZIVwWkYICFR8j1L8V1pUk00Q==";
    			
            	String src = RealPath.REAL_PATH + "\\esignrequest\\document\\TEST.pdf";
            	String dest = RealPath.REAL_PATH + "\\esignresponse\\document\\TESTDEST.pdf";
            	
            	File pdfFilePath = new File(src);
            	
            	PdfChecksum p = new PdfChecksum();
            	
               System.out.println("Before Signing: "+p.getHashOfFile(pdfFilePath));
            	
            	PdfEmbedder pdfSign = new PdfEmbedder(); 
            	pdfSign.pdfSigner(src, dest, signatureValue, certificate);
            	
            	 System.out.println("After Signing: " + p.getHashOfFile(new File(dest)));
            	
            	 return ResponseEntity.ok("Document signed successfully with certificate");
    

                   

       
    }
        
}

