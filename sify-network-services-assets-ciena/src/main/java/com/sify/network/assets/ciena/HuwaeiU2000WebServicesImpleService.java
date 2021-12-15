
package com.sify.network.assets.ciena;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "HuwaeiU2000WebServicesImpleService", targetNamespace = "http://u2000.huwaei.assets.network.sify.com/", wsdlLocation = "http://192.168.0.100:8080/ws/huwaei?wsdl")
public class HuwaeiU2000WebServicesImpleService
    extends Service
{

    private final static URL HUWAEIU2000WEBSERVICESIMPLESERVICE_WSDL_LOCATION;
    private final static WebServiceException HUWAEIU2000WEBSERVICESIMPLESERVICE_EXCEPTION;
    private final static QName HUWAEIU2000WEBSERVICESIMPLESERVICE_QNAME = new QName("http://u2000.huwaei.assets.network.sify.com/", "HuwaeiU2000WebServicesImpleService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://192.168.0.100:8080/ws/huwaei?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        HUWAEIU2000WEBSERVICESIMPLESERVICE_WSDL_LOCATION = url;
        HUWAEIU2000WEBSERVICESIMPLESERVICE_EXCEPTION = e;
    }

    public HuwaeiU2000WebServicesImpleService() {
        super(__getWsdlLocation(), HUWAEIU2000WEBSERVICESIMPLESERVICE_QNAME);
    }

    public HuwaeiU2000WebServicesImpleService(WebServiceFeature... features) {
        super(__getWsdlLocation(), HUWAEIU2000WEBSERVICESIMPLESERVICE_QNAME, features);
    }

    public HuwaeiU2000WebServicesImpleService(URL wsdlLocation) {
        super(wsdlLocation, HUWAEIU2000WEBSERVICESIMPLESERVICE_QNAME);
    }

    public HuwaeiU2000WebServicesImpleService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, HUWAEIU2000WEBSERVICESIMPLESERVICE_QNAME, features);
    }

    public HuwaeiU2000WebServicesImpleService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public HuwaeiU2000WebServicesImpleService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns HuwaeiU2000WebServices
     */
    @WebEndpoint(name = "HuwaeiU2000WebServicesImplePort")
    public HuwaeiU2000WebServices getHuwaeiU2000WebServicesImplePort() {
        return super.getPort(new QName("http://u2000.huwaei.assets.network.sify.com/", "HuwaeiU2000WebServicesImplePort"), HuwaeiU2000WebServices.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns HuwaeiU2000WebServices
     */
    @WebEndpoint(name = "HuwaeiU2000WebServicesImplePort")
    public HuwaeiU2000WebServices getHuwaeiU2000WebServicesImplePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://u2000.huwaei.assets.network.sify.com/", "HuwaeiU2000WebServicesImplePort"), HuwaeiU2000WebServices.class, features);
    }

    private static URL __getWsdlLocation() {
        if (HUWAEIU2000WEBSERVICESIMPLESERVICE_EXCEPTION!= null) {
            throw HUWAEIU2000WEBSERVICESIMPLESERVICE_EXCEPTION;
        }
        return HUWAEIU2000WEBSERVICESIMPLESERVICE_WSDL_LOCATION;
    }

}
