package fi.solita.utils.api_example.versioned.v0_1.serializer;

import static fi.solita.utils.functional.Collections.newList;
import static fi.solita.utils.functional.Collections.newMap;
import static fi.solita.utils.functional.Functional.concat;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.vividsolutions.jts.geom.Coordinate;

import fi.solita.utils.api.base.JsonSerializers;
import fi.solita.utils.functional.Pair;

public class JsonSerializersV0_1 extends JsonSerializers {
    
    private final SerializersV0_1 s;
    
    public JsonSerializersV0_1(SerializersV0_1 serializers) {
        super(serializers);
        this.s = serializers;
    }
    
    public final Map.Entry<? extends Class<?>, ? extends JsonSerializer<?>> coordinate = Pair.of(Coordinate.class, new StdSerializer<Coordinate>(Coordinate.class) {
        @Override
        public void serialize(Coordinate value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeObject(s.ser(value));
        }
    });

    @Override
    public Map<Class<?>,JsonSerializer<?>> serializers() { return newMap(
        concat(super.serializers().entrySet(), newList(
            coordinate
        )));
    }
    
    @Override
    public Map<Class<?>, Class<?>> rawTypes() { return newMap(
		concat(super.rawTypes().entrySet(), newList(
	        Pair.of(Coordinate.class, double[].class)
		))
	);
    }
}

