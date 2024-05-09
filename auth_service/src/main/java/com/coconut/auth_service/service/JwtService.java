package com.coconut.auth_service.service;

import com.coconut.auth_service.dto.JwtCreateDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

@Service
public class JwtService {

  @Value("${jwt.duration}")
  private String duration;

  @Value("${jwt.secret}")
  private String secretKey;

  public Cookie generateJwtInCookie(JwtCreateDto dto) {
    String jwtToken = generateJWT(dto);

    Cookie jwtCookie = new Cookie("jwt", jwtToken);
    jwtCookie.setHttpOnly(true);
    jwtCookie.setSecure(true);
    jwtCookie.setPath("/");
    jwtCookie.setMaxAge(7 * 24 * 60 * 60);

    return jwtCookie;
  }

  public String generateJWT(JwtCreateDto dto) {

    Date expireDate = getExpireDate(duration);
    long userId = dto.getUserId();

    return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .setIssuedAt(new Date())
            .setExpiration(expireDate)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
  }

  private Date getExpireDate(String expression) {
    long duration = parseDuration(expression);

    Date now = new Date();
    Date expireDate = new Date(now.getTime() + duration);

    return expireDate;
  }

  private long parseDuration(String expression) {
    String[] trimmed = expression
            .replaceAll("\\s*\\*\\s*", " * ")
            .split(" \\* ");

    long sum = Arrays.stream(trimmed)
            .mapToLong(Long::parseLong) // 문자열을 long 타입으로 변환
            .reduce(1, (acc, cur) -> acc * cur);

    return sum;
  }
}
