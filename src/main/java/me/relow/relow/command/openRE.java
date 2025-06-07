package me.relow.relow.command;

import me.relow.relow.Config;
import me.relow.relow.GUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class openRE implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player)sender;
            ItemStack handItem = player.getInventory().getItemInMainHand();

            if(handItem == null || handItem.getType().equals(Material.AIR)){
                GUI.GUI1(player,null);
            }else {
                if(handItem.hasItemMeta()){
                    ItemMeta itemMeta = handItem.getItemMeta();
                    List<String> lores = itemMeta.getLore();
                    if(itemMeta.hasLore()){
                        for(int i = 0;i < lores.size();i++){
                            String string = lores.get(i);
                            string = string.replace("§","&");
                            if(string.contains(Config.getLore())){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',Config.getNotAllow()));
                                return false;
                            }
                        }
                    }
                }
                GUI.GUI1(player,handItem);
            }
        }

        return false;
    }
}