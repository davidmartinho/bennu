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
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.groups.DynamicGroup;
import pt.ist.bennu.core.domain.groups.UserGroup;
import pt.ist.bennu.core.util.ConfigurationManager;
import pt.ist.bennu.core.util.TransactionalThread;
import pt.ist.bennu.service.Service;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * @author Pedro Santos
 */
public class Authenticate {
	private static final Logger logger = LoggerFactory.getLogger(Authenticate.class);

	private static Set<AuthenticationListener> authenticationListeners;

	@Service
	public static User login(HttpSession session, String username, String password, boolean checkPassword) {
		User user = User.findByUsername(username);
		if (checkPassword && ConfigurationManager.getBooleanProperty("check.login.password", true)) {
			if (user == null || user.getPassword() == null || !user.matchesPassword(password)) {
				throw new DomainException("resources.BennuResources", "error.bennu.core.authentication.failed");
			}
		}
		if (user == null) {
			user = new User(username);
		}

		SessionUserWrapper userWrapper = new SessionUserWrapper(user);
		session.setAttribute(SetUserViewFilter.USER_SESSION_ATTRIBUTE, user);
		UserView.setSessionUserWrapper(userWrapper);
		if (Bennu.getInstance().getUsersCount() == 1) {
			logger.info("Bootstrapped #managers group to user: " + user.getUsername());
			new DynamicGroup("managers", UserGroup.getInstance(user));
		}

		fireLoginListeners(user);
		logger.info("Logged in user: " + user.getUsername());
		return user;
	}

	@Service
	public static void logout(HttpSession session) {
		final SessionUserWrapper wrapper = (SessionUserWrapper) session.getAttribute(SetUserViewFilter.USER_SESSION_ATTRIBUTE);
		if (wrapper != null) {
			wrapper.getUser().setLastLogoutDateTime(new DateTime());
		}
		UserView.setSessionUserWrapper(null);
		session.invalidate();
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
