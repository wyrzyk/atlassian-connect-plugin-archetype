package wyrzyk.archetypes.web.resources.lifecycle;


import lombok.Value;

@Value
class LifecycleRequest {
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
