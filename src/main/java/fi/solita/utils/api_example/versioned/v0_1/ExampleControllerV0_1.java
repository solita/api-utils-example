package fi.solita.utils.api_example.versioned.v0_1;

import static fi.solita.utils.api.MemberUtil.resolveIncludes;
import static fi.solita.utils.api.RequestUtil.resolveQueryParams;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

import fi.solita.utils.api.Includes;
import fi.solita.utils.api.RequestUtil.ETags;
import fi.solita.utils.api.format.HtmlConversionService;
import fi.solita.utils.api.format.SerializationFormat;
import fi.solita.utils.api.format.geojson.Point_;
import fi.solita.utils.api.types.Count;
import fi.solita.utils.api.types.Filters;
import fi.solita.utils.api.types.Revision;
import fi.solita.utils.api.types.SRSName;
import fi.solita.utils.api_example.Transform;
import fi.solita.utils.api_example.common.ExampleStdSerialization;
import fi.solita.utils.api_example.common.service.ExampleService;
import fi.solita.utils.api_example.common.service.SupportService;
import fi.solita.utils.api_example.versioned.v0_1.dto.ExampleDto;
import fi.solita.utils.api_example.versioned.v0_1.dto.ExampleDto_;
import fi.solita.utils.functional.Option;
import fi.solita.utils.functional.Pair;
import fi.solita.utils.functional.Supplier;
import fi.solita.utils.functional.lens.Lens;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/" + Version0_1.VERSION  + "/", method = GET)
@Api(tags = "Examples", description = " ")
class ExampleControllerV0_1_redirects {
    @Autowired
    private SupportService support;

    @RequestMapping("/examples.*")
    void kokoonpanot(HttpServletRequest req, HttpServletResponse res) {
        support.redirectToCurrentRevision(req, res);
    }
    
    @RequestMapping("/examples/{id}.*")
    void kokoonpano(HttpServletRequest req, HttpServletResponse res) {
        support.redirectToCurrentRevision(req, res);
    }
}

@Controller
@RequestMapping(value = "/" + Version0_1.VERSION  + "/", method = GET)
@Api(tags = "Examples", description = " ")
public class ExampleControllerV0_1 {
    
    @Autowired
    private ExampleService exampleService;
    
    @Autowired
    private SupportService support;
    
    private Version0_1 version = new Version0_1();
    private ExampleStdSerialization serialization = new ExampleStdSerialization(version, URI.create("http://localhost/" + version.getVersion()));
    
    @RequestMapping("/{revision}/examples.{format}")
    @ApiOperation(value = "Examples")
    public List<ExampleDto> examples(
            HttpServletRequest req,
            HttpServletResponse res,
            @PathVariable("revision")                           Revision revision,
            @RequestParam(value="bbox",         required=false) Envelope b,
            @RequestParam(value="count",        required=false) Count c,
            @RequestParam(value="cql_filter",   required=false) Filters filters,
            @RequestParam(value="propertyName", required=false) List<String> propertyName,
            @RequestParam(value="srsName",      required=false) SRSName s
            ) {
        
        Envelope bbox = Transform.resolveBBox(b, filters);
        Count count = Option.of(c).getOrElse(Count.DEFAULT);
        SRSName srsName = Option.of(s).getOrElse(Transform.DEFAULT_SRS);
        
        Lens<ExampleDto, Coordinate> geometryLens = Lens.of(ExampleDto_.location, ExampleDto.BUILDER);
        
        for (Pair<SerializationFormat,ETags> formatAndETags: support.checkRevisionAndUrlAndResolveFormat(revision, req, res, resolveQueryParams(ExampleControllerV0_1_.examples))) {
            Includes<ExampleDto> includes = resolveIncludes(formatAndETags.left, propertyName, ExampleDto.FIELDS, ExampleDto.BUILDERS);
            Supplier<List<ExampleDto>> data = () -> version.filter(includes, filters, exampleService.examples(count, includes));
            
            serialization.cachingSpatialBoundedCollection(req, res, bbox, srsName, formatAndETags, includes, data, Transform.coordinate(srsName, geometryLens), HtmlConversionService.title("Examples"), geometryLens, Point_.$);
        }
        
        return null;
    }
    
    @RequestMapping("/{revision}/examples/{id}.{format}")
    @ApiOperation(value = "Example")
    public ExampleDto example(
            HttpServletRequest req,
            HttpServletResponse res,
            @PathVariable("revision")                           Revision revision,
            @PathVariable("id")                                 long id,
            @RequestParam(value="propertyName", required=false) List<String> propertyName,
            @RequestParam(value="srsName",      required=false) SRSName s
            ) {
        
        SRSName srsName = Option.of(s).getOrElse(Transform.DEFAULT_SRS);
        
        Lens<ExampleDto, Coordinate> geometryLens = Lens.of(ExampleDto_.location, ExampleDto.BUILDER);
        
        for (Pair<SerializationFormat,ETags> formatAndETags: support.checkRevisionAndUrlAndResolveFormat(revision, req, res, resolveQueryParams(ExampleControllerV0_1_.example))) {
            Includes<ExampleDto> includes = resolveIncludes(formatAndETags.left, propertyName, ExampleDto.FIELDS, ExampleDto.BUILDERS);
            Supplier<ExampleDto> data = () -> version.filter(includes, exampleService.example(id, includes));
            
            serialization.cachingSpatialSingle(req, res, srsName, formatAndETags, includes, data, Transform.coordinate(srsName, geometryLens), HtmlConversionService.title("Example"), geometryLens, Point_.$);
        }
        
        return null;
    }
}
