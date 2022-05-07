package health.medunited.tenant;

import java.io.StringReader;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.tenant.ITenantIdentificationStrategy;
import ca.uhn.fhir.util.UrlPathTokenizer;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class JWTTenantIdentificationStrategy implements ITenantIdentificationStrategy {

    private static Logger log = Logger.getLogger(JWTTenantIdentificationStrategy.class.getName());

    @Override
    public void extractTenant(UrlPathTokenizer theUrlPathTokenizer, RequestDetails theRequestDetails) {
        String authorization = theRequestDetails.getHeader("Authorization");
        try {
            if(authorization != null && !"".equals(authorization)) {
                String[] chunks = authorization.substring(7).split("\\.");
                Base64.Decoder decoder = Base64.getUrlDecoder();
                // String header = new String(decoder.decode(chunks[0]));
                String payload = new String(decoder.decode(chunks[1]));
                JsonObject jwt = Json.createReader(new StringReader(payload)).readObject();
                String email = jwt.getString("email");
                String domain = email.split("@")[1];
                theRequestDetails.setTenantId(domain);
            }
        } catch(Exception ex) {
            log.log(Level.WARNING, "Exception during JWT extraction.", ex);
        }
        HapiLocalizer localizer = theRequestDetails.getServer().getFhirContext().getLocalizer();
		throw new InvalidRequestException(Msg.code(307) + localizer.getMessage(RestfulServer.class, "rootRequest.multitenant"));

    }

    @Override
    public String massageServerBaseUrl(String theFhirServerBase, RequestDetails theRequestDetails) {
        
        return null;
    }
    
}
