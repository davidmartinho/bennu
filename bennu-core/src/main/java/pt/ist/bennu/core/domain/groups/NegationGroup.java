package pt.ist.bennu.core.domain.groups;

import java.util.HashSet;
import java.util.Set;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.service.Service;

public class NegationGroup extends NegationGroup_Base {
	protected NegationGroup(PersistentGroup persistentGroup) {
		super();
		setPersistentGroup(persistentGroup);
	}

	@Override
	public String getName() {
		return "NOT " + getPersistentGroup().getName();
	}

	@Override
	public Set<User> getMembers() {
		Set<User> users = new HashSet<>(Bennu.getInstance().getUsersSet());
		users.removeAll(getPersistentGroup().getMembers());
		return users;
	}

	@Override
	public boolean isMember(User user) {
		return !getPersistentGroup().isMember(user);
	}

	@Service
	public static NegationGroup getInstance(final PersistentGroup persistentGroup) {
		for (PersistentGroup group : Bennu.getInstance().getGroupsSet()) {
			if (group instanceof NegationGroup) {
				NegationGroup negationGroup = (NegationGroup) group;
				if (negationGroup.getPersistentGroup().equals(persistentGroup)) {
					return negationGroup;
				}
			}
		}
		return new NegationGroup(persistentGroup);
	}
}
