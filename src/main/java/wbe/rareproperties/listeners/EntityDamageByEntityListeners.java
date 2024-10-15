package wbe.rareproperties.listeners;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.common.Enlarge;
import wbe.rareproperties.properties.common.Explosion;
import wbe.rareproperties.properties.common.Shrink;
import wbe.rareproperties.properties.epic.Backstab;
import wbe.rareproperties.properties.epic.Critic;
import wbe.rareproperties.properties.epic.Vampirism;
import wbe.rareproperties.properties.legendary.Noctis;
import wbe.rareproperties.properties.legendary.Solem;
import wbe.rareproperties.properties.legendary.Swarm;
import wbe.rareproperties.properties.rare.Cut;
import wbe.rareproperties.properties.rare.Electro;
import wbe.rareproperties.properties.rare.Poison;
import wbe.rareproperties.properties.rare.Wither;
import wbe.rareproperties.properties.uncommon.Fire;
import wbe.rareproperties.properties.uncommon.Freezee;

public class EntityDamageByEntityListeners implements Listener {

    private RareProperties plugin;


    public EntityDamageByEntityListeners(RareProperties plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onHit(EntityDamageByEntityEvent event) {
        if(event.isCancelled()) {
            return;
        }

        if(!(event.getDamager() instanceof Player)) {
            return;
        }

        if(!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity damaged = (LivingEntity) event.getEntity();
        Player player = (Player) event.getDamager();

        if(event.getDamage() <= 0.0D) {
            return;
        }

        if(player.getAttackCooldown() < 0.3) {
            return;
        }

        if(player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            return;
        } else if(player.getInventory().getItemInMainHand().getItemMeta() == null) {
            return;
        }

        // Comprobación de Solem
        Solem solem = new Solem(plugin);
        boolean solemProperty = solem.checkUse(player, event);
        if(solemProperty) {
            solem.applyEffect(player, event);
        }

        // Comprobación de Noctis
        Noctis noctis = new Noctis(plugin);
        boolean noctisProperty = noctis.checkUse(player, event);
        if(noctisProperty) {
            noctis.applyEffect(player, event);
        }

        // Comprobación de Enjambre
        Swarm swarm = new Swarm(plugin);
        boolean swarmProperty = swarm.checkUse(player, event);
        if(swarmProperty) {
            swarm.applyEffect(player, event);
        }

        // Comprobación de Vampirismo
        Vampirism vampirism = new Vampirism(plugin);
        boolean vampirismProperty = vampirism.checkUse(player, event);
        if(vampirismProperty) {
            vampirism.applyEffect(player, event);
        }

        // Comprobación de Puñalada
        Backstab backstab = new Backstab(plugin);
        boolean backstabProperty = backstab.checkUse(player, event);
        if(backstabProperty) {
            backstab.applyEffect(player, event);
        }

        // Comprobación de Crítico
        Critic critic = new Critic(plugin);
        boolean criticProperty = critic.checkUse(player, event);
        if(criticProperty) {
            critic.applyEffect(player, event);
        }

        // Comprobación de Electro
        Electro electro = new Electro(plugin);
        boolean electroProperty = electro.checkUse(player, event);
        if(electroProperty) {
            electro.applyEffect(player, event);
        }

        // Comprobación de Corte
        Cut cut = new Cut(plugin);
        boolean cutProperty = cut.checkUse(player, event);
        if(cutProperty) {
            cut.applyEffect(player, event);
        }

        // Comprobación de Descomposición
        Wither wither = new Wither(plugin);
        boolean witherProperty = wither.checkUse(player, event);
        if(witherProperty) {
            wither.applyEffect(player, event);
        }

        // Comprobación de Putrefacción
        Poison poison = new Poison(plugin);
        boolean poisonProperty = poison.checkUse(player, event);
        if(poisonProperty) {
            poison.applyEffect(player, event);
        }

        // Comprobación de Ígneo
        Fire fire = new Fire(plugin);
        boolean fireProperty = fire.checkUse(player, event);
        if(fireProperty) {
            fire.applyEffect(player, event);
        }

        // Comprobación de Gélido
        Freezee freezee = new Freezee(plugin);
        boolean freezeeProperty = freezee.checkUse(player, event);
        if(freezeeProperty) {
            freezee.applyEffect(player, event);
        }

        // Comprobación de Explosión
        Explosion explosion = new Explosion(plugin);
        boolean explosionProperty = explosion.checkUse(player, event);
        if(explosionProperty) {
            explosion.applyEffect(player, event);
        }

        // Comprobación de Encogimiento
        Shrink shrink = new Shrink(plugin);
        boolean shrinkProperty = shrink.checkUse(player, event);
        if(shrinkProperty) {
            shrink.applyEffect(player, event);
        }

        // Comprobación de Agrandamiento
        Enlarge enlarge = new Enlarge(plugin);
        boolean enlargeProperty = enlarge.checkUse(player, event);
        if(enlargeProperty) {
            enlarge.applyEffect(player, event);
        }
    }
}
