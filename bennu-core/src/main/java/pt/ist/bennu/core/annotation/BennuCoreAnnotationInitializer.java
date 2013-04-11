/*
 * BennuCoreAnnotationInitializer.java
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
package pt.ist.bennu.core.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Manifest;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import pt.ist.bennu.core.domain.groups.CustomGroup;
import pt.ist.bennu.core.rest.json.JsonAwareResource;
import pt.ist.bennu.core.servlets.BennuJerseyRestApplication;

@HandlesTypes({ CustomGroupOperator.class, Path.class, Provider.class, DefaultJsonAdapter.class })
public class BennuCoreAnnotationInitializer implements ServletContainerInitializer {
    @Override
    @SuppressWarnings("unchecked")
    public void onStartup(Set<Class<?>> classes, ServletContext ctx) throws ServletException {

        if (classes != null) {
            for (Class<?> type : classes) {
                CustomGroupOperator operator = type.getAnnotation(CustomGroupOperator.class);
                if (operator != null) {
                    CustomGroup.registerOperator((Class<? extends CustomGroup>) type);
                }
                Path restEndpoint = type.getAnnotation(Path.class);
                if (restEndpoint != null) {
                    BennuJerseyRestApplication.registerEndpoint(getModuleName(type), restEndpoint.value(), type);
                }
                Provider restProvider = type.getAnnotation(Provider.class);
                if (restProvider != null) {
                    BennuJerseyRestApplication.register(type);
                }
                DefaultJsonAdapter defaultJsonAdapter = type.getAnnotation(DefaultJsonAdapter.class);
                if (defaultJsonAdapter != null) {
                    JsonAwareResource.setDefault(defaultJsonAdapter.value(), type);
                }
            }
        }
    }

    public String getModuleName(Class<?> type) {
        URL typeLocation = type.getProtectionDomain().getCodeSource().getLocation();

        if (typeLocation.toExternalForm().endsWith("/")) {
            Properties props = new Properties();
            InputStream propStream = null;
            try {
                propStream = new URL(typeLocation + "configuration.properties").openStream();
                props.load(propStream);
                return (String) props.get("app.name");
            } catch (IOException e) {
                throw new Error(e);
            } finally {
                try {
                    if (propStream != null) {
                        propStream.close();
                    }
                } catch (IOException e) {
                    throw new Error(e);
                }
            }
        }
        InputStream mfStream = null;
        try {
            mfStream = new URL("jar:".concat(typeLocation.toExternalForm()).concat("!/META-INF/MANIFEST.MF")).openStream();
            Manifest mf = new Manifest(mfStream);
            return mf.getMainAttributes().getValue("artifactId");
        } catch (IOException e) {
            throw new Error(e);
        } finally {
            try {
                if (mfStream != null) {
                    mfStream.close();
                }
            } catch (IOException e) {
                throw new Error(e);
            }
        }
    }

}
