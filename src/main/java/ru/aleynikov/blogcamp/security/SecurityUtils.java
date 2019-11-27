package ru.aleynikov.blogcamp.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.aleynikov.blogcamp.models.User;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

public final class SecurityUtils {

    private static Logger log = LoggerFactory.getLogger(SecurityUtils.class);

    public static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
                && Stream.of(ServletHelper.RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

    public static User getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public static void destroySession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("User with username [{}] and authorities {} leave session.", authentication.getName(), authentication.getAuthorities());

        authentication.setAuthenticated(false);
        UI.getCurrent().getPage().reload();
    }

}
