package me.relow.relow.command;

import me.relow.relow.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class reload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player)sender;
            if(player.isOp()){
                Config.loadConfig();
                player.sendMessage("重新加載文件成功!");
            }else {
                player.sendMessage("您沒有權限這麼做!");
            }
        }else {
            Config.loadConfig();
            System.out.println("重新加載文件成功!");
        }

        return false;
    }
}
