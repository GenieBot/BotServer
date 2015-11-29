package pw.sponges.botserver.internal;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public interface Server {

    /**
     * Start the server.
     * @throws CertificateException
     * @throws SSLException
     * @throws InterruptedException
     */
    void start() throws CertificateException, SSLException, InterruptedException;

    /**
     * Stop the server.
     * @throws InterruptedException
     */
    void stop() throws InterruptedException;

}
