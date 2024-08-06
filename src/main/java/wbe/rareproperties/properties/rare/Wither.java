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

public class Wither extends RareProperty {

    public Wither(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "wither", "Descomposición");
        setDescription(getConfig().getStringList("Properties.Wither.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        Random rand = new Random();
        int random = rand.nextInt(100);

        if (random > getLevel() * getConfig().getInt("Properties.Wither.chancePerLevel")) {
            return;
        }

        LivingEntity damaged = (LivingEntity) ((EntityDamageByEntityEvent) event).getEntity();
        PotionEffect potion = new PotionEffect(PotionEffectType.WITHER, getConfig().getInt("Properties.Wither.duration") * 20,
                getConfig().getInt("Properties.Wither.modifierPerLevel") - 1);
        damaged.addPotionEffect(potion);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if (!player.hasPermission("rareproperties.use.wither")) {
            return false;
        }

        if (!utilities.hasProperty(player.getInventory().getItemInMainHand(), "Descomposición")) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        level = checkProperty(item, "Descomposición");

        if (level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
