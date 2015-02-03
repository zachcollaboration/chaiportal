package ke.co.chaiportal.utilities;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

@JsonDeserialize(using=CustomerDateAndTimeDeserialize.class)
public class CustomerDateAndTimeDeserialize extends JsonDeserializer<Timestamp> {

	private SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    @Override
    public Timestamp deserialize(JsonParser paramJsonParser,
            DeserializationContext paramDeserializationContext)
            throws IOException, JsonProcessingException {
        String str = paramJsonParser.getText().trim();
        try {
            return new Timestamp(dateFormat.parse(str).getTime());
        } catch (ParseException e) {

        }
        return new Timestamp(paramDeserializationContext.parseDate(str).getTime());
    }

}
