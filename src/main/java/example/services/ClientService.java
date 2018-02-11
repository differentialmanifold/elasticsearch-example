package example.services;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class ClientService {
    @Value("${es.url}")
    private String url;

    @Value("${es.port}")
    private String port;

    @Value("${es.cluster.name}")
    private String cluster;

    private TransportClient _client;

    @PostConstruct
    public void initClient() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", cluster)
                .build();
        _client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(url), Integer.parseInt(port)));
    }

    public TransportClient getClient() {
        return _client;
    }
}
