package wyrzyk.archetypes.resources;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LifecycleRequest {
    private String key;
    private String clientKey;
    private String publicKey;
    private String sharedSecret;
    private String serverVersion;
    private String pluginsVersion;
    private String baseUrl;
    private String productType;
    private String description;
    private String serviceEntitlementNumber;
    private String eventType;
}
