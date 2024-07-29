package uk.gov.companieshouse.acspprofile.api.auth;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

import java.util.Optional;

import static uk.gov.companieshouse.acspprofile.api.ACSPProfileApplication.NAMESPACE;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthenticationConstants.*;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @Nonnull HttpServletResponse response, @Nullable Object handler) {

        String ericIdentity = request.getHeader(ERIC_IDENTITY);
        String ericIdentityType = request.getHeader(ERIC_IDENTITY_TYPE);

        if (StringUtils.isBlank(ericIdentity)) {
            LOGGER.error("Request received without eric identity", DataMapHolder.getLogMap());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        if (!(API_KEY_IDENTITY_TYPE.equalsIgnoreCase(ericIdentityType)
                || (OAUTH2_IDENTITY_TYPE.equalsIgnoreCase(ericIdentityType)))) {
            LOGGER.error("Request received without correct eric identity type", DataMapHolder.getLogMap());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        if (!isKeyAuthorised(request, ericIdentityType)) {
            LOGGER.error("Supplied key does not have sufficient privileges", DataMapHolder.getLogMap());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }

    private boolean isKeyAuthorised(HttpServletRequest request, String ericIdentityType) {
        String[] privileges = getApiKeyPrivileges(request);

        return request.getMethod().equals(HttpMethod.GET.name())
                || (ericIdentityType.equalsIgnoreCase(API_KEY_IDENTITY_TYPE)
                && ArrayUtils.contains(privileges, INTERNAL_APP_PRIVILEGE));
    }

    private String[] getApiKeyPrivileges(HttpServletRequest request) {
        String commaSeparatedPrivilegeString = request.getHeader(
                ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER);

        return Optional.ofNullable(commaSeparatedPrivilegeString)
                .map(s -> s.split(","))
                .orElse(new String[]{});
    }
}
