package fi.solita.utils.api_example.versioned.v0_1.serializer;

import static fi.solita.utils.functional.Collections.newList;
import static fi.solita.utils.functional.Collections.newMap;
import static fi.solita.utils.functional.Functional.concat;

import java.util.Map;

import org.springframework.core.convert.converter.Converter;

import com.vividsolutions.jts.geom.Coordinate;

import fi.solita.utils.api.base.HttpSerializers;
import fi.solita.utils.functional.Pair;

public class HttpSerializersV0_1 extends HttpSerializers {
    
    private final SerializersV0_1 s;

    public HttpSerializersV0_1(SerializersV0_1 s) {
        super(s);
        this.s = s;
    }
    
    public final Map.Entry<? extends Class<?>, ? extends Converter<String,Coordinate>> coordinate = Pair.of(Coordinate.class, new Converter<String, Coordinate>() {
        @Override
        public Coordinate convert(String source) {
            String[] split = source.split(",");
            if (split.length != 2) {
                throw new IllegalArgumentException(source);
            }
            
            double x = Double.valueOf(split[0].trim());
            double y = Double.valueOf(split[1].trim());
            
            return new Coordinate(x, y);
        }
    });
    
    @Override
    public Map<Class<?>,Converter<?,?>> converters() { return newMap(
        concat(super.converters().entrySet(), newList(
            coordinate
        )));
    }
}
