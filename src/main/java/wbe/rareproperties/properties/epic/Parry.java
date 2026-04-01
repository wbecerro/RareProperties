package wbe.rareproperties.properties.epic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

public class Parry extends RareProperty {

    public static HashMap<Player, Long> playerParry = new HashMap<>();

    public Parry(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "parry", RareProperties.propertyConfig.parryName);
        setDescription(RareProperties.propertyConfig.parryDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        playerParry.putIfAbsent(player, 0L);

        int cooldown = RareProperties.propertyConfig.parryCooldownTicks - RareProperties.propertyConfig.parryCooldownTicksPerLevel * getLevel();
        if(Instant.now().getEpochSecond() - playerParry.get(player) > cooldown / 20) {
            playerParry.put(player, Instant.now().getEpochSecond());
        }
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 1;

        PlayerInventory inventory = player.getInventory();
        level = checkHands(inventory, getExternalName());

        if(level < 0) {
            return false;
        }

        if(!inventory.getItemInMainHand().getType().equals(Material.SHIELD)) {
            if(!inventory.getItemInOffHand().getType().equals(Material.SHIELD)) {
                return false;
            }
        }

        setLevel(level);
        return true;
    }
}
