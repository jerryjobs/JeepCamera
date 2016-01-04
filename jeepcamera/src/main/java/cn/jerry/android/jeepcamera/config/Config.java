package cn.jerry.android.jeepcamera.config;

import java.io.File;

/**
 * Created by JieGuo on 16/1/4.
 */
public class Config {

  private static final String APP_BASE_DIR = "/sdcard/.jeepCamera";

  private static Config config;

  private Config() {
  }

  public static Config getInstance() {
    if (config == null) {
      config = new Config();
    }

    return config;
  }

  private String getBaseDir() {
    return APP_BASE_DIR;
  }

  private String getFileDir(String fileName) {
    String dir = getBaseDir() + fileName;
    if (!new File(dir).exists()) {
      new File(dir).mkdirs();
    }
    return dir;
  }

  public String picDir() {
    return getFileDir("/pic");
  }
}
