package fi.solita.utils.api_example.versioned;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fi.solita.utils.api_example.versioned.v0_1.Version0_1;
import io.swagger.annotations.Api;

@Controller
@RequestMapping(value = "/" + Version0_1.VERSION, method = GET)
@Api(tags = "Yleiset - General", description = " ")
public class ExampleAPIControllerV0_1 {
    
    @RequestMapping("")
    String root() {
        return "redirect:/" + Version0_1.VERSION + "/";
    }
    
    @RequestMapping("/")
    String getIndex() {
        return "redirect:/" + Version0_1.VERSION + "/swagger-ui.html";
    }
    
}
