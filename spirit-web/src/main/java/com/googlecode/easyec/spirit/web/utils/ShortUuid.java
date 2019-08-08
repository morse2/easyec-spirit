package com.googlecode.easyec.spirit.web.utils;

import java.io.Serializable;

/**
 * A short, unambiguous and URL-safe UUID
 */
public final class ShortUuid implements Serializable {

    private static final long serialVersionUID = -6586706359735993501L;
    private final String uuid;

    ShortUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof ShortUuid))
            return false;

        return o.toString().equals(uuid);
    }
}