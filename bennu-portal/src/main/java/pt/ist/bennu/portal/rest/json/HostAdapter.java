package pt.ist.bennu.portal.rest.json;

import pt.ist.bennu.core.annotation.DefaultJsonAdapter;
import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.rest.json.DomainObjectViewer;
import pt.ist.bennu.core.util.MultiLanguageString;
import pt.ist.bennu.json.JsonAdapter;
import pt.ist.bennu.json.JsonBuilder;
import pt.ist.bennu.portal.domain.HostInfo;
import pt.ist.bennu.portal.domain.MenuItem;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(VirtualHost.class)
public class HostAdapter implements JsonAdapter<VirtualHost> {

    @Override
    public VirtualHost create(JsonElement json, JsonBuilder ctx) {
        JsonObject jsonObj = json.getAsJsonObject();
        final String hostname = jsonObj.get("hostname").getAsString();
        VirtualHost virtualHost = Bennu.getInstance().getVirtualHost(hostname);
        if (virtualHost == null) {
            virtualHost = new VirtualHost(hostname);
            HostInfo info = new HostInfo(virtualHost);
            setHostInfo(virtualHost, jsonObj, info, ctx);
        }
        return virtualHost;
    }

    @Override
    public VirtualHost update(JsonElement json, VirtualHost obj, JsonBuilder ctx) {
        JsonObject jsonObj = json.getAsJsonObject();
        HostInfo info = obj.getInfo();
        obj.setHostname(jsonObj.get("hostname").getAsString());
        setHostInfo(obj, jsonObj, info, ctx);
        return obj;
    }

    private void setHostInfo(VirtualHost obj, JsonObject jsonObj, HostInfo info, JsonBuilder ctx) {
        if (jsonObj.has("applicationCopyright")) {
            info.setApplicationCopyright(MultiLanguageString.fromJson(jsonObj.get("applicationCopyright").getAsJsonObject()));
        }
        if (jsonObj.has("applicationTitle")) {
            info.setApplicationTitle(MultiLanguageString.fromJson(jsonObj.get("applicationTitle").getAsJsonObject()));
        }
        if (jsonObj.has("htmlTitle")) {
            info.setHtmlTitle(MultiLanguageString.fromJson(jsonObj.get("htmlTitle").getAsJsonObject()));
        }
        if (jsonObj.has("applicationSubTitle")) {
            info.setApplicationSubTitle(MultiLanguageString.fromJson(jsonObj.get("applicationSubTitle").getAsJsonObject()));
        }
        if (jsonObj.has("supportEmailAddress")) {
            info.setSupportEmailAddress(jsonObj.get("supportEmailAddress").getAsString());
        }
        if (jsonObj.has("systemEmailAddress")) {
            info.setSystemEmailAddress(jsonObj.get("systemEmailAddress").getAsString());
        }
        if (jsonObj.has("theme")) {
            info.setTheme(jsonObj.get("theme").getAsString());
        }
        if (jsonObj.has("menu")) {
            final JsonObject menuObj = jsonObj.get("menu").getAsJsonObject();
            final MenuItem menuItem = ctx.create(menuObj, MenuItem.class);
            obj.setMenu(menuItem);
        }
    }

    @Override
    public JsonElement view(VirtualHost obj, JsonBuilder ctx) {
        JsonObject json = new JsonObject();
        json.addProperty("id", obj.getExternalId());
        json.addProperty("hostname", obj.getHostname());
        json.add("applicationTitle", obj.getInfo().getApplicationTitle().json());
        json.add("htmlTitle", obj.getInfo().getHtmlTitle().json());
        json.add("applicationSubTitle", obj.getInfo().getApplicationSubTitle().json());
        json.add("applicationCopyright", obj.getInfo().getApplicationCopyright().json());
        json.addProperty("supportEmailAddress", obj.getInfo().getSupportEmailAddress());
        json.addProperty("systemEmailAddress", obj.getInfo().getSystemEmailAddress());
        json.addProperty("theme", obj.getInfo().getTheme());
        json.add("menu", ctx.view(obj.getMenu(), DomainObjectViewer.class));
        return json;
    }

}
