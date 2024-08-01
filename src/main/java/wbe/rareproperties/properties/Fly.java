package wbe.rareproperties.properties;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;

import java.util.HashMap;

public class Fly extends RareProperty {

    public static HashMap<Player, Integer> playersFlying = new HashMap<Player, Integer>();

    public Fly(RareProperties plugin) {
        super(plugin);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        if (player.getFoodLevel() <= 0) {
            player.setAllowFlight(false);
        } else {
            player.setAllowFlight(true);
            player.setFlySpeed(0.1F);
        }

        playersFlying.put(player, getLevel());
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        boolean fly = false;
        int level = 0;

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            Fly.playersFlying.remove(player);
            return false;
        }

        if(!player.hasPermission("rareproperties.use.fly")) {
            Fly.playersFlying.remove(player);
            return false;
        }

        PlayerInventory in = player.getInventory();
        ItemStack[] armor = in.getArmorContents();
        for(ItemStack is : armor) {
            if(is == null) {
                continue;
            }
            level = checkProperty(is, "Vuelo");
            if(level > 0) {
                fly = true;
                break;
            }
        }

        // Si no hay fly en la armadura se comprueba la mano primaria y luego la secundaria.
        if(!fly) {
            ItemStack hand = in.getItemInMainHand();
            level = checkProperty(hand, "Vuelo");
            if(level > 0) {
                fly = true;
            } else {
                ItemStack offHand = in.getItemInOffHand();
                level = checkProperty(offHand, "Vuelo");
                if(level > 0) {
                    fly = true;
                }
            }
        }

        // Si no se encuentra Vuelo en armor o item en la mano sale.
        if(!fly) {
            Fly.playersFlying.remove(player);
            player.setAllowFlight(false);
            return false;
        }

        setLevel(level);
        return true;
    }
}
