package me.relow.relow;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GUI {

    private static final int GUI_SIZE = 36;
    private static final String GUI_TITLE = "附魔分離系統";

    public static void GUI1(Player player, ItemStack itemStack) {
        // 創建GUI介面
        Inventory enchantmentGUI = Bukkit.createInventory(player, GUI_SIZE, GUI_TITLE);

        // 設置邊界物品
        setupBorderItems(enchantmentGUI);

        // 處理主要物品顯示
        handleMainItem(enchantmentGUI, player, itemStack);

        // 開啟GUI
        player.openInventory(enchantmentGUI);
    }

    /**
     * 設置GUI邊界物品
     */
    private static void setupBorderItems(Inventory gui) {
        // 邊界物品
        ItemStack yellowPane = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemStack book = new ItemStack(Material.BOOK);
        ItemStack sign = createControlSign();

        // 設置特定位置的邊界
        setBorderPanes(gui, yellowPane);

        // 設置控制標誌
        gui.setItem(1, sign);

        // 填充書本到其他位置
        fillBooksInSlots(gui, book);
    }

    /**
     * 創建控制標誌物品
     */
    private static ItemStack createControlSign() {
        ItemStack sign = new ItemStack(Material.OAK_SIGN);
        ItemMeta signMeta = sign.getItemMeta();

        if (signMeta != null) {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.translateAlternateColorCodes('&', "&f左鍵點擊&a分離附魔 &f| 右鍵點擊&b洗去附魔"));
            signMeta.setLore(lore);
            sign.setItemMeta(signMeta);
        }

        return sign;
    }

    /**
     * 設置黃色玻璃邊界
     */
    private static void setBorderPanes(Inventory gui, ItemStack pane) {
        int[] borderSlots = {0, 2, 9, 11, 18, 19, 20};
        for (int slot : borderSlots) {
            gui.setItem(slot, pane);
        }
    }

    /**
     * 在指定範圍填充書本
     */
    private static void fillBooksInSlots(Inventory gui, ItemStack book) {
        int[][] ranges = {
                {3, 9}, {12, 18}, {21, 27}, {27, 34}, {34, 40}, {40, 47}, {47, 54}
        };

        for (int[] range : ranges) {
            for (int i = range[0]; i < range[1]; i++) {
                if (i < GUI_SIZE) { // 確保不超出GUI大小
                    gui.setItem(i, book);
                }
            }
        }
    }

    /**
     * 處理主要物品的顯示和附魔分離
     */
    private static void handleMainItem(Inventory gui, Player player, ItemStack itemStack) {
        if (isValidItem(itemStack)) {
            // 將物品放在位置10
            gui.setItem(10, itemStack);

            // 處理附魔分離
            processEnchantments(gui, player, itemStack);
        } else {
            // 顯示提示物品
            gui.setItem(10, createHintItem());
        }
    }

    /**
     * 檢查物品是否有效
     */
    private static boolean isValidItem(ItemStack itemStack) {
        return itemStack != null &&
                !itemStack.getType().equals(Material.AIR) &&
                !itemStack.getType().equals(Material.ENCHANTED_BOOK);
    }

    /**
     * 創建提示物品
     */
    private static ItemStack createHintItem() {
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();

        if (barrierMeta != null) {
            barrierMeta.setDisplayName(
                    ChatColor.translateAlternateColorCodes('&', "&d請手持需要分離的物品打開GUI!")
            );
            barrier.setItemMeta(barrierMeta);
        }

        return barrier;
    }

    /**
     * 處理物品附魔的分離顯示
     */
    private static void processEnchantments(Inventory gui, Player player, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;

        Map<Enchantment, Integer> enchantments = itemMeta.getEnchants();
        if (enchantments.isEmpty()) return;

        int currentSlot = 3;
        int enchantmentCount = 0;
        int maxAllowed = judgePermission(player);

        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            Integer level = entry.getValue();

            // 檢查是否在不可移除列表中
            if (Config.getNoRemove().contains(enchantment.getKey().getKey())) {
                continue;
            }

            // 檢查權限限制
            if (enchantmentCount >= maxAllowed) {
                break;
            }

            // 創建附魔書
            ItemStack enchantedBook = createEnchantedBook(enchantment, level);
            gui.setItem(currentSlot, enchantedBook);

            enchantmentCount++;
            currentSlot = getNextSlot(currentSlot);

            // 檢查是否超出範圍
            if (currentSlot > 54) {
                player.sendMessage("再多的附魔請分離前面的一部分之後再來哦~");
                break;
            }
        }
    }

    /**
     * 創建附魔書物品
     */
    private static ItemStack createEnchantedBook(Enchantment enchantment, Integer level) {
        ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();

        if (storageMeta != null) {
            storageMeta.addStoredEnchant(enchantment, level, true);
            enchantedBook.setItemMeta(storageMeta);
        }

        return enchantedBook;
    }

    /**
     * 獲取下一個可用的槽位
     */
    private static int getNextSlot(int currentSlot) {
        currentSlot++;

        // 跳過邊界位置
        if (currentSlot == 9) {
            currentSlot += 3; // 跳到12
        } else if (currentSlot > 17 && currentSlot < 21) {
            currentSlot = 21; // 跳到21
        }

        return currentSlot;
    }

    /**
     * 判斷玩家權限等級
     * @param player 玩家對象
     * @return 允許的最大附魔數量
     */
    public static int judgePermission(Player player) {
        // 遍歷權限列表，返回玩家所在權限組能夠處理的附魔數量
        for (int i = 0; i < Config.getPermissionsList().size(); i++) {
            if (player.hasPermission(Config.getPermissionsList().get(i).getName())) {
                return Config.getPermissionsList().get(i).getNum();
            }
        }

        // 返回預設數量
        return Config.getDefaultNum();
    }
}