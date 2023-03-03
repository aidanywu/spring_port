package com.nighthawk.spring_portfolio.mvc.blacklist;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blacklist")
public class BlacklistedJwtApiController {

    @Autowired
    private BlacklistedJwtJpaRepository repository;

    @GetMapping("/")
    public ResponseEntity<List<BlacklistedJwt>> getAllBlacklistedJwt() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> postPerson(@CookieValue("jwt") String str) {
        BlacklistedJwt jwt = new BlacklistedJwt(str);
        repository.save(jwt);
		final ResponseCookie tokenCookie = ResponseCookie.from("jwt", "")
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(0)
			.sameSite("None")
			// .domain("example.com") // Set to backend
			.build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, tokenCookie.toString()).build();
    }

    
}