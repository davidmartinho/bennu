package pt.ist.bennu.portal.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import pt.ist.bennu.bennu.core.rest.BennuRestResource;
import pt.ist.bennu.bennu.core.rest.serializer.JsonAwareResource;
import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.portal.domain.MenuItem;

@Path("menu")
public class MenuResource extends BennuRestResource {

    static {
        JsonAwareResource.setDefault(MenuItem.class, MenuItemAdapter.class);
        JsonAwareResource.setDefault(VirtualHost.class, VirtualHostAdapter.class);
    }

    @Path("{hostname: [a-zA-Z0-9\\.]+}")
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String menu(@PathParam("hostname") final String hostname) {
        final VirtualHost virtualHost = Bennu.getInstance().getVirtualHost(hostname);
        if (virtualHost != null) {
            return view(virtualHost);
        } else {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
    }
}
