package wbe.rareproperties.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
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

        Random random = new Random();
        if(random.nextInt(100) > RareProperties.config.specialMobChance) {
            return;
        }

        if(random.nextInt(100) < RareProperties.config.socketChance) {
            event.getDrops().add(new Socket(plugin));
        }

        if(random.nextInt(100) < RareProperties.config.tomeChance) {
            event.getDrops().add(new IdentifierTome(plugin));
        }
    }
}
