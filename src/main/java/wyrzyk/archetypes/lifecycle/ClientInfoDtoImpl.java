package wyrzyk.archetypes.lifecycle;

import lombok.Builder;
import lombok.Value;
import wyrzyk.archetypes.api.ClientInfoDto;

@Value
@Builder
class ClientInfoDtoImpl implements ClientInfoDto {
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
    boolean installed;
    boolean enabled;

    @Override
    public boolean isAcive() {
        return installed && enabled;
    }
}
