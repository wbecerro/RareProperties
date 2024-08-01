package wbe.rareproperties.properties;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;

import java.util.Random;

public class Capture extends RareProperty {

    public Capture(RareProperties plugin) {
        super(plugin);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        LivingEntity entity = ((EntityDeathEvent) event).getEntity();

        if (!getPlugin().getMobs().contains(entity.getType())) {
            return;
        }

        Random random = new Random();
        int prob = random.nextInt(getConfig().getInt("Properties.Capture.maxProbability"));
        if (getLevel() < prob) {
            return;
        }

        String conversion = entity.getType().toString() + "_SPAWN_EGG";

        Material eggMaterial = Material.valueOf(conversion);

        ItemStack egg = new ItemStack(eggMaterial);
        egg.setAmount(1);
        player.sendMessage(getConfig().getString("Messages.captureSuccess").replace("&", "§"));
        player.playSound(player.getLocation(), Sound.BLOCK_TRIAL_SPAWNER_ABOUT_TO_SPAWN_ITEM, 1F, 1F);
        ((EntityDeathEvent) event).getDrops().add(egg);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        boolean capture = false;
        int level = 0;

        if(player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            return false;
        } else if(player.getInventory().getItemInMainHand().getItemMeta() == null) {
            return false;
        }

        if (!getPlugin().hasProperty(player.getInventory().getItemInMainHand(), "Captura")) {
            return false;
        }

        if (!player.hasPermission("rareproperties.use.capture")) {
            return false;
        }

        PlayerInventory in = player.getInventory();
        if (!capture) {
            ItemStack cursor = in.getItemInMainHand();
            level = checkProperty(cursor, "Captura");
            if (level > 0)
                capture = true;
        }

        if (!capture) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
