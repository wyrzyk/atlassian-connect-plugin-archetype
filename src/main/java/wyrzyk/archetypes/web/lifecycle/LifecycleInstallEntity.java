package wyrzyk.archetypes.web.lifecycle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
class LifecycleInstallEntity {
    @Id
    @GeneratedValue
    private Long id;
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

    static LifecycleInstallEntity fromDto(LifecycleDto lifecycleDto){
        return LifecycleInstallEntity.builder()
                .id(lifecycleDto.getId())
                .baseUrl(lifecycleDto.getBaseUrl())
                .clientKey(lifecycleDto.getClientKey())
                .description(lifecycleDto.getDescription())
                .eventType(lifecycleDto.getEventType())
                .key(lifecycleDto.getKey())
                .pluginsVersion(lifecycleDto.getPluginsVersion())
                .productType(lifecycleDto.getProductType())
                .publicKey(lifecycleDto.getPublicKey())
                .serverVersion(lifecycleDto.getServerVersion())
                .serviceEntitlementNumber(lifecycleDto.getServiceEntitlementNumber())
                .sharedSecret(lifecycleDto.getSharedSecret())
                .build();
    }

    LifecycleDto toDto() {
        return LifecycleDto.builder()
                .id(this.getId())
                .baseUrl(this.getBaseUrl())
                .clientKey(this.getClientKey())
                .description(this.getDescription())
                .eventType(this.getEventType())
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

