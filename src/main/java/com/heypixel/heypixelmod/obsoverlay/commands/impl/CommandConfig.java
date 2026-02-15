package com.heypixel.heypixelmod.obsoverlay.commands.impl;

import com.heypixel.heypixelmod.obsoverlay.Naven;
import com.heypixel.heypixelmod.obsoverlay.commands.Command;
import com.heypixel.heypixelmod.obsoverlay.commands.CommandInfo;
import com.heypixel.heypixelmod.obsoverlay.files.FileManager;
import com.heypixel.heypixelmod.obsoverlay.utils.ChatUtils;
import net.minecraft.client.Minecraft;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

@CommandInfo(
        name = "config",
        description = "Open client config folder or manage cloud configs.",
        aliases = {"conf"}
)
public class CommandConfig extends Command {
    @Override
    public void onCommand(String[] args) {
        if (args.length == 0) {
            showHelp();
            return;
        }

        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "open":
            case "folder":
                openConfigFolder();
                break;
            case "save":
                saveConfig(args.length > 1 ? args[1] : "default");
                break;
            case "load":
                loadConfig(args.length > 1 ? args[1] : "default");
                break;
            case "list":
                listConfigs();
                break;
            default:
                ChatUtils.addChatMessage("§c未知的子命令: " + subCommand);
                showHelp();
                break;
        }
    }

    @Override
    public String[] onTab(String[] args) {
        if (args.length == 1) {
            return new String[]{"open", "folder", "save", "load", "list"};
        }
        if (args.length == 2 && (args[0].equals("save") || args[0].equals("load"))) {
            return getConfigNames();
        }
        return new String[0];
    }

    private void showHelp() {
        ChatUtils.addChatMessage("§b=== 配置命令帮助 ===");
        ChatUtils.addChatMessage("§7/config folder §f- 打开配置文件夹");
        ChatUtils.addChatMessage("§7/config save [名称] §f- 保存当前配置");
        ChatUtils.addChatMessage("§7/config load [名称] §f- 加载指定配置");
        ChatUtils.addChatMessage("§7/config list §f- 列出所有配置");
    }

    private void openConfigFolder() {
        try {
            File configDir = new File(Minecraft.getInstance().gameDirectory, "config/Naven");
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            
            Desktop.getDesktop().browse(configDir.toURI());
            ChatUtils.addChatMessage("§a已打开配置文件夹");
        } catch (IOException e) {
            ChatUtils.addChatMessage("§c无法打开配置文件夹: " + e.getMessage());
        }
    }

    private void saveConfig(String configName) {
        try {
            FileManager fileManager = Naven.getInstance().getFileManager();
            fileManager.saveConfig(configName);
            ChatUtils.addChatMessage("§a配置已保存为: " + configName);
        } catch (Exception e) {
            ChatUtils.addChatMessage("§c保存配置失败: " + e.getMessage());
        }
    }

    private void loadConfig(String configName) {
        try {
            FileManager fileManager = Naven.getInstance().getFileManager();
            fileManager.loadConfig(configName);
            ChatUtils.addChatMessage("§a配置已加载: " + configName);
        } catch (Exception e) {
            ChatUtils.addChatMessage("§c加载配置失败: " + e.getMessage());
        }
    }

    private void listConfigs() {
        try {
            File configDir = new File(Minecraft.getInstance().gameDirectory, "config/Naven");
            if (!configDir.exists()) {
                ChatUtils.addChatMessage("§7暂无配置文件");
                return;
            }
            
            File[] configFiles = configDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (configFiles == null || configFiles.length == 0) {
                ChatUtils.addChatMessage("§7暂无配置文件");
                return;
            }
            
            ChatUtils.addChatMessage("§b可用的配置文件:");
            for (File file : configFiles) {
                String name = file.getName().replace(".json", "");
                ChatUtils.addChatMessage("§7- " + name);
            }
        } catch (Exception e) {
            ChatUtils.addChatMessage("§c列出配置文件失败: " + e.getMessage());
        }
    }

    private String[] getConfigNames() {
        try {
            File configDir = new File(Minecraft.getInstance().gameDirectory, "config/Naven");
            if (!configDir.exists()) {
                return new String[0];
            }
            
            File[] configFiles = configDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (configFiles == null) {
                return new String[0];
            }
            
            String[] names = new String[configFiles.length];
            for (int i = 0; i < configFiles.length; i++) {
                names[i] = configFiles[i].getName().replace(".json", "");
            }
            return names;
        } catch (Exception e) {
            return new String[0];
        }
    }
}
