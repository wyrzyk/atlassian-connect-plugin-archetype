package wyrzyk.archetypes.resources;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.xml.bind.annotation.XmlRootElement;

// todo: use lombok
@XmlRootElement
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

    public LifecycleRequest() {
    }

    public LifecycleRequest(String key, String clientKey, String publicKey, String sharedSecret, String serverVersion,
                            String pluginsVersion, String baseUrl, String productType, String description,
                            String serviceEntitlementNumber, String eventType) {
        this.key = key;
        this.clientKey = clientKey;
        this.publicKey = publicKey;
        this.sharedSecret = sharedSecret;
        this.serverVersion = serverVersion;
        this.pluginsVersion = pluginsVersion;
        this.baseUrl = baseUrl;
        this.productType = productType;
        this.description = description;
        this.serviceEntitlementNumber = serviceEntitlementNumber;
        this.eventType = eventType;
    }

    public String getKey() {
        return key;
    }

    public String getClientKey() {
        return clientKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getSharedSecret() {
        return sharedSecret;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public String getPluginsVersion() {
        return pluginsVersion;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getProductType() {
        return productType;
    }

    public String getDescription() {
        return description;
    }

    public String getEventType() {
        return eventType;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setSharedSecret(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public void setPluginsVersion(String pluginsVersion) {
        this.pluginsVersion = pluginsVersion;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getServiceEntitlementNumber() {
        return serviceEntitlementNumber;
    }

    public void setServiceEntitlementNumber(String serviceEntitlementNumber) {
        this.serviceEntitlementNumber = serviceEntitlementNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final LifecycleRequest that = (LifecycleRequest) o;

        return new EqualsBuilder()
                .append(key, that.key)
                .append(clientKey, that.clientKey)
                .append(publicKey, that.publicKey)
                .append(sharedSecret, that.sharedSecret)
                .append(serverVersion, that.serverVersion)
                .append(pluginsVersion, that.pluginsVersion)
                .append(baseUrl, that.baseUrl)
                .append(productType, that.productType)
                .append(description, that.description)
                .append(serviceEntitlementNumber, that.serviceEntitlementNumber)
                .append(eventType, that.eventType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(key)
                .append(clientKey)
                .append(publicKey)
                .append(sharedSecret)
                .append(serverVersion)
                .append(pluginsVersion)
                .append(baseUrl)
                .append(productType)
                .append(description)
                .append(serviceEntitlementNumber)
                .append(eventType)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "LifecycleRequest{" +
                "key='" + key + '\'' +
                ", clientKey='" + clientKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", sharedSecret='" + sharedSecret + '\'' +
                ", serverVersion='" + serverVersion + '\'' +
                ", pluginsVersion='" + pluginsVersion + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                ", productType='" + productType + '\'' +
                ", description='" + description + '\'' +
                ", serviceEntitlementNumber='" + serviceEntitlementNumber + '\'' +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
