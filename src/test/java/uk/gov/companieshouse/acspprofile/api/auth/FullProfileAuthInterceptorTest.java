package uk.gov.companieshouse.acspprofile.api.auth;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

@ExtendWith(MockitoExtension.class)
class FullProfileAuthInterceptorTest {

    private static final String USER = "user";
    private static final String STREAM = "stream";

    @InjectMocks
    private FullProfileAuthInterceptor authInterceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @Test
    void preHandleReturnsFalseIfEricIdentityIsNull() {
        // when
        boolean actual = authInterceptor.preHandle(request, response, handler);

        // then
        assertFalse(actual);
        verify(response).setStatus(401);
    }

    @Test
    void preHandleReturnsFalseIfEricIdentityIsEmpty() {
        // given
        when(request.getHeader(AuthConstants.ERIC_IDENTITY_TYPE)).thenReturn(null);
        when(request.getHeader(AuthConstants.ERIC_IDENTITY)).thenReturn("");

        // when
        boolean actual = authInterceptor.preHandle(request, response, handler);

        // then
        assertFalse(actual);
        verify(response).setStatus(401);
    }

    @Test
    void preHandleReturnsFalseIfEricIdentityTypeIsNull() {
        // given
        when(request.getHeader(AuthConstants.ERIC_IDENTITY_TYPE)).thenReturn(null);
        when(request.getHeader(AuthConstants.ERIC_IDENTITY)).thenReturn(USER);
        // when
        boolean actual = authInterceptor.preHandle(request, response, handler);

        // then
        assertFalse(actual);
        verify(response).setStatus(401);
    }

    @Test
    void preHandleReturnsFalseIfEricIdentityTypeIsEmpty() {
        // given
        when(request.getHeader(AuthConstants.ERIC_IDENTITY)).thenReturn(USER);
        when(request.getHeader(AuthConstants.ERIC_IDENTITY_TYPE)).thenReturn("");

        // when
        boolean actual = authInterceptor.preHandle(request, response, handler);

        // then
        assertFalse(actual);
        verify(response).setStatus(401);
    }

    @Test
    void preHandleReturnsFalseIfEricIdentityTypeIsInvalid() {
        // given
        when(request.getHeader(AuthConstants.ERIC_IDENTITY)).thenReturn(USER);
        when(request.getHeader(AuthConstants.ERIC_IDENTITY_TYPE)).thenReturn(STREAM);

        // when
        boolean actual = authInterceptor.preHandle(request, response, handler);

        // then
        assertFalse(actual);
        verify(response).setStatus(401);
    }

    @Test
    void preHandleReturnsFalseIfEricIdentitySetAndIdentityTypeOAuth() {
        // given
        when(request.getHeader(AuthConstants.ERIC_IDENTITY)).thenReturn(USER);
        when(request.getHeader(AuthConstants.ERIC_IDENTITY_TYPE)).thenReturn(AuthConstants.OAUTH2_IDENTITY_TYPE);

        // when
        boolean actual = authInterceptor.preHandle(request, response, handler);

        // then
        assertFalse(actual);
        verify(response).setStatus(401);
    }

    @Test
    void preHandleReturnsFalseIfCorrectAuthenticationButNoSensitiveDataPrivileges() {
        // given
        when(request.getMethod()).thenReturn(HttpMethod.GET.name());
        when(request.getHeader(AuthConstants.ERIC_IDENTITY)).thenReturn(USER);
        when(request.getHeader(AuthConstants.ERIC_IDENTITY_TYPE)).thenReturn(AuthConstants.API_KEY_IDENTITY_TYPE);

        // when
        boolean actual = authInterceptor.preHandle(request, response, handler);

        // then
        assertFalse(actual);
        verify(response).setStatus(403);
    }

    @Test
    void preHandleReturnsFalseIfCorrectAuthenticationButHasInternalPrivileges() {
        // given
        when(request.getMethod()).thenReturn(HttpMethod.PATCH.name());
        when(request.getHeader(AuthConstants.ERIC_IDENTITY)).thenReturn(USER);
        when(request.getHeader(AuthConstants.ERIC_IDENTITY_TYPE)).thenReturn(
                AuthConstants.API_KEY_IDENTITY_TYPE);
        when(request.getHeader(
                AuthConstants.ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER)).thenReturn(
                AuthConstants.INTERNAL_APP_PRIVILEGE);

        // when
        boolean actual = authInterceptor.preHandle(request, response, handler);

        // then
        assertFalse(actual);
        verify(response).setStatus(403);
    }

    @Test
    void preHandleReturnsFalseIfCorrectAuthenticationAndAuthorisationButMethodNotGET() {
        // given
        when(request.getMethod()).thenReturn(HttpMethod.PUT.name());
        when(request.getHeader(AuthConstants.ERIC_IDENTITY)).thenReturn(USER);
        when(request.getHeader(AuthConstants.ERIC_IDENTITY_TYPE)).thenReturn(AuthConstants.API_KEY_IDENTITY_TYPE);
        when(request.getHeader(AuthConstants.ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER))
                .thenReturn(AuthConstants.SENSITIVE_DATA_PRIVILEGE);

        // when
        boolean actual = authInterceptor.preHandle(request, response, handler);

        // then
        assertFalse(actual);
        verify(response).setStatus(403);
    }

    @Test
    void preHandleReturnsTrueIfCorrectAuthenticationAndAuthorisation() {
        // given
        when(request.getMethod()).thenReturn(HttpMethod.GET.name());
        when(request.getHeader(AuthConstants.ERIC_IDENTITY)).thenReturn(USER);
        when(request.getHeader(AuthConstants.ERIC_IDENTITY_TYPE)).thenReturn(AuthConstants.API_KEY_IDENTITY_TYPE);
        when(request.getHeader(AuthConstants.ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER))
                .thenReturn(AuthConstants.SENSITIVE_DATA_PRIVILEGE);

        // when
        boolean actual = authInterceptor.preHandle(request, response, handler);

        // then
        assertTrue(actual);
        verifyNoInteractions(response);
    }
}
