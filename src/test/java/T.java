import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class T {
    private final CamelContext context = new DefaultCamelContext();

    @Before
    public void before() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("timer://foo?period=100")
                        .to("rabbitmq://localhost:5672/e?username=test&password=test&declare=false&publisherAcknowledgements=true")
                        .to("log:send");
                from("rabbitmq://localhost:5672?username=test&password=test&declare=false&queue=q&autoAck=false&threadPoolSize=1&exclusiveConsumer=true")
                        .to("log:receive");
            }
        });
        context.start();
    }

    @After
    public void after() throws Exception {
        context.stop();
    }

    @Test
    public void test() throws InterruptedException {
        Thread.sleep(5 * 60 * 1000);
    }
}
