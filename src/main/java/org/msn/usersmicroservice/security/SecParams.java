package org.msn.usersmicroservice.security;

import com.auth0.jwt.algorithms.Algorithm;
import java.nio.charset.StandardCharsets;

public interface SecParams {
    public static final long EXP_TIME = 10*24*60*60*1000;
    public static final String SECRET = "my-super-secret-key-2026-secure";
    public static final String PREFIX = "Bearer ";

    // Créer l'algorithme une seule fois pour éviter les problèmes d'encodage
    public static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET.getBytes(StandardCharsets.UTF_8));
}
