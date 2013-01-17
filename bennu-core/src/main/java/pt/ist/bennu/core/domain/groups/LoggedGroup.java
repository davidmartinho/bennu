package pt.ist.bennu.core.domain.groups;

import java.util.Collections;
import java.util.Set;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.service.Service;

public class LoggedGroup extends LoggedGroup_Base {
	protected LoggedGroup() {
		super();
	}

	@Override
	public String expression() {
		return "logged";
	}

	@Override
	public Set<User> getMembers() {
		return Collections.unmodifiableSet(Bennu.getInstance().getUsersSet());
	}

	@Override
	public boolean isMember(final User user) {
		return user != null;
	}

	@Override
	public Set<User> getMembers(DateTime when) {
		return getMembers();
	}

	@Override
	public boolean isMember(User user, DateTime when) {
		return isMember(user);
	}

	@Service
	public static LoggedGroup getInstance() {
		LoggedGroup group = select(LoggedGroup.class);
		return group == null ? new LoggedGroup() : group;
	}
}
