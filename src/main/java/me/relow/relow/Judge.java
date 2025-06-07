package me.relow.relow;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class Judge {

    RELOW re = RELOW.getPlugin(RELOW.class);

    public static boolean JudgeLevel(Player player, int level) {
        return player.getLevel() >= level;
    }

    public static boolean JudgeMoney(Player player, double money) {
        Economy economy = RELOW.getEconomy();
        EconomyResponse economyResponse = economy.withdrawPlayer(player, money);

        if (RELOW.getEconomy().getBalance(player) >= money) {

            //player.sendMessage(String.valueOf(RELOW.getEconomy().getBalance(player)));
            return economyResponse.transactionSuccess();
        } else {
            return false;
        }

    }
}
