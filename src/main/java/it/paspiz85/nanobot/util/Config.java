package it.paspiz85.nanobot.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Properties;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Utility to manage configuration.
 *
 * @author paspiz85
 *
 */
public final class Config {

    private static final String URL = "http://paspiz85.altervista.org/nanobot/config.php?v=${VERSION}";
    
    private static final String VERSION = "0.3.0"; 

    private static Config instance;

    public static Config instance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    private Properties properties;
    
    private String url = URL.replace("${VERSION}", VERSION);

    private Config() {
    }

    public Properties getProperties() {
        if (properties == null) {
            final Properties props = new Properties();
            try {
                try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                    HttpGet request = new HttpGet(url);
                    request.setHeader("X-Nanobot-UUID", Settings.instance().getUuid().toString());
                    request.setHeader("X-Nanobot-User", Settings.instance().getUserMailAddress());
                    try (CloseableHttpResponse response = httpClient.execute(request)){
                        StatusLine statusLine = response.getStatusLine();
                        if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                            throw new IOException(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                        }
                        try (InputStream in = response.getEntity().getContent()) {
                            props.load(in);
                        }
                    }
                }
            } catch (final IOException e) {
                throw new ExceptionInInitializerError(e);
            }
            properties = props;
        }
        return properties;
    }

    public String getProperty(final String name) {
        return getProperties().getProperty(name);
    }
}
