package wyrzyk.archetypes;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import wyrzyk.archetypes.filters.JwtAtlassianAuthenticationFilter;
import wyrzyk.archetypes.web.WebConfiguration;

import javax.servlet.Filter;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfiguration.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{new JwtAtlassianAuthenticationFilter()};
    }
}
