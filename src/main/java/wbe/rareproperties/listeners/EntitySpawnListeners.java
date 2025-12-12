package wbe.rareproperties.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import wbe.rareproperties.RareProperties;

import java.util.List;

public class EntitySpawnListeners implements Listener {

    RareProperties plugin;

    public EntitySpawnListeners(RareProperties plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void groupExperienceOnSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if(!(entity instanceof ExperienceOrb experienceOrb)) {
            return;
        }

        List<Entity> nearbyEntities = entity.getNearbyEntities(RareProperties.config.experienceGroupDistance,
                RareProperties.config.experienceGroupDistance,
                RareProperties.config.experienceGroupDistance);

        nearbyEntities.removeIf(nearbyEntity -> !(nearbyEntity instanceof ExperienceOrb));

        int storedExperience = 0;
        for(Entity nearbyEntity : nearbyEntities) {
            ExperienceOrb nearbyOrb = (ExperienceOrb) nearbyEntity;
            storedExperience += nearbyOrb.getExperience();
            nearbyOrb.remove();
        }

        storedExperience += experienceOrb.getExperience();

        experienceOrb.setExperience(storedExperience);
    }
}
