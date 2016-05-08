package wyrzyk.archetypes.api;

public interface ClientInfoDto {
    String getClientKey();

    String getBaseUrl();

    String getProductType();

    String getSharedSecret();

    boolean isAcive();
}
