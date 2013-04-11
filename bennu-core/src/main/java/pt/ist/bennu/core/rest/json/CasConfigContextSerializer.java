package pt.ist.bennu.core.rest.json;

import pt.ist.bennu.core.annotation.DefaultJsonAdapter;
import pt.ist.bennu.core.rest.BennuRestResource.CasConfigContext;
import pt.ist.bennu.core.util.ConfigurationManager.CasConfig;
import pt.ist.bennu.json.JsonBuilder;
import pt.ist.bennu.json.JsonViewer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(CasConfigContext.class)
public class CasConfigContextSerializer implements JsonViewer<CasConfigContext> {

    @Override
    public JsonElement view(CasConfigContext casConfigContext, JsonBuilder context) {
        JsonObject jsonObject = new JsonObject();
        CasConfig casConfig = casConfigContext.getCasConfig();
        if (casConfig.isCasEnabled()) {
            jsonObject.addProperty("casEnabled", true);
            jsonObject.addProperty("loginUrl", casConfig.getCasLoginUrl(casConfigContext.getRequest()));
            jsonObject.addProperty("logoutUrl", casConfig.getCasLogoutUrl());
        } else {
            jsonObject.addProperty("casEnabled", false);
        }
        return jsonObject;
    }

}
