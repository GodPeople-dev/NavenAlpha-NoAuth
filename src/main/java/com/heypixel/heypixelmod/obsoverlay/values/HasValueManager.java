package com.heypixel.heypixelmod.obsoverlay.values;

import com.heypixel.heypixelmod.obsoverlay.utils.auth.AuthUtils;

import java.lang.reflect.Method;
import java.util.*;

public class HasValueManager {
    private final List<HasValue> hasValues = new ArrayList<>();
    private final Map<String, HasValue> nameMap = new HashMap<>();

    public HasValueManager() {

    }

    public void registerHasValue(HasValue hasValue) {
        this.hasValues.add(hasValue);
        this.nameMap.put(hasValue.getName().toLowerCase(), hasValue);
    }

    public HasValue getHasValue(String name) {
        return this.nameMap.get(name.toLowerCase());
    }

    public List<HasValue> getHasValues() {
        return this.hasValues;
    }
}
