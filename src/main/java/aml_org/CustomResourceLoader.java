package aml_org;

import amf.client.remote.Content;
import amf.client.resource.FileResourceLoader;
import amf.client.resource.ResourceLoader;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Custom resource loader defined to exemplify the usage of AMF's Resource Loader interface
 */
public class CustomResourceLoader implements ResourceLoader {
    private final FileResourceLoader resourceLoader = new FileResourceLoader();
    private static final Pattern CUSTOM_PATH_PATTERN = Pattern.compile("^CustomProtocol/");

    @Override
    public CompletableFuture<Content> fetch(String path) {
        final String normalizedPath = path.substring(CUSTOM_PATH_PATTERN.pattern().length() - 1);
        return resourceLoader.fetch(new File(normalizedPath).getAbsolutePath());
    }

    @Override
    public boolean accepts(String resource) {
        if (resource == null || resource.isEmpty()) return false;
        final Matcher matcher = CUSTOM_PATH_PATTERN.matcher(resource);
        return matcher.find();
    }
}
