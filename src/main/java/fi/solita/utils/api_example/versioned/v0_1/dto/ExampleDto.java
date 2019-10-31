package fi.solita.utils.api_example.versioned.v0_1.dto;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import fi.solita.utils.functional.Tuple;
import fi.solita.utils.functional.lens.Builder;
import fi.solita.utils.meta.MetaNamedMember;

public class ExampleDto {
    public static final List<? extends MetaNamedMember<ExampleDto,?>> FIELDS = Tuple.asList(ExampleDto_.$Fields());
    public static final Builder<ExampleDto> BUILDER = Builder.of(ExampleDto_.$Fields(), ExampleDto_.$);
    public static final Builder<?>[] BUILDERS = { BUILDER };
    
    public final String name;
    public final Coordinate location;
    
    public ExampleDto(String name, Coordinate location) {
        this.name = name;
        this.location = location;
    }
}
