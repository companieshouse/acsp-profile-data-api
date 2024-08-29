package uk.gov.companieshouse.acspprofile.api.auth;

import static uk.gov.companieshouse.acspprofile.api.AcspProfileApplication.NAMESPACE;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.API_KEY_IDENTITY_TYPE;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.ERIC_IDENTITY;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.ERIC_IDENTITY_TYPE;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.SENSITIVE_DATA_PRIVILEGE;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class FullProfileAuthInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    @Override
    public boolean preHandle(HttpServletRequest request,
            @Nonnull HttpServletResponse response, @Nullable Object handler) {

        String ericIdentity = request.getHeader(ERIC_IDENTITY);
        String ericIdentityType = request.getHeader(ERIC_IDENTITY_TYPE);

        if (StringUtils.isBlank(ericIdentity)) {
            LOGGER.error("Eric identity is blank", DataMapHolder.getLogMap());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        if (!API_KEY_IDENTITY_TYPE.equalsIgnoreCase(ericIdentityType)) {
            LOGGER.error("Incorrect eric identity type", DataMapHolder.getLogMap());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        if (!isKeySensitiveDataAuthorised(request)) {
            LOGGER.error("Key is not authorised", DataMapHolder.getLogMap());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }

    private boolean isKeySensitiveDataAuthorised(HttpServletRequest request) {
        String[] privileges = getApiKeyPrivileges(request);

        return request.getMethod().equals(HttpMethod.GET.name()) &&
                ArrayUtils.contains(privileges, SENSITIVE_DATA_PRIVILEGE);
    }

    private String[] getApiKeyPrivileges(HttpServletRequest request) {
        String commaSeparatedPrivilegeString = request.getHeader(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER);
        return Optional.ofNullable(commaSeparatedPrivilegeString)
                .map(s -> s.split(","))
                .orElse(new String[]{});
    }
}
