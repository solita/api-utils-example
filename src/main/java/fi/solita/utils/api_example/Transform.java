package fi.solita.utils.api_example;

import static fi.solita.utils.api.Transform.transformCoordinate;
import static fi.solita.utils.api.Transform.transforming;

import java.math.BigDecimal;

import org.geotools.geometry.jts.WKTReader2;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.io.ParseException;

import fi.solita.utils.api.types.Filters;
import fi.solita.utils.api.types.SRSName;
import fi.solita.utils.functional.Function1;
import fi.solita.utils.functional.Pair;
import fi.solita.utils.functional.lens.Setter;

public class Transform {
    public static final SRSName DEFAULT_SRS = SRSName.EPSG3067;
    
    public static Envelope resolveBBox(Envelope bbox, Filters filters) {
        return fi.solita.utils.api.Transform.resolveBounds(bbox, filters, Transform_.parseWKT);
    }
    
    static Envelope parseWKT(String wkt) throws ParseException {
        return (new WKTReader2().read(wkt)).getEnvelopeInternal();
    }
    
    public static final <T> Function1<T,T> coordinate(final SRSName target, final Setter<T, Coordinate> setter) {
        return transforming(target, setter, Transform_.coordinate2.ap(DEFAULT_SRS));
    }
    
    public static final <T> Function1<T,T> coordinate(final SRSName source, final SRSName target, final Setter<T, Coordinate> setter) {
        return transforming(target, setter, Transform_.coordinate2.ap(source));
    }
    
    static Coordinate coordinate(SRSName source, SRSName target, Coordinate v) {
        if (v == null || source.equals(target)) {
            return v;
        } else {
            Pair<BigDecimal, BigDecimal> ret = transformCoordinate(source, target, Pair.of(BigDecimal.valueOf(v.x), BigDecimal.valueOf(v.y)));
            return new Coordinate(ret.left.doubleValue(), ret.right.doubleValue());
        }
    }
}
