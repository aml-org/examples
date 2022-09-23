package javaPlatform;

import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.reference.CachedReference;
import amf.core.client.platform.reference.ClientUnitCache;
import amf.core.client.platform.resource.ResourceNotFound;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.Optional.ofNullable;

public class ImmutableCache implements ClientUnitCache {

    private final Map<String, CachedReference> cache;

    public ImmutableCache() {
        this.cache = new HashMap<>();
    }

    public ImmutableCache(Map<String, CachedReference> cache) {
        this.cache = cache;
    }

    public ImmutableCache add(String alias, String url, BaseUnit unit) {
        Map<String, CachedReference> next = copy(this.cache);
        next.put(alias, new CachedReference(url, unit));
        return new ImmutableCache(next);
    }

    @Override
    public CompletableFuture<amf.core.client.platform.reference.CachedReference> fetch(String url) {
        return ofNullable(cache.get(url))
                .map(CompletableFuture::completedFuture)
                .orElseGet(() -> failed(new ResourceNotFound(url)));
    }

    private <T> CompletableFuture<T> failed(Throwable throwable) {
        CompletableFuture<T> result = new CompletableFuture<>();
        result.completeExceptionally(throwable);
        return result;
    }

    private <K, V> Map<K, V> copy(Map<K, V> map) {
        final Map<K, V> result = new HashMap<>();
        map.entrySet().stream().forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        return result;
    }
}
