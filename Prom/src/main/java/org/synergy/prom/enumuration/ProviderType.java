package org.synergy.prom.enumuration;

public enum ProviderType {
    KAKAO("KAKAO"),
    GOOGLE("GOOGLE"),
    INSERVICE("INSERVICE");

    private String providerType;

    ProviderType(String providerType) {
        this.providerType = providerType;
    }

    public String getProviderType() {
        return this.providerType;
    }
}
