package com.snowypeaksystems.mobactions.util;

/**
 * Contains tokens that may be substituted.
 * Example: "This is test number {}".
 *
 * @author Copyright (c) Levi Muniz. All Rights Reserved.
 */
public interface IMessage {
  char TOKEN_PREFIX = '{';
  char TOKEN_SUFFIX = '}';

  /**
   * Gets the message, replacing IMessage.TOKEN with any specified args.
   * @param args List of arguments to replace in the message
   * @return Returns the String form of the message
   * @throws IllegalArgumentException If the number of arguments does not match the number of tokens
   */
  String replace(String... args);
}
