package wyrzyk.archetypes.api;

public interface ClientInfoDto {
    Long getId();

    String getClientKey();

    String getBaseUrl();

    String getProductType();

    String getSharedSecret();

    boolean isAcive();
}
