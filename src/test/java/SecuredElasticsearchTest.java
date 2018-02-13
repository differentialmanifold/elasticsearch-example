import example.services.SecuredClientService;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
public class SecuredElasticsearchTest {
    @Autowired
    SecuredClientService securedClientService;

    private TransportClient client;

    private int MAX_RECORD = 10;

    @Before
    public void beforTest() {
        client = securedClientService.getClient();
    }

    @Test
    public void getApiTest() {

        GetResponse response = client.prepareGet("bank", "account", "1").get();

        String firstname = (String) response.getSource().get("firstname");

        Assert.assertEquals("Amber", firstname);
    }

    @Test
    public void matchAllTest() {
        MatchAllQueryBuilder builder = QueryBuilders.matchAllQuery();

        printResponse(builder);
    }

    @Test
    public void matchTest() {
        MatchQueryBuilder builder = QueryBuilders.matchQuery("address", "620 National Drive");

        printResponse(builder);
    }

    @Test
    public void matchPhraseTest() {
        MatchPhraseQueryBuilder builder = QueryBuilders.matchPhraseQuery("address", "620 National Drive");

        printResponse(builder);
    }

    @Test
    public void termTest() {
        TermQueryBuilder builder = QueryBuilders.termQuery("age", "39");

        printResponse(builder);
    }

    @Test
    public void boolTest() {
        MatchPhraseQueryBuilder builder1 = QueryBuilders.matchPhraseQuery("firstname", "Effie");
        MatchPhraseQueryBuilder builder2 = QueryBuilders.matchPhraseQuery("address", "620 National Drive");
        MatchPhraseQueryBuilder builder3 = QueryBuilders.matchPhraseQuery("lastname", "Gates");
        MatchPhraseQueryBuilder builder4 = QueryBuilders.matchPhraseQuery("age", "39");

        BoolQueryBuilder builder = QueryBuilders.boolQuery()
                .must(builder1)
                .must(builder2)
                .should(builder3)
                .should(builder4)
                .minimumShouldMatch(1);

        printResponse(builder);
    }

    private SearchResponse search(String index, String type, QueryBuilder build) {
        return client.prepareSearch(index)
                .setTypes(type)
                .setQuery(build)
                .setSize(MAX_RECORD).get();
    }

    private void printResponse(QueryBuilder builder) {
        SearchResponse response = search("bank", "account", builder);

        System.out.println(response);
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }
}

