package Util;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MapWrapper implements Map<String, String> {

    private Map<String, String> map;

    public MapWrapper(Map<String, String> map) {
        this.map = map;
    }

    public MapWrapper execute(Function function) {
        function.execute(this);
        return this;
    }

    public MapWrapper filter(Predicate<Entry<String, String>> predicate) {
        Map<String, String> newMap = new LinkedHashMap<>();
        map.entrySet()
                .stream()
                .sequential()
                .filter(predicate)
                .collect(
                        Collectors.toList()
                ).forEach(e -> newMap.put(e.getKey(), e.getValue()));
        map = newMap;
        return this;
    }

    public void addAllToBegin(Map<String, String> map) {
        Map<String, String> clone = new LinkedHashMap<>(map);
        clone.putAll(this.map);
        clone.putAll(map); // Rewrite some values
        this.map = clone;
    }

    public void addAllToEnd(Map<String, String> map) {
        this.map.putAll(map);
    }

    public String toJson() {
        return "{" + map.entrySet().stream().map(e ->
                        ",\n  " + e.getKey() + ": " +
                                (e.getValue().startsWith("{comment") ?
                                        e.getValue() :
                                        "\"" + e.getValue() + "\""
                                )
        ).collect(Collectors.joining()).replaceFirst(",", "") + "\n}\n";
    }










    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsKey(value);
    }

    @Override
    public String get(Object key) {
        return map.get(key);
    }

    @Override
    public String put(String key, String value) {
        return map.put(key, value);
    }

    @Override
    public String remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<String> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return map.entrySet();
    }
}
