package com.groupe3.pharmaconnect.enums;

public enum AppUserRole {
    ADMIN,
    PHARMACIST,
    CLIENT;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}