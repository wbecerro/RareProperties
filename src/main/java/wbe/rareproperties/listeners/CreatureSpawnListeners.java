package wbe.rareproperties.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import wbe.rareproperties.RareProperties;

import java.util.Random;

public class CreatureSpawnListeners implements Listener {

    RareProperties plugin;

    public CreatureSpawnListeners(RareProperties plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void addRareItemsOnSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        if(!(entity instanceof Monster)) {
            return;
        }

        if(RareProperties.config.blockSpawnerSpawns) {
            if(event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER)) {
                return;
            }
        }

        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1;
        if(randomNumber > RareProperties.config.specialMobChance) {
            return;
        }

        ItemStack item;
        for(int i=0;i<5;i++) {
            if(random.nextInt(100) > RareProperties.config.itemSlotChance) {
                continue;
            }

            if(random.nextInt(2) > 0) {
                item = RareProperties.itemManager.generateItem(true);
                if(EnchantmentTarget.ARMOR_HEAD.includes(item)) {
                    entity.getEquipment().setHelmet(item);
                    entity.getEquipment().setHelmetDropChance(100F);
                } else if(EnchantmentTarget.ARMOR_TORSO.includes(item)) {
                    entity.getEquipment().setChestplate(item);
                    entity.getEquipment().setChestplateDropChance(100F);
                } else if(EnchantmentTarget.ARMOR_LEGS.includes(item)) {
                    entity.getEquipment().setLeggings(item);
                    entity.getEquipment().setLeggingsDropChance(100F);
                } else if(EnchantmentTarget.ARMOR_FEET.includes(item)) {
                    entity.getEquipment().setBoots(item);
                    entity.getEquipment().setBootsDropChance(100F);
                }
            } else {
                item = RareProperties.itemManager.generateItem(false);
                entity.getEquipment().setItemInMainHand(item);
                entity.getEquipment().setItemInMainHandDropChance(100F);
            }
        }
    }
}
