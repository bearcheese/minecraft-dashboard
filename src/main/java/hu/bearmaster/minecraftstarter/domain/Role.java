package hu.bearmaster.minecraftstarter.domain;

public enum Role {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String role;

    Role(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}
