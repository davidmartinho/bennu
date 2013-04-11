/*
 * Authenticate.java
 * 
 * Copyright (c) 2013, Instituto Superior Técnico. All rights reserved.
 * 
 * This file is part of bennu-core.
 * 
 * bennu-core is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * bennu-core is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with bennu-core. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package pt.ist.bennu.core.security;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.exceptions.AuthorizationException;
import pt.ist.bennu.core.domain.groups.DynamicGroup;
import pt.ist.bennu.core.i18n.I18N;
import pt.ist.bennu.core.util.ConfigurationManager;
import pt.ist.bennu.core.util.TransactionalThread;
import pt.ist.bennu.service.Service;

public class Authenticate {
    private static final Logger logger = LoggerFactory.getLogger(Authenticate.class);

    private static final String USER_SESSION_ATTRIBUTE = "USER_SESSION_ATTRIBUTE";

    private static final ThreadLocal<SessionUserWrapper> wrapper = new ThreadLocal<>();

    private static Set<AuthenticationListener> authenticationListeners;

    public static User login(HttpSession session, String username, String password, boolean checkPassword) {
        SessionUserWrapper user = internalLogin(username, password, checkPassword);
        session.setAttribute(USER_SESSION_ATTRIBUTE, user);
        I18N.setLocale(session, user.getUser().getPreferredLocale());

        fireLoginListeners(user.getUser());
        logger.info("Logged in user: " + user.getUsername());

        return user.getUser();
    }

    @Service
    private static SessionUserWrapper internalLogin(String username, String password, boolean checkPassword) {
        User user = User.findByUsername(username);
        if (checkPassword && ConfigurationManager.getBooleanProperty("check.login.password", true)) {
            if (user == null || user.getPassword() == null || !user.matchesPassword(password)) {
                throw AuthorizationException.authenticationFailed();
            }
        }
        if (user == null) {
            user = new User(username);
        }

        SessionUserWrapper userWrapper = new SessionUserWrapper(user);
        wrapper.set(userWrapper);
        if (Bennu.getInstance().getUsersCount() == 1) {
            DynamicGroup.getInstance("managers").grant(user);
            logger.info("Bootstrapped #managers group to user: " + user.getUsername());
        }

        return userWrapper;
    }

    public static void logout(HttpSession session) {
        final SessionUserWrapper userWrapper = (SessionUserWrapper) session.getAttribute(USER_SESSION_ATTRIBUTE);
        if (userWrapper != null) {
            internalLogout(userWrapper.getUser());
        }
        wrapper.set(userWrapper);
        session.invalidate();
    }

    @Service
    private static void internalLogout(User user) {
        user.setLastLogoutDateTime(new DateTime());
    }

    public static User getUser() {
        return wrapper.get() != null ? wrapper.get().getUser() : null;
    }

    public static boolean hasUser() {
        return wrapper.get() != null;
    }

    public static String getPrivateConstantForDigestCalculation() {
        return wrapper.get() != null ? wrapper.get().getPrivateConstantForDigestCalculation() : null;
    }

    static void updateFromSession(HttpSession session) {
        SessionUserWrapper user = (SessionUserWrapper) (session == null ? null : session.getAttribute(USER_SESSION_ATTRIBUTE));
        if (user != null) {
            final DateTime lastLogoutDateTime = user.getLastLogoutDateTime();
            if (lastLogoutDateTime == null || user.getUserCreationDateTime().isAfter(lastLogoutDateTime)) {
                wrapper.set(user);
                logger.debug("Set thread's user to: {}", user.getUsername());
            } else {
                wrapper.set(null);
            }
        } else {
            wrapper.set(null);
        }
    }

    static void clear() {
        wrapper.set(null);
    }

    public static void addAuthenticationListener(AuthenticationListener listener) {
        if (authenticationListeners == null) {
            authenticationListeners = new HashSet<>();
        }
        authenticationListeners.add(listener);
    }

    public static void removeAuthenticationListener(AuthenticationListener listener) {
        if (authenticationListeners != null) {
            authenticationListeners.remove(listener);
        }
    }

    private static void fireLoginListeners(final User user) {
        final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
        if (authenticationListeners != null) {
            for (final AuthenticationListener listener : authenticationListeners) {
                final TransactionalThread thread = new TransactionalThread() {
                    @Override
                    public void transactionalRun() {
                        try {
                            VirtualHost.setVirtualHostForThread(virtualHost);
                            listener.afterLogin(user);
                        } finally {
                            VirtualHost.releaseVirtualHostFromThread();
                        }
                    }
                };
                thread.start();
            }
        }
    }
}
