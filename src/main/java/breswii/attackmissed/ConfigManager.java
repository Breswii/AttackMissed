package breswii.attackmissed;

import net.minecraftforge.fml.loading.FMLPaths;
import java.io.*;
import java.util.Properties;

public class ConfigManager {


    private static final String FILE_PATH = FMLPaths.CONFIGDIR.get().toString()+"/AttackMissed.properties";

    public static Properties loadConfig() {
        try {

            InputStream inputStream = new FileInputStream(FILE_PATH);
            Properties config = new Properties();
            config.load(inputStream);
            inputStream.close();
            return config;
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found, creating default config...");
            Properties defaultConfig = createDefaultConfig();
            saveConfig(defaultConfig);
            return defaultConfig;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void saveConfig(Properties config) {
        try {
            OutputStream outputStream = new FileOutputStream(FILE_PATH);
            config.store(outputStream, "AttackMissed Mod Config:");
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties createDefaultConfig() {
        Properties defaultConfig = new Properties();
        defaultConfig.setProperty("zombie", "0.5");
        defaultConfig.setProperty("skeleton", "0.4");
        return defaultConfig;
    }
}
