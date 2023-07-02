package com.al.qdt.common.infrastructure.converters;

import com.al.qdt.common.domain.enums.Player;
import org.springframework.core.convert.converter.Converter;

/**
 * Player enum converter.
 */
public class StringToPlayerConverter implements Converter<String, Player> {

    @Override
    public Player convert(String source) {
        try {
            return Player.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
