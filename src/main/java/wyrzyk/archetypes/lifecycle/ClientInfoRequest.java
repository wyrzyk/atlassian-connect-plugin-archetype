package wyrzyk.archetypes.lifecycle;


import lombok.Value;

@Value
class ClientInfoRequest {
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

    ClientInfoDtoImpl toDto() {
        return ClientInfoDtoImpl.builder()
                .baseUrl(this.getBaseUrl())
                .clientKey(this.getClientKey())
                .description(this.getDescription())
                .key(this.getKey())
                .pluginsVersion(this.getPluginsVersion())
                .productType(this.getProductType())
                .publicKey(this.getPublicKey())
                .serverVersion(this.getServerVersion())
                .serviceEntitlementNumber(this.getServiceEntitlementNumber())
                .sharedSecret(this.getSharedSecret())
                .build();
    }
}
