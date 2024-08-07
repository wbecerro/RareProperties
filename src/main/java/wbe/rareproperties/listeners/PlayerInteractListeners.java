package wbe.rareproperties.listeners;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.mythic.*;

public class PlayerInteractListeners implements Listener {

    private RareProperties plugin;

    private FileConfiguration config;

    public PlayerInteractListeners(RareProperties plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteractRareItem(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }

        Player player = event.getPlayer();

        if(player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            return;
        } else if(player.getInventory().getItemInMainHand().getItemMeta() == null) {
            return;
        }

        // Comprobación de Reparación
        Repair repair = new Repair(plugin);
        boolean repairProperty = repair.checkUse(player, event);
        if(repairProperty) {
            repair.applyEffect(player, event);
        }

        // Comprobación de Andanada
        Burst burst = new Burst(plugin);
        boolean burstProperty = burst.checkUse(player, event);
        if(burstProperty) {
            burst.applyEffect(player, event);
        }

        // Comprobación de Teletransporte
        Teleport teleport = new Teleport(plugin);
        boolean teleportProperty = teleport.checkUse(player, event);
        if(teleportProperty) {
            teleport.applyEffect(player, event);
        }

        // Comprobación de Égida
        Aegis aegis = new Aegis(plugin);
        boolean aegisProperty = aegis.checkUse(player, event);
        if(aegisProperty) {
            aegis.applyEffect(player, event);
        }

        // Comprobación de Presteza
        Promptness promptness = new Promptness(plugin);
        boolean promptnessProperty = promptness.checkUse(player, event);
        if(promptnessProperty) {
            promptness.applyEffect(player, event);
        }

        // Comprobación de Demolición
        Demolition demolition = new Demolition(plugin);
        boolean demolitionProperty = demolition.checkUse(player, event);
        if(demolitionProperty) {
            demolition.applyEffect(player, event);
        }

        // Comprobación de Propulsión
        Propulsion propulsion = new Propulsion(plugin);
        boolean propulsionProperty = propulsion.checkUse(player, event);
        if(propulsionProperty) {
            propulsion.applyEffect(player, event);
        }

        // Comprobación de Sanación
        Healing healing = new Healing(plugin);
        boolean healingProperty = healing.checkUse(player, event);
        if(healingProperty) {
            healing.applyEffect(player, event);
        }

        // Comprobación de Adrenalina
        Adrenaline adrenaline = new Adrenaline(plugin);
        boolean adrenalineProperty = adrenaline.checkUse(player, event);
        if(adrenalineProperty) {
            adrenaline.applyEffect(player, event);
        }
    }
}
