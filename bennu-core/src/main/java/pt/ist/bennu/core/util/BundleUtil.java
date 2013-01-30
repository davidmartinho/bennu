/*
 * BundleUtil.java
 *
 * Copyright (c) 2013, Instituto Superior Técnico. All rights reserved.
 *
 * This file is part of bennu-core.
 *
 * bennu-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * bennu-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with bennu-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ist.bennu.core.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.bennu.core.domain.VirtualHost;

public class BundleUtil {
	private static final Logger logger = LoggerFactory.getLogger(BundleUtil.class);

	public static String getString(final String bundle, final String key, String... args) {
		return getString(bundle, Language.getLocale(), key, args);
	}

	public static String getString(final String bundle, Locale locale, final String key, String... args) {
		try {
			String message = ResourceBundle.getBundle(bundle, locale).getString(key);
			for (int i = 0; i < args.length; i++) {
				message = message.replaceAll("\\{" + i + "\\}", args[i] == null ? "" : args[i]);
			}
			return message;
		} catch (MissingResourceException e) {
			logger.warn(e.getMessage());
			return '!' + key + '!';
		}
	}

	public static MultiLanguageString getMultilanguageString(final String bundle, final String key, String... args) {
		MultiLanguageString mls = new MultiLanguageString();
		for (Language language : VirtualHost.getVirtualHostForThread().getSupportedLanguagesSet()) {
			String message = getString(bundle, new Locale(language.name()), key, args);
			mls = mls.with(language, message);
		}
		return mls;
	}
}
