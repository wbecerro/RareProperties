package wbe.rareproperties.properties.epic;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.Random;

public class Critic extends RareProperty {

    public Critic(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "critic", "Crítico");
        setDescription(getConfig().getStringList("Properties.Critic.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        double damage = ((EntityDamageByEntityEvent) event).getDamage();

        Random rand = new Random();
        int random = rand.nextInt(100);

        if (random > getLevel() * getConfig().getInt("Properties.Critic.chancePerLevel")) {
            return;
        }

        damage = damage * (1 + getLevel() * getConfig().getDouble("Properties.Critic.extraDamage"));
        ((EntityDamageByEntityEvent) event).setDamage(damage);
        player.playSound(player.getLocation(), Sound.valueOf(getConfig().getString("Properties.Critic.sound")), 0.5F, 1.0F);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if (!player.hasPermission("rareproperties.use.critic")) {
            return false;
        }

        if (!utilities.hasProperty(player.getInventory().getItemInMainHand(), "Crítico")) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        level = checkProperty(item, "Crítico");

        if (level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
