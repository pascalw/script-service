package nl.pwiddershoven.scriptor.repository;

public interface CacheRepository {
    Object get(String id);

    void set(String id, Object data);
}
