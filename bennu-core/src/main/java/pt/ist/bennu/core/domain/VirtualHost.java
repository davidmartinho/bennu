package pt.ist.bennu.core.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import pt.ist.bennu.core.util.ConfigurationManager;
import pt.ist.bennu.core.util.ConfigurationManager.CasConfig;
import pt.ist.bennu.service.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class VirtualHost extends VirtualHost_Base {

	private static final ThreadLocal<VirtualHost> current = new ThreadLocal<>();

	public static VirtualHost getVirtualHostForThread() {
		return current.get();
	}

	public static void setVirtualHostForThread(final VirtualHost virtualHost) {
		current.set(virtualHost);
	}

	public static void releaseVirtualHostFromThread() {
		current.remove();
	}

	public static VirtualHost setVirtualHostForThread(final String serverName) {
		final Bennu bennu = Bennu.getInstance();
		final Set<VirtualHost> virtualHosts = bennu.getVirtualHostsSet();
		for (final VirtualHost virtualHost : virtualHosts) {
			if (virtualHost.getHostname().startsWith(serverName)) {
				setVirtualHostForThread(virtualHost);
				return virtualHost;
			}
		}
		final VirtualHost virtualHost = virtualHosts.iterator().next();
		setVirtualHostForThread(virtualHost);
		return virtualHost;
	}

	public VirtualHost() {
		setBennu(Bennu.getInstance());
		setHostname("localhost");
		setLanguages(Collections.singleton(Language.en));
	}

	@Service
	public void delete() {
		if (Bennu.getInstance().getVirtualHostsSet().size() > 1) {
			removeBennu();
			deleteDomainObject();
		}
	}

	@Override
	public void setHostname(final String hostname) {
		super.setHostname(hostname.toLowerCase());
	}

	public boolean isCasEnabled() {
		CasConfig casConfig = ConfigurationManager.getCasConfig(getHostname());
		return casConfig != null && casConfig.isCasEnabled();
	}

	public Set<Language> getSupportedLanguagesSet() {
		Set<Language> languages = new HashSet<>();
		if (StringUtils.isNotBlank(getSupportedLanguages())) {
			for (String code : getSupportedLanguages().split(":")) {
				languages.add(Language.valueOf(code));
			}
		}
		return languages;
	}

	public boolean supports(final Language language) {
		final String supportedLanguages = getSupportedLanguages();
		return supportedLanguages != null && language != null && supportedLanguages.indexOf(language.name()) >= 0;
	}

	@Service
	public void setLanguages(final Set<Language> languages) {
		setSupportedLanguages(StringUtils.join(languages, ":"));
	}
}