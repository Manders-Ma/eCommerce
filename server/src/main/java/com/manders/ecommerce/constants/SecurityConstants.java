package com.manders.ecommerce.constants;

public class SecurityConstants {
  
  public static final String JWT_KEY = "RcdQDUlOBdDyoDzBk3aTRF6wk3j03SK5";
  public static final String JWT_HEADER = "Authorization";
  // 單位: 毫秒
  public static final Long JWT_EXPIRATION_TIME = 8 * 3600 * 1000L;
  public static final String[] JWT_AUTHENTICATED_URL = new String[] {
      "/checkout/purchase", 
      "/inventory", 
      "/order-history", 
      "/pay"
      };
}
