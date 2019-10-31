package fi.solita.utils.api_example.versioned.v0_1.serializer;

import static fi.solita.utils.functional.Collections.newList;
import static fi.solita.utils.functional.Collections.newMap;
import static fi.solita.utils.functional.Functional.concat;
import static fi.solita.utils.functional.Option.None;
import static org.rendersnake.HtmlAttributesFactory.class_;

import java.io.IOException;
import java.util.Map;

import org.rendersnake.HtmlCanvas;

import com.vividsolutions.jts.geom.Coordinate;

import fi.solita.utils.api.base.HtmlModule;
import fi.solita.utils.api.base.HtmlSerializer;
import fi.solita.utils.api.base.HtmlSerializers;
import fi.solita.utils.functional.Option;
import fi.solita.utils.functional.Pair;


public class HtmlSerializersV0_1 extends HtmlSerializers {

    private final SerializersV0_1 s;
    
    public HtmlSerializersV0_1(SerializersV0_1 s) {
        super(s);
        this.s = s;
    }
    
    protected <T extends Enum<?>> Option<String> docName_en(T member) {
        return None();
    }

    protected <T extends Enum<?>> Option<String> docDescription_en(T member) {
        return None();
    }

    protected <T extends Enum<?>> Option<String> docDescription(T member) {
        return None();
    }
    
    public final Map.Entry<? extends Class<?>, ? extends HtmlSerializer<?>> coordinate = Pair.of(Coordinate.class, new HtmlSerializer<Coordinate>() {
        @Override
        public void renderOn(Coordinate value, HtmlCanvas html, HtmlModule module) throws IOException {
            html.span(class_("type-point"))
                ._span()
                .render(module.toRenderable(value.toString()));
        }
    });
    
    @Override
    public Map<Class<?>,HtmlSerializer<?>> serializers() { return newMap(
        concat(super.serializers().entrySet(), newList(
            coordinate
        )));
    }
}
