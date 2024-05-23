package com.coconut.auth_service.service;

import com.coconut.auth_service.dto.JwtCreateDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

  @Value("${jwt.duration}")
  private String duration;

  @Value("${jwt.secret}")
  private String secretKey;

  private Key getSigningKey() {
    byte[] keyBytes = Base64.getDecoder().decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }


  public boolean validateJWT(String jwt) {
    try {
      log.info("validate started: >>");
      Jws<Claims> parser = getJwtParser(jwt.trim());
      Claims claims = parser.getBody();
      JwsHeader header = parser.getHeader();

      if (claims.getExpiration().before(new Date())) {
        throw new ExpiredJwtException(header, claims, "토큰의 유효시간이 경과하였습니다.");
      }

      return true;
    } catch (Exception e) {
      log.error("error in:> " + e);
      throw e;
    }

  }

  public Jws<Claims> getJwtParser(String jwt) {
    try {

      return Jwts.parserBuilder()
              .setSigningKey(getSigningKey())
              .build()
              .parseClaimsJws(jwt);
    } catch (Exception e) {
      log.error("token error: >> " + e);
      throw new SignatureException("서명에 문제가 있는 토큰입니다.");
    }
  }

  public ResponseCookie generateJwtInCookie(JwtCreateDto dto) {
    String jwtToken = generateJWT(dto);
    log.info("jwtToken: >> " + jwtToken);
    ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .sameSite("None")
            .domain("coconutapp.co.kr")
            .build();

    return cookie;
  }

  public String generateJWT(JwtCreateDto dto) {

    Date expireDate = getExpireDate(duration);
    long userId = dto.getUserId();

    return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .setIssuedAt(new Date())
            .setExpiration(expireDate)
            .signWith(SignatureAlgorithm.HS256, getSigningKey())
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
