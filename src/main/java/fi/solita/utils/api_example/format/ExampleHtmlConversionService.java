package fi.solita.utils.api_example.format;

import static fi.solita.utils.functional.Option.None;

import java.lang.reflect.AccessibleObject;

import fi.solita.utils.api.base.HtmlModule;
import fi.solita.utils.api.format.HtmlConversionService;
import fi.solita.utils.functional.Option;

public class ExampleHtmlConversionService extends HtmlConversionService {
    public ExampleHtmlConversionService(HtmlModule htmlModule) {
        super(htmlModule);
    }

    @Override
    protected Option<String> docName_en(AccessibleObject member) {
        return None();
    }

    @Override
    protected Option<String> docDescription_en(AccessibleObject member) {
        return None();
    }

    @Override
    protected Option<String> docDescription(AccessibleObject member) {
        return None();
    }
}