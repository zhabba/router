package com.test.xzha.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;

/**
 * Class com.test.xzha.cache.RouterCache
 * created at 06.08.15 - 13:00
 */
public class RouterCache<T> {
    private static final Logger LOG = Logger.getLogger(RouterCache.class);
	private static CacheManager CACHE_MANAGER = CacheManager.getInstance();
	private Cache memCache;

	/**
	 * RouterCache constructor
	 * @param cacheName String cache name
	 */
	public RouterCache (String cacheName) {
		memCache = CACHE_MANAGER.getCache(cacheName);
		if (memCache == null) {
			CACHE_MANAGER.addCache(cacheName);
			memCache = CACHE_MANAGER.getCache(cacheName);
		}
	}

	/**
	 * Obviously returns CacheManager :)
	 * @return
	 */
	public CacheManager getCacheManager() {
		return CACHE_MANAGER;
	}

	/**
	 * Get value from cache
     * @param key String key
     * @return String[] || null
	 */
    public T get(String key) {
        T cachedRoute = null;
        Element elt = memCache.get(key);
        if (elt != null) {
            cachedRoute = (T) elt.getObjectValue();
            LOG.debug("Phone " + key + " cache hits: " + elt.getHitCount());
        }
		return cachedRoute;
	}


	/**
	 * Save value in cache by key
	 * @param key
	 * @param value
	 */
    public void put(String key, T value) {
        memCache.put(new Element(key, value));
	}

	/**
	 * Delete all cached elements
	 */
	public void clear() {
		memCache.removeAll();
	}
}
