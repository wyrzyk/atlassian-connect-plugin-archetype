package wyrzyk.archetypes.web.resources;


import lombok.Value;

@Value
public class LifecycleRequest {
    String key;
    String clientKey;
    String publicKey;
    String sharedSecret;
    String serverVersion;
    String pluginsVersion;
    String baseUrl;
    String productType;
    String description;
    String serviceEntitlementNumber;
    String eventType;
}
