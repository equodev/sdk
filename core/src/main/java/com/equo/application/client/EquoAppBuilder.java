package com.equo.application.client;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.atomos.Atomos;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;

import com.equo.chromium.ChromiumBrowser;
import com.equo.middleware.api.IMiddlewareService;
	
public class EquoAppBuilder {
	private static final String CLASSPATH_SCHEME = "classpath";
	private static final String CUSTOM_URL = "test.com";
	private static final String CLASSPATH_URI = String.format("%s://%s/", CLASSPATH_SCHEME, CUSTOM_URL);
	private static final String RESOURCE_NOT_FOUND_MSG = "Resource not found: ";
	private IMiddlewareService middlewareService;
	
	public EquoAppBuilder() {
		middlewareService = IMiddlewareService.findServiceReference();
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

	public EquoAppBuilder enableChromeRuntime() {
		System.setProperty("chrome_runtime", "true");
		return this;
	}

	public void launch(String uri) {
		if (uri == null || uri.isEmpty()) {
			uri = CLASSPATH_URI + "index.html";	
		}
		try {
			new URL(uri);
		} catch (MalformedURLException e) {
			if (uri != null) {
				uri = CLASSPATH_URI + uri;
			}
		}

		middlewareService.addResourceHandler(CLASSPATH_SCHEME, "", (request, headers) -> {
			String resourceToFind = request.getUrl().replaceAll(CLASSPATH_URI, "");
	        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceToFind);
	        if (inputStream != null) {
	            return inputStream;
	        } else {
	            System.err.println(RESOURCE_NOT_FOUND_MSG + resourceToFind);
	            return null;
	        }
		});

		ChromiumBrowser.standalone(uri);
		ChromiumBrowser.startBrowsers();
		System.exit(0);
	}
	
	public void launch() {
		// By default, try to load the index.html in the ClassPath
		launch("");
	}
}
