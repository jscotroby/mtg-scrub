package mtgscrub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

public class AppTest {

  @Rule
  public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

  @Before
  public void init() {
    environmentVariables.set("RDS_USERNAME", "mtg");
    environmentVariables.set("RDS_PASSWORD", "pw");
    environmentVariables.set("RDS_DB_NAME", "mtg_scrub_test");
    environmentVariables.set("RDS_HOSTNAME", "localhost");
  }
  @Test
  public void successfulResponse() {
    App app = new App();
    GatewayResponse result = (GatewayResponse) app.handleRequest(null, null);
    assertEquals(result.getStatusCode(), 200);
    assertEquals(result.getHeaders().get("Content-Type"), "application/json");
    String content = result.getBody();
    assertNotNull(content);
    assertTrue(content.contains("\"message\""));
    assertTrue(content.contains("\"hello world, I'm live\""));
    assertTrue(content.contains("\"location\""));
  }
}
