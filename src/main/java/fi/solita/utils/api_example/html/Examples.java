package fi.solita.utils.api_example.html;

import static fi.solita.utils.api.html.UI.definition;
import static fi.solita.utils.api.html.UI.link;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;
import org.rendersnake.ext.servlet.ServletUtils;

import fi.solita.utils.api.html.Page;

public abstract class Examples implements Renderable {
    public static Renderable page() {
        return new Page("Examples", "?", "?", new Examples() {});
    }
    
    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        String ctx = ServletUtils.requestFor(html).getContextPath();
        html.section().dl()
            .render(definition(
"Get all examples",
link(ctx + "/latest/examples.html")
            ))
            ._dl()._section();
    }

}
