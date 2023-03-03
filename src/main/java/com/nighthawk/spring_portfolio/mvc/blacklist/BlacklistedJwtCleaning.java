package com.nighthawk.spring_portfolio.mvc.blacklist;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class BlacklistedJwtCleaning {

    @Autowired
    private BlacklistedJwtJpaRepository repository;

    // Run the method every 3600000 milliseconds, or 1 hour
    @Scheduled(fixedRate = 3600000)
    private void cleanBlacklist() {
        for (BlacklistedJwt jwt : repository.findAll()) {
            String token = jwt.getBlacklistedJwt();
            try {
                DecodedJWT decoded = JWT.decode(token);
                if (decoded.getExpiresAt().before(new Date())) {
                    repository.deleteById(jwt.getId());
                }
            } catch(JWTDecodeException e) {
    			System.out.println("Error" + e);
            }
        }
    }
}