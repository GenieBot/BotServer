package io.sponges.bot.server.module;

import io.sponges.bot.api.module.Module;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ModuleLoader {

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
    public List<Module> load(File directory) throws IOException, ClassNotFoundException {
        if (!directory.exists()) {
            directory.mkdir();
            return null;
        }
        List<Module> modules = new ArrayList<>();
        for (File file : directory.listFiles()) {
            JarFile jarFile = new JarFile(file);
            Enumeration enumeration = jarFile.entries();
            URL[] urls = { new URL("jar:file:" + directory.getName() + File.separatorChar + file.getName() + "!/") };
            URLClassLoader loader = URLClassLoader.newInstance(urls);

            while (enumeration.hasMoreElements()) {
                JarEntry entry = (JarEntry) enumeration.nextElement();
                if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
                    continue;
                }
                String className = entry.getName();
                className = className.substring(0, className.length() - 6).replace("/", ".");
                Class<?> c = loader.loadClass(className);
                Class<? extends Module> moduleClass;
                try {
                    moduleClass = c.asSubclass(Module.class);
                } catch (ClassCastException ignored) {
                    continue;
                }
                try {
                    Module module = moduleClass.newInstance();
                    modules.add(module);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return modules;
    }

}
