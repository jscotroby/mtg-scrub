package mtgscrub;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Object, Object> {

    public Object handleRequest(final Object input, final Context context) {

        int rowsEffected = 0;

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<CardEntry> criteriaRoot = criteriaBuilder.createQuery(CardEntry.class);
            Root<CardEntry> root = criteriaRoot.from(CardEntry.class);
            criteriaRoot.select(root);

            Query query = session.createQuery(criteriaRoot);
            List cardEntries = query.getResultList();

            if(cardEntries.isEmpty()) {
                CardEntry cardEntry = new CardEntry();
                cardEntry.setId(1);
                cardEntry.setName("Test");
                session.persist(cardEntry);
                session.flush();
                rowsEffected = 1;
            }

            session.getTransaction().commit();
        }

        GatewayResponseBody gatewayResponseBody = new GatewayResponseBody();
        gatewayResponseBody.setRowsEffected(rowsEffected);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        try {
            final String pageContents = this.getPageContents("https://checkip.amazonaws.com");
            String output = String.format("{ \"message\": \"hello world, I'm live\", \"location\": \"%s\" }", pageContents);
            return new GatewayResponse(new ObjectMapper().writeValueAsString(gatewayResponseBody), headers, 200);
        } catch (IOException e) {
            return new GatewayResponse("{}", headers, 500);
        }
    }

    private String getPageContents(String address) throws IOException{
        URL url = new URL(address);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
