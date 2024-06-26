package com.equo.application.client;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import org.apache.felix.atomos.Atomos;
import org.osgi.framework.Constants;

import com.equo.application.client.exceptions.AppNameNotSpecifiedException;
import com.equo.chromium.ChromiumBrowser;
import com.equo.middleware.api.IMiddlewareService;

/**
 * EquoApp facilitates the easy creation of Chromium-based applications and
 * their customization.
 */
public class EquoApp {
  public static final String CHROMIUM_ARGS = "chromium.args";
  public static final String NEW_TAB_URL_SWITCH = "--new-tab-url";
  public static final String APP_ID_SWITCH = "--app-id";
  private static final String CLASSPATH_SCHEME = "equo";
  private static final String RESOURCE_NOT_FOUND_MSG = "Resource not found: ";
  private final IMiddlewareService middlewareService = IMiddlewareService.findServiceReference();
  private static String CUSTOM_URL = null;
  private static String APP_ID = null;

  private EquoApp(String appName) {
    setAppName(appName);
    checkAppId();
  }

  private EquoApp() {
    checkAppId();
  }

  /**
   * Creates and returns a new instance of the EquoApp class with the specified
   * appName.
   * 
   * @param appName Represents the unique identifier for the EquoApp.
   * @return An instance of the EquoApp class with the specified appName.
   */
  public static EquoApp create(String appName) {
    return new EquoApp(appName);
  }

  /**
   * Creates and returns a new instance of the EquoApp class.
   * 
   * @return An instance of the EquoApp class.
   */
  public static EquoApp create() {
    return new EquoApp();
  }

  private static void addChromiumArgs(String... values) {
    var chromiumArgs = System.getProperty(CHROMIUM_ARGS, "");
    var builder = new StringBuilder(chromiumArgs);
    for (int i = 0; i < values.length; i++) {
      var value = values[i];
      if (i == 0 && !chromiumArgs.isBlank()) {
        builder.append(";");
      }
      builder.append(value);
    }
    System.setProperty(CHROMIUM_ARGS, builder.toString());
  }

  /**
   * Sets the URL of the new tab page for the chromium app.
   * 
   * @param url Represents the URL of the new tab page.
   * @return The method is returning an instance of the EquoApp class.
   */
  public EquoApp setNewTabPageURL(String url) {
    if (Boolean.getBoolean("chrome_runtime")) {
      addChromiumArgs(NEW_TAB_URL_SWITCH + "=" + url);
    }
    return this;
  }

  /**
   * Sets the app name.
   * 
   * @param appName Represents the name of the application.
   */
  public static void setAppName(String appName) {
    APP_ID = sanitizeAppName(appName);
    CUSTOM_URL = APP_ID + ".app";
    addChromiumArgs(APP_ID_SWITCH + "=" + APP_ID);
  }

  private static String sanitizeAppName(String appName) {
    var appId = appName.toLowerCase()
        .replaceAll("[^a-z0-9 -]", "")
        .replaceAll(" +", " ")
        .trim()
        .replaceAll(" ", "-");
    while (appId.startsWith("-")) {
      appId = appId.substring(1);
    }
    if (!appId.isEmpty()) {
      appId = appId.substring(0, Math.min(50, appId.length()));
      while (appId.charAt(appId.length() - 1) == '-') {
        appId = appId.substring(0, appId.length() - 1);
      }
    }
    return appId;
  }

  private static void checkAppId() {
    if (APP_ID == null || APP_ID.isBlank()) {
      throw new AppNameNotSpecifiedException();
    }
  }

  /**
   * Gets the app name.
   * 
   * @return Gets the app name.
   */
  public static String getAppName() {
    checkAppId();
    return APP_ID;
  }

  private void launch_(String url) {
    ChromiumBrowser.standalone(url);
    ChromiumBrowser.startBrowsers();
    System.exit(0);
  }

  /**
   * Adds an OSGi compatibility layer to the EquoApp by starting Felix in a daemon
   * thread.
   * 
   * @return The method is returning an instance of the EquoApp class.
   */
  public EquoApp addOSGICompatibilityLayer() {
    /*
     * Start Felix in a daemon thread. It inherits this property and allows us easy
     * control from the main thread.
     */
    var daemonStartup = new Thread(() -> {
      var atomos = Atomos.newAtomos();
      var config = new HashMap<String, String>();
      config.put("atomos.enable.resolution.errors", "true");
      config.put(Constants.FRAMEWORK_STORAGE, ConfigLocations.cacheHome().toString());
      var framework = atomos.newFramework(config);
      try {
        framework.init();
        framework.start();
      } catch (Exception e) {
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

  /**
   * Enable the browser UI in an EquoApp.
   * 
   * @return The method is returning an instance of the EquoApp class.
   */
  public EquoApp withBrowserUI() {
    System.setProperty("chrome_runtime", "true");
    return this;
  }

  /**
   * Launches the application with the given URI.
   * 
   * @param uri Represents either the name of the file to be launched
   *            or a URL based on HTTP/HTTPS, this protocol is 
   *            mandatory for URLs.
   */
  public void launch(String uri) {
    if (uri.toLowerCase().startsWith("http")) {
      launch_(uri);
      return;
    }
    var customUri = String.format("%s://%s/", CLASSPATH_SCHEME, CUSTOM_URL);
    var filenameUri = customUri + "index.html";
    if (!uri.isBlank()) {
      filenameUri = customUri + uri;
    }

    middlewareService.addResourceHandler(CLASSPATH_SCHEME, CUSTOM_URL, (request, headers) -> {
      String resourceToFind = request.getUrl().substring(customUri.length());
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceToFind);
      if (inputStream != null) {
        return inputStream;
      } else {
        System.err.println(RESOURCE_NOT_FOUND_MSG + resourceToFind);
        return null;
      }
    });

    launch_(filenameUri);
  }

  /**
   * Launches the application with specified URL.
   * 
   * @param url Represents the URL that needs to be launched.
   */
  public void launch(URL url) {
    launch_(url.toString());
  }

  /**
   * Sets up a resource handler, try to load the index.html and launches the
   * application.
   */
  public void launch() {
    // By default, try to load the index.html in the ClassPath
    launch("");
  }
}
