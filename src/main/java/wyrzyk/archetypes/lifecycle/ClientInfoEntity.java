package wyrzyk.archetypes.lifecycle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wyrzyk.archetypes.api.ClientInfoDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
class ClientInfoEntity {
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
    boolean installed;
    boolean enabled;

    static ClientInfoEntity fromDto(ClientInfoDtoImpl lifecycleDto) {
        return ClientInfoEntity.builder()
                .id(lifecycleDto.getId())
                .baseUrl(lifecycleDto.getBaseUrl())
                .clientKey(lifecycleDto.getClientKey())
                .description(lifecycleDto.getDescription())
                .key(lifecycleDto.getKey())
                .pluginsVersion(lifecycleDto.getPluginsVersion())
                .productType(lifecycleDto.getProductType())
                .publicKey(lifecycleDto.getPublicKey())
                .serverVersion(lifecycleDto.getServerVersion())
                .serviceEntitlementNumber(lifecycleDto.getServiceEntitlementNumber())
                .sharedSecret(lifecycleDto.getSharedSecret())
                .enabled(lifecycleDto.isEnabled())
                .installed(lifecycleDto.isInstalled())
                .build();
    }

    ClientInfoDto toDto() {
        return ClientInfoDtoImpl.builder()
                .id(this.getId())
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
                .enabled(this.isEnabled())
                .installed(this.isInstalled())
                .build();
    }
}

