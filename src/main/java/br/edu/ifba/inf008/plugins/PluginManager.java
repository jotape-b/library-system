package br.edu.ifba.inf008.plugins;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class PluginManager {
    private List<ReportPlugin> plugins = new ArrayList<>();

    public void loadPlugins(String pluginDirectory) throws Exception {
        File dir = new File(pluginDirectory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Diretório de plugins não encontrado: " + pluginDirectory);
            return;
        }
    
        File[] files = dir.listFiles((file, name) -> name.endsWith(".jar"));
        if (files == null || files.length == 0) {
            System.out.println("Nenhum JAR encontrado no diretório: " + pluginDirectory);
            return;
        }
    
        for (File file : files) {
            System.out.println("Tentando carregar plugin: " + file.getName());
            URL jarURL = file.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL});
            ServiceLoader<ReportPlugin> loader = ServiceLoader.load(ReportPlugin.class, classLoader);
    
            boolean foundPlugin = false;
            for (ReportPlugin plugin : loader) {
                System.out.println("Plugin carregado: " + plugin.getPluginName());
                System.out.println("Classe do plugin: " + plugin.getClass().getName());  // Adicionando a depuração da classe
                plugins.add(plugin);
                foundPlugin = true;
            }
    
            if (!foundPlugin) {
                System.out.println("Nenhum plugin válido encontrado em: " + file.getName());
            }
        }
    }
    

    public List<ReportPlugin> getPlugins() {
        return plugins;
    }

    public void clearPlugins() {
        plugins.clear();
    }
}
