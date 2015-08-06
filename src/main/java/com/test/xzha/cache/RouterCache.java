package com.test.xzha.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;

/**
 * Class com.test.xzha.cache.RouterCache
 * created at 06.08.15 - 13:00
 */
public class RouterCache {
	private static final Logger LOG = Logger.getLogger(RouterCache.class);
	private static CacheManager CACHE_MANAGER = CacheManager.create();
	private Cache memCache;

	/**
	 * RouterCache constructor
	 * @param cacheName String cache name
	 * @param capacity int max number of elements in cache
	 * @param ttl long time to live, sec
	 * @param tti long time to idle, sec
	 */
	public RouterCache (String cacheName, int capacity, long ttl, long tti) {
		memCache = new Cache(cacheName, capacity, false, false, ttl, tti);
		CACHE_MANAGER.addCache(memCache);
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
	 * @param phone String phone
	 * @return String[] || null
	 */
	public String[] get(String phone) {
		String[] cachedRoute = null;
		Element elt = memCache.get(phone);
		if (elt != null) {
			cachedRoute = (String[]) elt.getObjectValue();
			LOG.debug("Phone " + phone + " cache hits: " + elt.getHitCount());
		}
		return cachedRoute;
	}


	/**
	 * Save value in cache by key
	 * @param key
	 * @param value
	 */
	public void put(String key, String[] value) {
		memCache.put(new Element(key, value));
	}

	/**
	 * Delete all cached elements
	 */
	public void clear() {
		memCache.removeAll();
	}
}
