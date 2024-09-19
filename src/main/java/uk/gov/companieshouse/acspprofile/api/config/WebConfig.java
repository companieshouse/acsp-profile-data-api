package uk.gov.companieshouse.acspprofile.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.companieshouse.acspprofile.api.auth.AuthInterceptor;
import uk.gov.companieshouse.acspprofile.api.auth.FullProfileAuthInterceptor;

@Configuration
class WebConfig implements WebMvcConfigurer {

    private static final String FULL_PROFILE_PATH = "/**/full-profile";
    private static final String ERROR_PATH = "/error"; // NOSONAR

    private final AuthInterceptor authInterceptor;
    private final FullProfileAuthInterceptor fullProfileAuthInterceptor;

    WebConfig(AuthInterceptor authInterceptor, FullProfileAuthInterceptor fullProfileAuthInterceptor) {
        this.authInterceptor = authInterceptor;
        this.fullProfileAuthInterceptor = fullProfileAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns(FULL_PROFILE_PATH, ERROR_PATH);
        registry.addInterceptor(fullProfileAuthInterceptor)
                .addPathPatterns(FULL_PROFILE_PATH)
                .excludePathPatterns(ERROR_PATH);
    }
}
