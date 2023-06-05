package com.equo.application.client;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.atomos.Atomos;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;

import com.equo.chromium.ChromiumBrowser;

public class EquoApplication {

  private static Path getCacheLocation() {
    // Comply with XDG (https://specifications.freedesktop.org/basedir-spec/basedir-spec-latest.html)
    Path baseDir;
    String cacheHome = System.getProperty("XDG_CACHE_HOME");
    if (cacheHome != null) {
      baseDir = Paths.get(cacheHome);
    } else {
      baseDir = Paths.get(System.getProperty("user.home"), ".cache");
    }
    return Paths.get(baseDir.toString(), "equo", "application");
  }

  public static void launch(String url) {

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

    ChromiumBrowser.standalone(url);
    ChromiumBrowser.startBrowsers();
    System.exit(0);
  }

  public static void main(String args[]) {
    launch("https://equo.dev");
  }

}
