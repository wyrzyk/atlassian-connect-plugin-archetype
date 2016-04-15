package wyrzyk.archetypes.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("lifecycle")
public class LifecycleResource {

    @POST
    @Path("installed")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response installed(LifecycleRequest lifecycleRequest) {
        return Response.ok().build();
    }
}
