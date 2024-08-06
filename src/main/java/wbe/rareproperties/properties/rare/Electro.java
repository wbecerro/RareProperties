package wbe.rareproperties.properties.rare;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.Random;

public class Electro extends RareProperty {

    public Electro(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "electro", "Electro");
        setDescription(getConfig().getStringList("Properties.Electro.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        double damage = ((EntityDamageByEntityEvent) event).getDamage();

        Random rand = new Random();
        int random = rand.nextInt(100);

        if (random > getLevel() * getConfig().getInt("Properties.Electro.chancePerLevel")) {
            return;
        }

        damage = damage + getLevel() * getConfig().getDouble("Properties.Electro.damagePerLevel");
        ((EntityDamageByEntityEvent) event).setDamage(damage);
        player.playSound(player.getLocation(), Sound.valueOf(getConfig().getString("Properties.Electro.sound")), 1.0F, 1.0F);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if (!player.hasPermission("rareproperties.use.electro")) {
            return false;
        }

        if (!utilities.hasProperty(player.getInventory().getItemInMainHand(), "Electro")) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        level = checkProperty(item, "Electro");

        if (level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
