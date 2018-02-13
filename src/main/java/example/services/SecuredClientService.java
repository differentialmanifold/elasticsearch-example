package example.services;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class SecuredClientService {
    @Value("${es.url}")
    private String url;

    @Value("${es.port}")
    private String port;

    @Value("${es.cluster.name}")
    private String cluster;

    @Value("${es.userPw}")
    private String userPw;

    @Value("${es.key.path}")
    private String keyPath;

    @Value("${es.crt.path}")
    private String crtPath;

    @Value("${es.ca.path}")
    private String caPath;

    private TransportClient _client;

    @PostConstruct
    public void initClient() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", cluster)
                .put("xpack.security.user", userPw)
//                .put("xpack.ssl.key", keyPath)
//                .put("xpack.ssl.certificate", crtPath)
//                .put("xpack.ssl.certificate_authorities", caPath)
//                .put("xpack.security.transport.ssl.enabled", "true")
                .build();
        _client = new PreBuiltXPackTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(url), Integer.parseInt(port)));
    }

    public TransportClient getClient() {
        return _client;
    }
}
