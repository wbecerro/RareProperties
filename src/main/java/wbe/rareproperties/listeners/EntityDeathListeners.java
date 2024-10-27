package wbe.rareproperties.listeners;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.items.IdentifierTome;
import wbe.rareproperties.items.Socket;
import wbe.rareproperties.properties.mythic.Capture;

import java.util.Random;

public class EntityDeathListeners implements Listener {

    private RareProperties plugin;

    public EntityDeathListeners(RareProperties plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMobDeath(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if(player == null) {
            return;
        }

        // Comprobación de Captura
        Capture capture = new Capture(plugin);
        boolean captureProperty = capture.checkUse(player, event);
        if(captureProperty) {
            capture.applyEffect(player, event);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void addSpecialDropsOnDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if(!(entity instanceof Monster)) {
            return;
        }

        LivingEntity killer = event.getEntity().getKiller();
        if(killer == null) {
            return;
        }

        ItemStack weapon = killer.getEquipment().getItemInMainHand();
        int lootingLevel = 0;
        if(!weapon.getType().equals(Material.AIR)) {
            lootingLevel = weapon.getEnchantments().getOrDefault(Enchantment.LOOTING, 0);
        }

        double socketChance = RareProperties.config.socketChance + RareProperties.config.lootingExtraChance * lootingLevel;
        double tomeChance = RareProperties.config.tomeChance + RareProperties.config.lootingExtraChance * lootingLevel;

        Random random = new Random();
        if(random.nextDouble(100) < socketChance) {
            event.getDrops().add(new Socket(plugin));
        }

        if(random.nextDouble(100) < tomeChance) {
            event.getDrops().add(new IdentifierTome(plugin));
        }
    }
}
