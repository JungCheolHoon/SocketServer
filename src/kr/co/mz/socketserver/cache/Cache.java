package kr.co.mz.socketserver.cache;

import java.util.HashMap;

public class Cache {
    private final HashMap<String,byte []> htmlCache;
    private final HashMap<String, byte []> faviconCache;
    public Cache() {
        htmlCache = new HashMap<>();
        faviconCache = new HashMap<>();
    }
    public Boolean containsHtmlKey(String key){
        return htmlCache.containsKey(key);
    }
    public void removeHtmlCache(String key){
        htmlCache.remove(key);
    }
    public byte [] getHtmlCache(String key){
        return htmlCache.get(key);
    }
    public void putHtmlCache(String key, byte [] value){
        htmlCache.put(key,value);
    }
    public Boolean containsFaviconKey(String key){
        return faviconCache.containsKey(key);
    }
    public byte [] getFaviconCache(String key){
        return faviconCache.get(key);
    }
    public void putFaviconCache(String key, byte [] value){
        faviconCache.put(key,value);
    }
}
