package com.test.xzha.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by zhabba on 06.08.15.
 */
public class RouterCacheTest {
    private String preConfiguredCacheName = "routes";
    private String unConfiguredCacheName = "somecache";

    @Test
    public void getCacheManagerTest() {
        RouterCache preConfiguredCache = new RouterCache(preConfiguredCacheName);
        CacheManager cm1 = preConfiguredCache.getCacheManager();
        CacheManager cm2 = CacheManager.getInstance();
        assertNotNull(cm1);
        assertEquals(cm1, cm2);
    }

    @Test
    public void initPreConfiguredCacheByNameTest() {
        RouterCache preConfiguredCache = new RouterCache(preConfiguredCacheName);
        Cache cache1 = CacheManager.getInstance().getCache(preConfiguredCacheName);
        RouterCache unConfiguredCache = new RouterCache(unConfiguredCacheName);
        Cache cache2 = CacheManager.getInstance().getCache(unConfiguredCacheName);
        List<String> cacheNames = Arrays.asList(CacheManager.getInstance().getCacheNames());
        assertNotNull(preConfiguredCache);
        assertNotNull(unConfiguredCache);
        assertNotNull(cache1);
        assertNotNull(cache2);
        assertEquals(2, cacheNames.size());
        assertTrue(cacheNames.contains(preConfiguredCacheName));
        assertTrue(cacheNames.contains(unConfiguredCacheName));
    }

    @Test
    public void putEltToCacheTest() {
        CacheManager.getInstance().clearAll();
        RouterCache preConfiguredCache = new RouterCache(preConfiguredCacheName);
        String[] routeToSave = new String[]{"somePrefix", "somePrice", "someOpName"};
        Cache cache = CacheManager.getInstance().getCache(preConfiguredCacheName);
        assertEquals(0, cache.getSize());
        preConfiguredCache.put("2128506", routeToSave);
        assertEquals(1, cache.getSize());
    }

    @Test
    public void getEltFromCache() {
        CacheManager.getInstance().clearAll();
        RouterCache preConfiguredCache = new RouterCache(preConfiguredCacheName);
        String[] routeToSave = new String[]{"somePrefix", "somePrice", "someOpName"};
        preConfiguredCache.put("2128506", routeToSave);
        String[] routeFromCache = preConfiguredCache.get("2128506");
        assertNotNull(routeFromCache);
        assertEquals(3, routeFromCache.length);
        assertArrayEquals(routeFromCache, routeToSave);
    }

    @Test
    public void clearTest() {
        CacheManager.getInstance().clearAll();
        RouterCache preConfiguredCache = new RouterCache(preConfiguredCacheName);
        String[] routeToSave = new String[]{"somePrefix", "somePrice", "someOpName"};
        preConfiguredCache.put("2128506", routeToSave);
        preConfiguredCache.put("2128507", routeToSave);
        preConfiguredCache.put("2128508", routeToSave);
        assertEquals(3, CacheManager.getInstance().getCache(preConfiguredCacheName).getSize());
        preConfiguredCache.clear();
        assertEquals(0, CacheManager.getInstance().getCache(preConfiguredCacheName).getSize());
    }
}
