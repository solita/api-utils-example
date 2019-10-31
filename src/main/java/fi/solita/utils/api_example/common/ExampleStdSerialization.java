package fi.solita.utils.api_example.common;

import static fi.solita.utils.api.ResponseUtil.respondWithEternalCaching;

import java.net.URI;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vividsolutions.jts.geom.Envelope;

import fi.solita.utils.api.Includes;
import fi.solita.utils.api.RequestUtil.ETags;
import fi.solita.utils.api.StdSerialization;
import fi.solita.utils.api.base.VersionBase;
import fi.solita.utils.api.format.CsvConversionService;
import fi.solita.utils.api.format.ExcelConversionService;
import fi.solita.utils.api.format.HtmlConversionService.HtmlTitle;
import fi.solita.utils.api.format.JsonConversionService;
import fi.solita.utils.api.format.PngConversionService;
import fi.solita.utils.api.format.SerializationFormat;
import fi.solita.utils.api.format.geojson.GeometryObject;
import fi.solita.utils.api.types.SRSName;
import fi.solita.utils.api_example.format.ExampleHtmlConversionService;
import fi.solita.utils.functional.Apply;
import fi.solita.utils.functional.Pair;
import fi.solita.utils.functional.Supplier;
import fi.solita.utils.functional.lens.Lens;

public class ExampleStdSerialization extends StdSerialization<Envelope> {
    public ExampleStdSerialization(VersionBase version, URI versionBaseUri) {
        super(new JsonConversionService(version.jsonModule),
              new JsonConversionService(version.jsonModule),
              new ExampleHtmlConversionService(version.htmlModule),
              new CsvConversionService(version.csvModule),
              new ExcelConversionService(version.excelModule),
              new PngConversionService(versionBaseUri));
    }

    @Override
    public Envelope bounds2envelope(Envelope place) {
        return place;
    }
    
    // Add your favourite serialization operations here, if you so wish
    
    public <DTO, KEY, SPATIAL> void cachingSpatialBoundedCollection(
            HttpServletRequest req,
            HttpServletResponse res,
            Envelope bbox,
            SRSName srsName,
            Pair<SerializationFormat, ETags> formatAndETags,
            Includes<DTO> includes,
            Supplier<? extends Collection<DTO>> data,
            Apply<DTO, DTO> dataTransformer,
            HtmlTitle title,
            Lens<? super DTO, SPATIAL> geometryLens,
            Apply<? super SPATIAL, ? extends GeometryObject> toGeojson) {
        byte[] response = super.stdSpatialBoundedCollection(req, res, bbox, srsName, formatAndETags, includes, data, dataTransformer, title, geometryLens, toGeojson);
        respondWithEternalCaching(res, response, formatAndETags.right);
    }
    
    public <DTO, KEY, SPATIAL> void cachingSpatialSingle(
            HttpServletRequest req,
            HttpServletResponse res,
            SRSName srsName,
            Pair<SerializationFormat, ETags> formatAndETags,
            Includes<DTO> includes,
            Supplier<DTO> data,
            Apply<DTO, DTO> dataTransformer,
            HtmlTitle title,
            Lens<? super DTO, SPATIAL> geometryLens,
            Apply<? super SPATIAL, ? extends GeometryObject> toGeojson) {
        byte[] response = super.stdSpatialSingle(req, res, srsName, formatAndETags, includes, data, dataTransformer, title, geometryLens, toGeojson);
        respondWithEternalCaching(res, response, formatAndETags.right);
    }
}