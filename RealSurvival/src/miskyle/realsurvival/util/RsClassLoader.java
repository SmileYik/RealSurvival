package miskyle.realsurvival.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class RsClassLoader extends ClassLoader {
  private final String rootPath;

  // Left fullClassName | Right fullClassPath
  private HashMap<String, String> classPath;

  /**
   * 根据路径导入一个或多个类文件信息.

   * @param path 路径.
   */
  public RsClassLoader(String path) {
    this(new File(path), RsClassLoader.class.getClassLoader());
  }

  /**
   * 根据路径导入一个或多个类文件信息.

   * @param path 路径
   * @param parent ClassLoader
   */
  public RsClassLoader(String path, ClassLoader parent) {
    this(new File(path), parent);
  }

  /**
   * 根据路径导入一个或多个类文件信息.

   * @param path 路径
   * @param parent ClassLoader
   */
  public RsClassLoader(File path, ClassLoader parent) {
    super(parent);
    rootPath = path.getAbsolutePath();
    classPath = new HashMap<String, String>();
    loadClassPath(path);
  }

  /**
   * 导入路径下一个或多个类文件信息.

   * @param path 路径.
   */
  private void loadClassPath(File path) {
    if (path.isDirectory()) {
      for (File file : path.listFiles()) {
        if (!file.isDirectory()
            && file.getName().substring(
                file.getName().lastIndexOf(".")).equalsIgnoreCase(".class")) {
          loadClass(file);
        } else {
          loadClassPath(file);
        }
      }      
    } else {
      loadClass(path);      
    }
  }

  private void loadClass(File file) {
    String fullPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("."));
    String packageName = fullPath.substring(rootPath.length() + 1).replaceAll("[/\\\\]", ".");
    classPath.put(packageName, file.getAbsolutePath());
  }

  /**
   * 当loadClass无法寻找到指定类,将会使用此方法寻找已导入信息的类.
   */
  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    if (!classPath.containsKey(name)) {
      return super.findClass(name);      
    }
    File classFile = new File(classPath.get(name));
    if (!classFile.exists()) {
      return super.findClass(name);      
    }

    byte[] classData = null;
    InputStream is = null;
    ByteArrayOutputStream baos = null;
    try {
      is = new FileInputStream(classFile);
      baos = new ByteArrayOutputStream();
      byte[] temp = new byte[1024];
      int length = 0;
      while ((length = is.read(temp)) != -1) {
        baos.write(temp, 0, length);
      }
      classData = baos.toByteArray();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (is != null) {
          is.close();          
        }
        if (baos != null) {
          baos.close();          
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if (classData != null) {
      return super.defineClass(name, classData, 0, classData.length);      
    } else {
      return super.findClass(name);      
    }
  }

  /**
   * 获取所有导入的类.

   * @param <T> 类
   * @return 类名(包括包名) 及 类
   */
  public <T> HashMap<String, Class<?>> loadAllClass(Class<T> c) {
    HashMap<String, Class<?>> classes = new HashMap<String, Class<?>>();
    classPath.keySet().forEach(k -> {
      Class<?> clazz = null;
      try {
        clazz = super.loadClass(k);
        if (clazz != null && clazz.asSubclass(c) != null) {
          classes.put(k, clazz);
        }
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (ClassCastException e2) {
        e2.printStackTrace();
      }
    });
    return classes;
  }

  public String getrootPath() {
    return rootPath;
  }

  public HashMap<String, String> getClassPath() {
    return classPath;
  }

}
