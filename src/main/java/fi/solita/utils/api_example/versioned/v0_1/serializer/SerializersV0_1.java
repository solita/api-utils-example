package fi.solita.utils.api_example.versioned.v0_1.serializer;

import com.vividsolutions.jts.geom.Coordinate;

import fi.solita.utils.api.base.Serializers;

public class SerializersV0_1 extends Serializers {

    public double[] ser(Coordinate v) {
        return new double[] {v.x, v.y};
    }
}
