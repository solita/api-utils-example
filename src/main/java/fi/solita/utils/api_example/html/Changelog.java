package fi.solita.utils.api_example.html;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import fi.solita.utils.api.html.Page;
import fi.solita.utils.api_example.versioned.v0_1.Version0_1;

public abstract class Changelog implements Renderable {
    public static Renderable page() {
        return new Page("Changelog", "?", "?", new Changelog() {});
    }
    
    public static Renderable version(final String version, final Renderable component) throws IOException {
        return new Renderable() {
            @Override
            public void renderOn(HtmlCanvas html) throws IOException {
                html.section()
                        .h3().write(version)._h3()
                        .render(component)
                    ._section();
            }
        };
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
          html.render(version(Version0_1.VERSION, new Renderable() {
            @Override
            public void renderOn(HtmlCanvas html) throws IOException {
                html.ul()
                        .li().write("Example stuff.")._li()
                    ._ul();
            }
        }));
    }

}
