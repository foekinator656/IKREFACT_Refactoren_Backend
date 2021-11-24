package nl.hsleiden.ipsen2.groep3.bouncer;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

public class PrepareRequest {
    public ObjectMapper mapper;
    private ObjectWriter writer;

    public PrepareRequest() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);

        this.writer = this.mapper.writer().withDefaultPrettyPrinter();
    }

    public String getContentAsString (Object object) throws IOException {
        return writer.writeValueAsString(object);
    }
}
