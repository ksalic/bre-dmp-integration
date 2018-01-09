package com.example.dmp;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.dmp.client.DMPClient;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;

/**
 * @version "\$Id$" kenan
 */
public class ClientUtil {

    private static final List PROVIDERS = Arrays.asList(new JacksonJaxbJsonProvider());

    public static DMPClient createDMPClient(final String baseAddress) {
        DMPClient client = JAXRSClientFactory.create(baseAddress, DMPClient.class, PROVIDERS);
        setTimeouts(client, 2000, 2000);
        return client;
    }


    public static void setTimeouts(final Object client, final long connectionTimeout, final long receiveTimeout) {
        final ClientConfiguration config = WebClient.getConfig(client);
        HTTPConduit conduit = config.getHttpConduit();
        if (receiveTimeout != 0) {
            conduit.getClient().setReceiveTimeout(receiveTimeout);
        }
        if (connectionTimeout != 0) {
            conduit.getClient().setConnectionTimeout(connectionTimeout);
        }
    }
}
