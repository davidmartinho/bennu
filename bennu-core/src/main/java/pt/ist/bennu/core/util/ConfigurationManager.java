package pt.ist.bennu.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.artifact.FenixFrameworkArtifact;
import pt.ist.fenixframework.project.DmlFile;
import pt.ist.fenixframework.project.exception.FenixFrameworkProjectException;

public class ConfigurationManager {
	private static final Properties properties = new Properties();

	private static Map<String, CasConfig> casConfigByHost;

	private static Config config = null;

	private static List<URL> urls = null;

	static {
		try (InputStream inputStream = ConfigurationManager.class.getResourceAsStream("/configuration.properties")) {
			if (inputStream == null) {
				throw new Error("configuration.properties not found in classpath.");
			}
			properties.load(inputStream);

			config = new Config() {
				{
					domainModelPaths = new String[0];
					dbAlias = getProperty("db.alias");
					dbUsername = getProperty("db.user");
					dbPassword = getProperty("db.pass");
					appName = getProperty("app.name");
					updateRepositoryStructureIfNeeded = true;
					rootClass = Bennu.class;
					errorfIfDeletingObjectNotDisconnected = true;
				}

				@Override
				public List<URL> getDomainModelURLs() {
					return getUrls();
				}
			};

			casConfigByHost = new HashMap<>();
			for (final Object key : properties.keySet()) {
				final String property = (String) key;
				int i = property.indexOf(".cas.enable");
				if (i >= 0) {
					final String hostname = property.substring(0, i);
					if (getBooleanProperty(property)) {
						final String casLoginUrl = getProperty(hostname + ".cas.loginUrl");
						final String casLogoutUrl = getProperty(hostname + ".cas.logoutUrl");
						final String casValidateUrl = getProperty(hostname + ".cas.ValidateUrl");
						final String serviceUrl = getProperty(hostname + ".cas.serviceUrl");

						final CasConfig casConfig = new CasConfig(casLoginUrl, casLogoutUrl, casValidateUrl, serviceUrl);
						casConfigByHost.put(hostname, casConfig);
					} else {
						final CasConfig casConfig = new CasConfig();
						casConfigByHost.put(hostname, casConfig);
					}
				}
			}
		} catch (IOException e) {
			throw new Error("configuration.properties could not be read.", e);
		}
	}

	public synchronized static List<URL> getUrls() {
		if (urls == null) {
			urls = new ArrayList<>();
			try {
				for (DmlFile dml : FenixFrameworkArtifact.fromName(getProperty("app.name")).getFullDmlSortedList()) {
					urls.add(dml.getUrl());
				}
			} catch (FenixFrameworkProjectException | IOException e) {
				throw new Error(e);
			}
		}
		return urls;
	}

	public static String getProperty(final String key) {
		return properties.getProperty(key);
	}

	public static boolean getBooleanProperty(final String key) {
		return Boolean.parseBoolean(properties.getProperty(key));
	}

	public static Integer getIntegerProperty(final String key) {
		return Integer.valueOf(properties.getProperty(key));
	}

	public static void setProperty(final String key, final String value) {
		properties.setProperty(key, value);
	}

	public static class CasConfig {

		protected boolean casEnabled = false;
		protected String casLoginUrl = null;
		protected String casLogoutUrl = null;
		protected String casValidateUrl = null;
		protected String serviceUrl = null;

		public CasConfig() {
		}

		public CasConfig(final String casLoginUrl, final String casLogoutUrl, final String casValidateUrl, final String serviceUrl) {
			this.casEnabled = true;
			this.casLoginUrl = casLoginUrl;
			this.casLogoutUrl = casLogoutUrl;
			this.casValidateUrl = casValidateUrl;
			this.serviceUrl = serviceUrl;
		}

		public String getServiceUrl() {
			return serviceUrl;
		}

		public boolean isCasEnabled() {
			return casEnabled;
		}

		public String getCasLoginUrl() {
			return casLoginUrl;
		}

		public String getCasLogoutUrl() {
			return casLogoutUrl;
		}

		public String getCasValidateUrl() {
			return casValidateUrl;
		}

	}

	public static CasConfig getCasConfig(String hostname) {
		for (final Entry<String, CasConfig> entry : casConfigByHost.entrySet()) {
			if (entry.getKey().startsWith(hostname)) {
				return entry.getValue();
			}
		}
		return null;
	}

	public static Config getFenixFrameworkConfig() {
		return config;
	}

	public static boolean isFilterRequestWithDigest() {
		return getBooleanProperty("filter.request.with.digest");
	}

	public static String getTamperingRedirect() {
		return getProperty("digest.tampering.url");
	}

	public static String getAppContext() {
		return getProperty("app.context");
	}

	public static Locale getDefaultLocale() {
		return new Locale(getProperty("language"), getProperty("location"), getProperty("variant"));
	}
}
