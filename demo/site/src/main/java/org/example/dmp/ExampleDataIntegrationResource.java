package org.example.dmp;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bloomreach.relevance.dmp.targeting.DataIntegrationTargetingData;
import com.bloomreach.relevance.dmp.util.Util;
import com.fasterxml.jackson.databind.JsonNode;
import com.onehippo.cms7.targeting.Visitor;
import com.onehippo.cms7.targeting.VisitorInfo;
import com.onehippo.cms7.targeting.VisitorService;
import com.onehippo.cms7.targeting.data.TargetingData;

import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.essentials.components.rest.BaseRestResource;
import org.onehippo.forge.selection.hst.contentbean.ValueList;
import org.onehippo.forge.selection.hst.manager.ValueListManager;
import org.onehippo.forge.selection.hst.util.SelectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @version "\$Id$" ksalic
 */
@Path("/dmp")
@Produces(APPLICATION_JSON)
public class ExampleDataIntegrationResource extends BaseRestResource {

    private static final Logger log = LoggerFactory.getLogger(ExampleDataIntegrationResource.class);
    private static final String DMP_PREFIX = "dmp_";

    private VisitorService visitorService;

    public void setVisitorService(final VisitorService visitorService) {
        this.visitorService = visitorService;
    }


    /***
     * Get detail information of a visitor (Secured)
     *
     * @param visitorId
     * @return
     */
    @GET
    @Path("/{visitorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Visitor getVisitorDetails(@PathParam("visitorId") final String visitorId) {
        final VisitorInfo visitorInfo = visitorService.getVisitorInfo(visitorId);
        if (visitorInfo != null) {
            return visitorInfo.getVisitor();
        }
        return null;
    }


    /***
     * End point for the cookie sync (unsecured)
     * May need to implement an interface for security e.g. tokenservice
     *
     * @param request
     * @param response
     * @param partner or dmp
     * @param sid parameter name of the sync id
     * @param uid parameter name of the BRE visitor id
     * @param redirect
     * @return
     */
    @GET
    @Path("/sync/{partner}")
    public Response cookiesync(@Context HttpServletRequest request, @Context HttpServletResponse response,
                               @PathParam("partner") final String partner,
                               @QueryParam("q_sid") final String sid,
                               @QueryParam("q_uid") final String uid,
                               @QueryParam("rd") final String redirect) {

        try {


            boolean isAllowed = isDomainWhiteListed(request, redirect);
            if (isAllowed) {
                final Map<String, String> queryMap = Util.getQueryMap(redirect);

                String sync_id = queryMap.get(sid);

                final Visitor visitor = visitorService.getVisitor(request, response);
                final String visitorId = visitor.getId();

                String newDirect = redirect.replace(uid, visitorId);

                Map<String, TargetingData> targetingDataMap = visitor.getTargetingData();
                if (targetingDataMap == null) {
                    targetingDataMap = new HashMap<>();
                }
                DataIntegrationTargetingData integrationTargetingData = null;
                if (targetingDataMap.containsKey(DMP_PREFIX + partner)) {
                    integrationTargetingData = (DataIntegrationTargetingData) targetingDataMap.get(DMP_PREFIX + partner);
                } else {
                    integrationTargetingData = new DataIntegrationTargetingData(DMP_PREFIX + partner);
                }

                integrationTargetingData.setId(sync_id);

                targetingDataMap.put(DMP_PREFIX + partner, integrationTargetingData);
                visitor.setTargetingData(targetingDataMap);
                visitor.save();
                return Response.status(Response.Status.FOUND).location(new URI(newDirect)).build();
            }
        } catch (URISyntaxException e) {
            log.error("error while trying to redirect partner URL");
        } catch (RepositoryException e) {
            log.error("error while trying retrieving scope");
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    private boolean isDomainWhiteListed(final HttpServletRequest request, final String redirect) throws RepositoryException {
        final ValueListManager valueListManager = HstServices.getComponentManager().getComponent("dmp.valuelistmanager", "com.onehippo.cms7.targeting.restapi");

        final HstRequestContext requestContext = getRequestContext(request);
        final ValueList dmpWhitelist = valueListManager.getValueList(requestContext.getSiteContentBaseBean(), "dmp_whitelist");
        final Map<String, String> dmpWhiteListMap = SelectionUtil.valueListAsMap(dmpWhitelist);

        try {
            final String domainName = Util.getDomainName(redirect);
            return dmpWhiteListMap.containsKey(domainName);
        } catch (URISyntaxException e) {
            log.error("...");
        }

        return false;
    }


    /***
     * Edit information of a certain visitor of a certain partner
     * Secured
     *
     * @param visitorId
     * @param partner
     * @param data
     * @return
     */
    @POST
    @Path("/{visitorId}/{partner}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public VisitorInfo saveVisitorDetails(@PathParam("visitorId") final String visitorId, @PathParam("partner") final String partner, final JsonNode data) {
        final VisitorInfo visitorInfo = visitorService.getVisitorInfo(visitorId);
        final Visitor visitor = visitorInfo.getVisitor();

        if (visitor != null & visitor.getTargetingData() != null && visitor.getTargetingData().containsKey(DMP_PREFIX + partner)) {
            DataIntegrationTargetingData targetingData = (DataIntegrationTargetingData) visitor.getTargetingData().get(DMP_PREFIX + partner);

            if (targetingData == null) {
                targetingData = new DataIntegrationTargetingData(DMP_PREFIX + partner);
            }
            targetingData.setData(data);

            visitor.getTargetingData().put(DMP_PREFIX + partner, targetingData);
            visitor.save();
        }

        return visitorInfo;
    }

    /***
     * Delete information of a certain visitor of a certain partner
     * Secured
     *
     * @param visitorId
     * @param partner
     * @return
     */
    @DELETE
    @Path("/{visitorId}/{partner}")
    @Produces(MediaType.APPLICATION_JSON)
    public VisitorInfo deleteVisitorDetails(@PathParam("visitorId") final String visitorId, @PathParam("partner") final String partner) {
        final VisitorInfo visitorInfo = visitorService.getVisitorInfo(visitorId);
        final Visitor visitor = visitorInfo.getVisitor();

        if (visitor != null & visitor.getTargetingData() != null && visitor.getTargetingData().containsKey(DMP_PREFIX + partner)) {
            visitor.getTargetingData().remove(DMP_PREFIX + partner);
            visitor.save();
        }

        return visitorInfo;
    }


}
