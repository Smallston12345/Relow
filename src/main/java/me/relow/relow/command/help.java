package me.relow.relow.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class help implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            player.sendMessage(ChatColor.BLUE + "=========附魔分離=========");
            player.sendMessage(ChatColor.GOLD + "手持附魔物品输入");
            player.sendMessage(ChatColor.GOLD + "/rel-open 打開分離附魔界面 ");
            if(player.isOp()){
                player.sendMessage(ChatColor.GOLD + "/rel-reload 重新加載插件");
            }
            player.sendMessage(ChatColor.BLUE + "=========附魔分離=========");
        }else {
            System.out.println(ChatColor.BLUE + "=========附魔分離=========");
            System.out.println(ChatColor.GOLD + "手持附魔物品输入");
            System.out.println(ChatColor.GOLD + "/rel-open 打開分離附魔界面 ");
            System.out.println(ChatColor.GOLD + "/rel-reload 重新加載插件");
            System.out.println(ChatColor.BLUE + "=========附魔分離=========");
        }
        return false;
    }
}
