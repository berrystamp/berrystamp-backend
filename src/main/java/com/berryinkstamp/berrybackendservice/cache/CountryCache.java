package com.berryinkstamp.berrybackendservice.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Locale;
import java.util.Objects;

public class CountryCache {

    private LoadingCache<String, String> loadingCache;

    private CountryCache() {
        loadingCache = CacheBuilder.newBuilder()
                .build(new CacheLoader<>() {
                    public String load(String key) {
                        return null;
                    }
                });
        loadData();
    }

    private static class SingletonHelper {
        private static final CountryCache INSTANCE = new CountryCache();
    }

    public static CountryCache getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public void loadData() {
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            loadingCache.put(l.getDisplayCountry().toUpperCase(), l.getDisplayCountry().toUpperCase());
        }
    }

    public String get(String key) {
        try {
            if (Objects.nonNull(key)) {
                key = key.toUpperCase().trim();
            }
            return loadingCache.get(key);
        } catch (Exception e) {
            return null;
        }
    }

}
