package fi.solita.utils.api_example.common;

import static fi.solita.utils.functional.Collections.newList;
import static fi.solita.utils.functional.Functional.cons;
import static fi.solita.utils.functional.Functional.map;
import static fi.solita.utils.functional.Functional.mkString;
import static fi.solita.utils.functional.FunctionalA.concat;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rendersnake.ext.spring.HtmlCanvasFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import fi.solita.utils.api.ResponseUtil;
import fi.solita.utils.api.SwaggerSupport;
import fi.solita.utils.api.html.FAQ;
import fi.solita.utils.api_example.ExampleAPIApplication;
import fi.solita.utils.api_example.common.service.SupportService;
import fi.solita.utils.api_example.html.Changelog;
import fi.solita.utils.api_example.html.Examples;
import fi.solita.utils.api_example.versioned.ExampleAPIControllerV0_1;


@Controller
@RequestMapping(method = GET)
public class RootController {

    /** keep this referring to the root controller of the latest version */
    private static final Class<?> latestVersion = ExampleAPIControllerV0_1.class;
    
    @Autowired
    private SupportService support;
    
    @RequestMapping("/")
    public @ResponseBody ModelAndView getIndex() {
        return new ModelAndView("index.html");
    }
    
    @RequestMapping(value = "faq.html", produces = MediaType.TEXT_HTML_VALUE)
    public void faq(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HtmlCanvasFactory.createCanvas(req, resp, resp.getWriter()).render(FAQ.page("?", "?"));
    }
    
    @RequestMapping(value = "changelog.html", produces = MediaType.TEXT_HTML_VALUE)
    public void changelog(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HtmlCanvasFactory.createCanvas(req, resp, resp.getWriter()).render(Changelog.page());
    }
    
    @RequestMapping(value = "examples.html", produces = MediaType.TEXT_HTML_VALUE)
    public void examples(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HtmlCanvasFactory.createCanvas(req, resp, resp.getWriter()).render(Examples.page());
    }
    
    @RequestMapping({"/latest", "/latest/**"})
    public void latest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ResponseUtil.respondLatest(latestVersion, req, res);
    }
    
    @RequestMapping(value = "seed.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody byte[] seed(HttpServletResponse response) {
        // alkuun kommenttina kantaversio ja aikaväli jotta etag vaihtuu vuorokauden vaihteessa
        String ret = mkString("\n", cons("#" + support.getRevisionsNewestFirst().first(), cons("#" + SwaggerSupport.intervalNow(), SEED)));
        byte[] resp = ret.getBytes(Charset.forName("UTF-8"));
        response.setHeader(HttpHeaders.ETAG, ResponseUtil.calculateETag(resp).replaceFirst("W/\"", "W/\"seedhint"));
        return resp;
    }
    
    private static final List<String> FRONTPAGE_QUICKLINKS = newList(
            "latest/examples.{format}"
        );
    
    private static final List<String> OTHERS = newList(
            
        );
    
    private static final List<String> SWAGGER = newList(map(v -> {
        try {
            return "v2/api-docs?group=" + v.getField("VERSION").get(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }, ExampleAPIApplication.PUBLISHED_VERSIONS));
    
    private static final List<String> SEED = newList(concat(
            SWAGGER,
            map(x -> x.replace("{format}", "html"), FRONTPAGE_QUICKLINKS),
            map(x -> x.replace("{format}", "html"), OTHERS)));
}
