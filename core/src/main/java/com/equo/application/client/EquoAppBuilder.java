package com.equo.application.client;

import com.equo.chromium.ChromiumBrowser;
import com.equo.middleware.api.IMiddlewareService;
import org.apache.felix.atomos.Atomos;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class EquoAppBuilder {
    public static final String CHROMIUM_ARGS = "chromium.args";
    public static final String NEW_TAB_URL_SWITCH = "new-tab-url";
    public static final String APP_ID_SWITCH = "app-id";
    private static final String CLASSPATH_SCHEME = "classpath";
    private static final String CUSTOM_URL = "equo.app";
    private static final String CLASSPATH_URI = String.format("%s://%s/", CLASSPATH_SCHEME, CUSTOM_URL);
    private static final String RESOURCE_NOT_FOUND_MSG = "Resource not found: ";
    private final IMiddlewareService middlewareService;

    public EquoAppBuilder(String appId) {
        setAppId(appId);
        middlewareService = IMiddlewareService.findServiceReference();
    }

    private void addChromiumArgs(String... values) {
        StringBuilder chromium_args = new StringBuilder(System.getProperty(CHROMIUM_ARGS, ""));
        for (String value : values) {
            chromium_args.append(";").append(value);
        }
        System.setProperty(CHROMIUM_ARGS, chromium_args.toString());
    }

    public EquoAppBuilder setNewTabPageURL(String url) {
        if (Boolean.getBoolean("chrome_runtime")) {
            addChromiumArgs(NEW_TAB_URL_SWITCH + "=" + url);
        }
        return this;
    }

    private void setAppId(String appID) {
        addChromiumArgs(APP_ID_SWITCH + "=" + appID);
    }

    private void _launch(String url) {
        ChromiumBrowser.standalone(url);
        ChromiumBrowser.startBrowsers();
        System.exit(0);
    }

    private Path getCacheLocation() {
        // Comply with XDG
        // (https://specifications.freedesktop.org/basedir-spec/basedir-spec-latest.html)
        Path baseDir;
        String cacheHome = System.getProperty("XDG_CACHE_HOME");
        if (cacheHome != null) {
            baseDir = Paths.get(cacheHome);
        } else {
            baseDir = Paths.get(System.getProperty("user.home"), ".cache");
        }
        return Paths.get(baseDir.toString(), "equo", "application");
    }

    public EquoAppBuilder addOSGICompatibilityLayer() {
        /*
         * Start Felix in a daemon thread. It inherits this property and allows us easy
         * control from the main thread.
         */
        Thread daemonStartup = new Thread(() -> {
            Atomos atomos = Atomos.newAtomos();
            Map<String, String> config = new HashMap<>();
            config.put("atomos.enable.resolution.errors", "true");
            config.put(Constants.FRAMEWORK_STORAGE, getCacheLocation().toString());
            Framework framework = atomos.newFramework(config);
            try {
                framework.init();
                framework.start();
            } catch (BundleException e) {
                System.err.println("Error initializing the application. " + e);
            }
        });

        daemonStartup.setDaemon(true);
        daemonStartup.start();
        try {
            daemonStartup.join();
        } catch (InterruptedException e) {
            System.err.println("Application startup interrupted. " + e);
            System.exit(1);
        }
        return this;
    }

    public EquoAppBuilder useChromeUI() {
        System.setProperty("chrome_runtime", "true");
        return this;
    }

    public void launch(String filename) {
        String uri = filename;
        if (uri == null || uri.isEmpty()) {
            uri = CLASSPATH_URI + "index.html";
        }

        middlewareService.addResourceHandler(CLASSPATH_SCHEME, CUSTOM_URL, (request, headers) -> {
            String resourceToFind = request.getUrl().substring(CLASSPATH_URI.length());
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceToFind);
            if (inputStream != null) {
                return inputStream;
            } else {
                System.err.println(RESOURCE_NOT_FOUND_MSG + resourceToFind);
                return null;
            }
        });

        _launch(uri);
    }

    public void launch(URL url) {
        _launch(url.toString());
    }

    public void launch() {
        // By default, try to load the index.html in the ClassPath
        launch("");
    }
}
