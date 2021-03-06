package com.snowypeaksystems.mobactions.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.bukkit.ChatColor;
import org.junit.jupiter.api.Test;

/**
 * Tests for Message class.
 * @author Copyright (c) Levi Muniz. All Rights Reserved.
 */
class MessageTest {
  @Test
  void replace() {
    IMessage message = new Message("&ctes\\\\t");
    assertEquals(ChatColor.translateAlternateColorCodes('&', "&ctes\\\\t"), message.replace());

    message = new Message("test {} test");
    assertEquals("test test test", message.replace("test"));
    assertThrows(IllegalArgumentException.class, message::replace);
    assertThrows(IllegalArgumentException.class, message::replace);

    message = new Message("\\\\{} \\{\\} {}");
    assertEquals("\\test {} test", message.replace("test", "test", "test"));
    message = new Message("\\{\\} \\\\{} {}");
    assertEquals("{} \\test test", message.replace("test", "test", "test"));
    message = new Message("{} \\{\\} \\\\{}");
    assertEquals("test {} \\test", message.replace("test", "test", "test"));

    message = new Message("&ctest {} test");
    assertEquals(
        ChatColor.translateAlternateColorCodes('&', "&ctest test test"),
        message.replace("test"));

    message = new Message("&ctest \\\\{&d} test");
    assertEquals(
        ChatColor.translateAlternateColorCodes('&', "&ctest \\&r&dtest&r&c test"),
        message.replace("test"));

    message = new Message("&ctest {&d} &n {&e} test");
    assertEquals(
        ChatColor.translateAlternateColorCodes('&', "&ctest &r&dtest&r&c &n &r&etest&r&c&n test"),
        message.replace("test", "test"));
  }
}