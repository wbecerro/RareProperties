package wbe.rareproperties.properties.rare;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.Random;

public class Poison extends RareProperty {

    public Poison(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "poison", "Putrefacción");
        setDescription(getConfig().getStringList("Properties.Poison.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        Random rand = new Random();
        int random = rand.nextInt(100);

        if (random > getLevel() * getConfig().getInt("Properties.Poison.chancePerLevel")) {
            return;
        }

        LivingEntity damaged = (LivingEntity) ((EntityDamageByEntityEvent) event).getEntity();
        PotionEffect potion = new PotionEffect(PotionEffectType.POISON, getConfig().getInt("Properties.Poison.duration") * 20,
                getConfig().getInt("Properties.Poison.modifierPerLevel") - 1);
        damaged.addPotionEffect(potion);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if (!player.hasPermission("rareproperties.use.poison")) {
            return false;
        }

        if (!utilities.hasProperty(player.getInventory().getItemInMainHand(), "Putrefacción")) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        level = checkProperty(item, "Putrefacción");

        if (level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
