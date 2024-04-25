package com.jdh.community_spring.common.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

@Component
public class SimpleEncrypt {

  public boolean match(String rawString, String hashedString) {
    String hashedPassword = encrypt(rawString);
    return hashedString.equals(hashedPassword);
  }

  public String encrypt(String rawString) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] hashedPassword = md.digest(rawString.getBytes());

      return HexFormat.of().formatHex(hashedPassword);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("암호화 알고리즘이 존재하지 않음", e);
    }
  }

  private byte[] createSalt(int length) {
    byte[] salt = new byte[length];
    SecureRandom random = new SecureRandom();
    random.nextBytes(salt);

    return salt;
  }
}
