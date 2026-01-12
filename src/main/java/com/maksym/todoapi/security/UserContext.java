package com.maksym.todoapi.security;

import java.util.UUID;

public final class UserContext {
    private static final ThreadLocal<UUID> CURRENT_USER = new ThreadLocal<>();

    private UserContext(){}

    public static void set(UUID userId) { CURRENT_USER.set(userId); }
    public static UUID get() { return CURRENT_USER.get(); }
    public static void clear() { CURRENT_USER.remove(); }
}
