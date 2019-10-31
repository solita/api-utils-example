package fi.solita.utils.api_example;

import static fi.solita.utils.functional.Collections.newSet;
import static fi.solita.utils.functional.FunctionalA.head;
import static fi.solita.utils.functional.Option.None;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.classmate.TypeResolver;

import fi.solita.utils.api.GeneralExceptionResolver;
import fi.solita.utils.api.HttpSerializationExceptionResolver;
import fi.solita.utils.api.SwaggerSupport;
import fi.solita.utils.api.base.VersionBase;
import fi.solita.utils.api_example.versioned.v0_1.Version0_1;
import fi.solita.utils.functional.Option;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
class ExampleAPIWebConfig extends WebMvcConfigurationSupport implements BeanFactoryAware {

    @Override
    public FormattingConversionService mvcConversionService() {
        // This should in reality be different for each version, but I don't know how to do that in Spring...
        return new Version0_1().httpModule;
    }
    
    @Override
    protected void configureViewResolvers(ViewResolverRegistry registry) {
        super.configureViewResolvers(registry);
        InternalResourceViewResolver plainResolver = new InternalResourceViewResolver();
        plainResolver.setViewNames("*.html");
        registry.viewResolver(plainResolver);
    }
    
    @Autowired
    private TypeResolver typeResolver;
    
    private ConfigurableListableBeanFactory beanFactory;
    
    @Override
    public void setServletContext(ServletContext servletContext) {
        super.setServletContext(servletContext);
        for (Class<? extends VersionBase> publishedVersion: ExampleAPIApplication.PUBLISHED_VERSIONS) {
            try {
                Constructor<?> cs = head(publishedVersion.getConstructors());
                registerDocket(servletContext, (VersionBase)(cs.getParameterTypes().length == 1 ? cs.newInstance(new Object[] {null}) : cs.newInstance()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public void registerDocket(ServletContext servletContext, VersionBase version) {
        beanFactory.registerSingleton("swagger_" + version.getVersion(), SwaggerSupport.createDocket(servletContext.getContextPath(), typeResolver, version, new SwaggerSupport.ApiInfo("Example-API", "### Example API")));
    }
    
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("index.html").addResourceLocations("classpath:/index.html");
        registry.addResourceHandler("/r/**").addResourceLocations("classpath:/r/");
        
        for (Class<? extends VersionBase> publishedVersion: ExampleAPIApplication.PUBLISHED_VERSIONS) {
            try {
                SwaggerSupport.addResourceHandler(registry, (String) publishedVersion.getField("VERSION").get(null));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

@Configuration
@EnableSwagger2
class ExampleAPISwaggerConfig {
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1)
    @Bean
    public SwaggerSupport.OptionModelPropertyBuilder optionModelPropertyBuilder() {
        return new SwaggerSupport.OptionModelPropertyBuilder();
    }
    
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 2)
    @Bean
    public SwaggerSupport.DocumentingModelPropertyBuilder exampleModelPropertyBuilder() {
        return new SwaggerSupport.DocumentingModelPropertyBuilder() {
            @Override
            protected Option<String> doc(AnnotatedElement ae) {
                return None();
            }};
    }
    
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
    @Bean
    public SwaggerSupport.CustomTypeParameterBuilder customTypeParameterBuilder() {
        return new SwaggerSupport.CustomTypeParameterBuilder() {
            @Override
            protected Option<String> doc(Class<?> clazz) {
                return None();
            }
        };
    }
    
    @Bean
    public SwaggerSupport.PathSanitizerFixer customPathSanitizer() {
       return new SwaggerSupport.PathSanitizerFixer();
    }
    
    @Bean
    public SecurityConfiguration securityConfiguration() {
        return SwaggerSupport.SECURITY_CONFIGURATION;
    }
}

@Controller
@RequestMapping("/" + Version0_1.VERSION + "/swagger-resources")
class ApiResourceControllerV0_1 extends SwaggerSupport {
 public ApiResourceControllerV0_1() {
     super(Version0_1.VERSION);
 }
}


@Configuration
@EnableAutoConfiguration
@ComponentScan
public class ExampleAPIApplication extends SpringBootServletInitializer {
    public static final Set<? extends Class<? extends VersionBase>> PUBLISHED_VERSIONS = newSet(Version0_1.class);
    
    public static void main(String[] args) throws IOException {
        SpringApplication.run(ExampleAPIApplication.class, args);
    }
    
    @Bean
    public GeneralExceptionResolver generalExceptionResolver() {
        return new GeneralExceptionResolver();
    }
    
    @Bean
    public HttpSerializationExceptionResolver httpSerializationExceptionResolver() {
        return new HttpSerializationExceptionResolver();
    }
}
