package fi.solita.utils.api_example.versioned.v0_1.serializer;

import static fi.solita.utils.functional.Collections.newList;
import static fi.solita.utils.functional.Collections.newMap;
import static fi.solita.utils.functional.Functional.concat;
import static fi.solita.utils.functional.Option.Some;

import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;

import fi.solita.utils.api.base.CsvModule;
import fi.solita.utils.api.base.CsvSerializer;
import fi.solita.utils.api.base.CsvSerializers;
import fi.solita.utils.api.types.SRSName;
import fi.solita.utils.functional.Pair;


public class CsvSerializersV0_1 extends CsvSerializers {
    
    private final SerializersV0_1 s;

    public CsvSerializersV0_1(SerializersV0_1 s) {
        super(s);
        this.s = s;
    }
    
    public final Map.Entry<? extends Class<?>, ? extends CsvSerializer<?>> coordinate = Pair.of(Coordinate.class, new CsvSerializer<Coordinate>() {
        @Override
        public Cells render(CsvModule module, Coordinate value) {
            double[] cs = s.ser(value);
            return new Cells(newList(Double.toString(cs[0]), Double.toString(cs[1])), cs[0] + "," + cs[1]);
        }
        
        @Override
        public List<String> columns(CsvModule module, Class<Coordinate> type) {
            return newList("coord1", "coord2");
        }
    });
    
    @Override
    public Map<Class<?>,CsvSerializer<?>> serializers() { return newMap(
        concat(super.serializers().entrySet(), newList(
            coordinate
        )));
    }
}
