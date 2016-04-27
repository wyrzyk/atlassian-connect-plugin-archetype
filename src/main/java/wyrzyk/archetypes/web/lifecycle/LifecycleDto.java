package wyrzyk.archetypes.web.lifecycle;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LifecycleDto {
    Long id;
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
    boolean installed;
    boolean enabled;
}
