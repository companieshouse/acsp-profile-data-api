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

@ExtendWith(MockitoExtension.class)
class AuthInterceptorTest {

    private static final String USER = "user";
    private static final String STREAM = "stream";

    @InjectMocks
    private AuthInterceptor authInterceptor;

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
    void preHandleReturnsFalseIfEricIdentityTypeOAuth() {
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
    void preHandleReturnsTrueIfIdentityTypeApiKeyAndHasInternalPrivileges() {
        // given
        when(request.getHeader(AuthConstants.ERIC_IDENTITY)).thenReturn(USER);
        when(request.getHeader(AuthConstants.ERIC_IDENTITY_TYPE)).thenReturn(AuthConstants.API_KEY_IDENTITY_TYPE);
        when(request.getHeader(AuthConstants.ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER))
                .thenReturn(AuthConstants.INTERNAL_APP_PRIVILEGE);

        // when
        boolean actual = authInterceptor.preHandle(request, response, handler);

        // then
        assertTrue(actual);
        verifyNoInteractions(response);
    }

    @Test
    void preHandleReturnsFalseIfIdentityTypeApiKeyAndNoInternalPrivileges() {
        // given
        when(request.getHeader(AuthConstants.ERIC_IDENTITY)).thenReturn(USER);
        when(request.getHeader(AuthConstants.ERIC_IDENTITY_TYPE)).thenReturn(AuthConstants.API_KEY_IDENTITY_TYPE);

        // when
        boolean actual = authInterceptor.preHandle(request, response, handler);

        // then
        assertFalse(actual);
        verify(response).setStatus(403);
    }
}
