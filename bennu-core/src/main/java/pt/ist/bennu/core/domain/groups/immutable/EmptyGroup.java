package pt.ist.bennu.core.domain.groups.immutable;

import java.util.Collections;
import java.util.Set;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.service.Service;

public class EmptyGroup extends EmptyGroup_Base {
	protected EmptyGroup() {
		super();
	}

	@Override
	public Set<User> getMembers() {
		return Collections.emptySet();
	}

	@Override
	public boolean isMember(final User user) {
		return false;
	}

	@Service
	public static EmptyGroup getInstance() {
		final EmptyGroup group = getSystemGroup(EmptyGroup.class);
		return group == null ? new EmptyGroup() : group;
	}
}
