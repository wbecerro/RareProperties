package wbe.rareproperties.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Properties {

    private FileConfiguration config;

    public String flyName;
    public String repairName;
    public String burstName;
    public String teleportName;
    public String captureName;
    public String reinforcedName;
    public String aegisName;
    public String demolitionName;
    public String promptnessName;
    public String propulsionName;
    public String healingName;
    public String adrenalineName;
    public String rocketryName;
    public String purityName;
    public String channelingName;
    public String solemName;
    public String noctisName;
    public String armorName;
    public String swarmName;
    public String executionName;
    public String vampirismName;
    public String backstabName;
    public String criticName;
    public String repulsionName;
    public String electroName;
    public String cutName;
    public String witherName;
    public String poisonName;
    public String fireName;
    public String freezeeName;
    public String instabilityName;
    public String explosionName;
    public String shrinkName;
    public String enlargeName;

    public List<String> flyDescription = new ArrayList<>();
    public List<String> repairDescription = new ArrayList<>();
    public List<String> burstDescription = new ArrayList<>();
    public List<String> teleportDescription = new ArrayList<>();
    public List<String> captureDescription = new ArrayList<>();
    public List<String> reinforcedDescription = new ArrayList<>();
    public List<String> aegisDescription = new ArrayList<>();
    public List<String> demolitionDescription = new ArrayList<>();
    public List<String> promptnessDescription = new ArrayList<>();
    public List<String> propulsionDescription = new ArrayList<>();
    public List<String> healingDescription = new ArrayList<>();
    public List<String> adrenalineDescription = new ArrayList<>();
    public List<String> rocketryDescription = new ArrayList<>();
    public List<String> purityDescription = new ArrayList<>();
    public List<String> channelingDescription = new ArrayList<>();
    public List<String> solemDescription = new ArrayList<>();
    public List<String> noctisDescription = new ArrayList<>();
    public List<String> armorDescription = new ArrayList<>();
    public List<String> swarmDescription = new ArrayList<>();
    public List<String> executionDescription = new ArrayList<>();
    public List<String> vampirismDescription = new ArrayList<>();
    public List<String> backstabDescription = new ArrayList<>();
    public List<String> criticDescription = new ArrayList<>();
    public List<String> repulsionDescription = new ArrayList<>();
    public List<String> electroDescription = new ArrayList<>();
    public List<String> cutDescription = new ArrayList<>();
    public List<String> witherDescription = new ArrayList<>();
    public List<String> poisonDescription = new ArrayList<>();
    public List<String> fireDescription = new ArrayList<>();
    public List<String> freezeeDescription = new ArrayList<>();
    public List<String> instabilityDescription = new ArrayList<>();
    public List<String> explosionDescription = new ArrayList<>();
    public List<String> shrinkDescription = new ArrayList<>();
    public List<String> enlargeDescription = new ArrayList<>();

    public int flyCost;
    public int flyTime;

    public int repairCost;
    public int repairPercent;

    public int burstCost;
    public int burstVelocity;

    public int teleportCost;
    public int teleportBlocks;

    public int captureChance;

    public int reinforcedChance;

    public int aegisResistance;
    public int aegisAbsortion;
    public double aegisSize;
    public int aegisDuration;
    public int aegisHealth;

    public double demolitionDamage;
    public int demolitionRange;
    public int demolitionCost;

    public int promptnessSpeed;
    public double promptnessSize;
    public int promptnessDuration;
    public int promptnessHealth;

    public int propulsionImpulse;
    public int propulsionCost;

    public int healingCost;
    public double healingPercent;

    public int adrenalineStrength;
    public int adrenalinePoison;
    public double adrenalineSize;
    public int adrenalineStrengthDuration;
    public int adrenalinePoisonDuration;
    public String adrenalineSoundOn;
    public String adrenalineSoundOff;
    public int adrenalineHealth;

    public int rocketryChance;

    public int channelingHalfHearts;
    public int channelingTime;

    public double solemDamage;

    public double noctisDamage;

    public double armorPercent;

    public int swarmPercent;
    public int swarmMaxEntities;
    public int swarmDistance;

    public double executionExtraDamagePerPercentPerLevel;

    public double vampirismHealth;

    public double backstabDamage;

    public double criticDamage;
    public int criticChance;
    public String criticSound;

    public int repulsionChance;

    public double electroDamage;
    public int electroChance;
    public String electroSound;

    public double cutDamage;

    public int witherModifier;
    public int witherDuration;
    public int witherChance;

    public int poisonModifier;
    public int poisonDuration;
    public int poisonChance;

    public int fireTime;

    public int freezeeChance;
    public double freezeeDamage;

    public int instabilityChance;
    public int instabilityModifier;
    public int instabilityDuration;

    public int explosionChance;
    public double explosionDamage;

    public int shrinkChance;
    public int shrinkDuration;
    public String shrinkSound;

    public int enlargeChance;
    public int enlargeDuration;
    public String enlargeSound;

    public Properties(FileConfiguration config) {
        this.config = config;

        flyName = config.getString("Properties.Fly.name").replace("&", "§");
        repairName = config.getString("Properties.Repair.name").replace("&", "§");
        burstName = config.getString("Properties.Burst.name").replace("&", "§");
        teleportName = config.getString("Properties.Teleport.name").replace("&", "§");
        captureName = config.getString("Properties.Capture.name").replace("&", "§");
        reinforcedName = config.getString("Properties.Reinforced.name").replace("&", "§");
        aegisName = config.getString("Properties.Aegis.name").replace("&", "§");
        demolitionName = config.getString("Properties.Demolition.name").replace("&", "§");
        promptnessName = config.getString("Properties.Promptness.name").replace("&", "§");
        propulsionName = config.getString("Properties.Propulsion.name").replace("&", "§");
        healingName = config.getString("Properties.Healing.name").replace("&", "§");
        adrenalineName = config.getString("Properties.Adrenaline.name").replace("&", "§");
        rocketryName = config.getString("Properties.Rocketry.name").replace("&", "§");
        purityName = config.getString("Properties.Purity.name").replace("&", "§");
        channelingName = config.getString("Properties.Channeling.name").replace("&", "§");
        solemName = config.getString("Properties.Solem.name").replace("&", "§");
        noctisName = config.getString("Properties.Noctis.name").replace("&", "§");
        armorName = config.getString("Properties.Armor.name").replace("&", "§");
        swarmName = config.getString("Properties.Swarm.name").replace("&", "§");
        executionName = config.getString("Properties.Execution.name").replace("&", "§");
        vampirismName = config.getString("Properties.Vampirism.name").replace("&", "§");
        backstabName = config.getString("Properties.Backstab.name").replace("&", "§");
        criticName = config.getString("Properties.Critic.name").replace("&", "§");
        repulsionName = config.getString("Properties.Repulsion.name").replace("&", "§");
        electroName = config.getString("Properties.Electro.name").replace("&", "§");
        cutName = config.getString("Properties.Cut.name").replace("&", "§");
        witherName = config.getString("Properties.Wither.name").replace("&", "§");
        poisonName = config.getString("Properties.Poison.name").replace("&", "§");
        fireName = config.getString("Properties.Fire.name").replace("&", "§");
        freezeeName = config.getString("Properties.Freezee.name").replace("&", "§");
        instabilityName = config.getString("Properties.Instability.name").replace("&", "§");
        explosionName = config.getString("Properties.Explosion.name").replace("&", "§");
        shrinkName = config.getString("Properties.Shrink.name").replace("&", "§");
        enlargeName = config.getString("Properties.Enlarge.name").replace("&", "§");

        flyDescription = config.getStringList("Properties.Fly.description");
        repairDescription = config.getStringList("Properties.Repair.description");
        burstDescription = config.getStringList("Properties.Burst.description");
        teleportDescription = config.getStringList("Properties.Teleport.description");
        captureDescription = config.getStringList("Properties.Capture.description");
        reinforcedDescription = config.getStringList("Properties.Reinforced.description");
        aegisDescription = config.getStringList("Properties.Aegis.description");
        demolitionDescription = config.getStringList("Properties.Demolition.description");
        promptnessDescription = config.getStringList("Properties.Promptness.description");
        propulsionDescription = config.getStringList("Properties.Propulsion.description");
        healingDescription = config.getStringList("Properties.Healing.description");
        adrenalineDescription = config.getStringList("Properties.Adrenaline.description");
        rocketryDescription = config.getStringList("Properties.Rocketry.description");
        purityDescription = config.getStringList("Properties.Purity.description");
        channelingDescription = config.getStringList("Properties.Channeling.description");
        solemDescription = config.getStringList("Properties.Solem.description");
        noctisDescription = config.getStringList("Properties.Noctis.description");
        armorDescription = config.getStringList("Properties.Armor.description");
        swarmDescription = config.getStringList("Properties.Swarm.description");
        executionDescription = config.getStringList("Properties.Execution.description");
        vampirismDescription = config.getStringList("Properties.Vampirism.description");
        backstabDescription = config.getStringList("Properties.Backstab.description");
        criticDescription = config.getStringList("Properties.Critic.description");
        repulsionDescription = config.getStringList("Properties.Repulsion.description");
        electroDescription = config.getStringList("Properties.Electro.description");
        cutDescription = config.getStringList("Properties.Cut.description");
        witherDescription = config.getStringList("Properties.Wither.description");
        poisonDescription = config.getStringList("Properties.Poison.description");
        fireDescription = config.getStringList("Properties.Fire.description");
        freezeeDescription = config.getStringList("Properties.Freezee.description");
        instabilityDescription = config.getStringList("Properties.Instability.description");
        explosionDescription = config.getStringList("Properties.Explosion.description");
        shrinkDescription = config.getStringList("Properties.Shrink.description");
        enlargeDescription = config.getStringList("Properties.Enlarge.description");

        flyCost = config.getInt("Properties.Fly.baseCost");
        flyTime = config.getInt("Properties.Fly.time");
        repairCost = config.getInt("Properties.Repair.baseCost");
        repairPercent = config.getInt("Properties.Repair.repairPercentage");
        burstCost = config.getInt("Properties.Burst.baseCost");
        burstVelocity = config.getInt("Properties.Burst.baseVelocity");
        teleportCost = config.getInt("Properties.Teleport.baseCost");
        teleportBlocks = config.getInt("Properties.Teleport.extraBlocksPerLevel");
        captureChance = config.getInt("Properties.Capture.maxProbability");
        reinforcedChance = config.getInt("Properties.Reinforced.baseProbability");
        aegisResistance = config.getInt("Properties.Aegis.resistanceLevel");
        aegisAbsortion = config.getInt("Properties.Aegis.absortionLevel");
        aegisSize = config.getDouble("Properties.Aegis.sizeMultiplier");
        aegisDuration = config.getInt("Properties.Aegis.effectsDuration");
        aegisHealth = config.getInt("Properties.Aegis.healthCost");
        demolitionDamage = config.getDouble("Properties.Demolition.baseDamage");
        demolitionRange = config.getInt("Properties.Demolition.baseRange");
        demolitionCost = config.getInt("Properties.Demolition.foodCost");
        promptnessSpeed = config.getInt("Properties.Promptness.speedLevel");
        promptnessSize = config.getDouble("Properties.Promptness.sizeMultiplier");
        promptnessDuration = config.getInt("Properties.Promptness.effectsDuration");
        promptnessHealth = config.getInt("Properties.Promptness.healthCost");
        propulsionImpulse = config.getInt("Properties.Propulsion.baseImpulse");
        propulsionCost = config.getInt("Properties.Propulsion.foodCost");
        healingCost = config.getInt("Properties.Healing.foodCost");
        healingPercent = config.getDouble("Properties.Healing.healthPercent");
        adrenalineStrength = config.getInt("Properties.Adrenaline.strengthLevel");
        adrenalinePoison = config.getInt("Properties.Adrenaline.poisonLevel");
        adrenalineSize = config.getDouble("Properties.Adrenaline.sizePerLevel");
        adrenalineStrengthDuration = config.getInt("Properties.Adrenaline.durationPerLevelStrength");
        adrenalinePoisonDuration = config.getInt("Properties.Adrenaline.durationPerLevelPoison");
        adrenalineSoundOn = config.getString("Properties.Adrenaline.sound");
        adrenalineSoundOff = config.getString("Properties.Adrenaline.soundOff");
        adrenalineHealth = config.getInt("Properties.Adrenaline.healthCost");
        rocketryChance = config.getInt("Properties.Rocketry.chancePerLevel");
        channelingHalfHearts = config.getInt("Properties.Channeling.halfHeartsPerLevel");
        channelingTime = config.getInt("Properties.Channeling.rechargeTime");
        solemDamage = config.getDouble("Properties.Solem.damagePercent");
        noctisDamage = config.getDouble("Properties.Noctis.damagePercent");
        armorPercent = config.getDouble("Properties.Armor.reductionPercent");
        swarmPercent = config.getInt("Properties.Swarm.maxPercentPerLevel");
        swarmMaxEntities = config.getInt("Properties.Swarm.maxEntities");
        swarmDistance = config.getInt("Properties.Swarm.distance");
        executionExtraDamagePerPercentPerLevel = config.getDouble("Properties.Execution.extraDamagePerPercentPerLevel");
        vampirismHealth = config.getDouble("Properties.Vampirism.healthSteal");
        backstabDamage = config.getDouble("Properties.Backstab.extraDamage");
        criticDamage = config.getDouble("Properties.Critic.extraDamage");
        criticChance = config.getInt("Properties.Critic.chancePerLevel");
        criticSound = config.getString("Properties.Critic.sound");
        repulsionChance = config.getInt("Properties.Repulsion.chancePerLevel");
        electroDamage = config.getDouble("Properties.Electro.damagePerLevel");
        electroChance = config.getInt("Properties.Electro.chancePerLevel");
        electroSound = config.getString("Properties.Electro.sound");
        cutDamage = config.getDouble("Properties.Cut.damagePerLevel");
        witherModifier = config.getInt("Properties.Wither.modifierPerLevel");
        witherDuration = config.getInt("Properties.Wither.duration");
        witherChance = config.getInt("Properties.Wither.chancePerLevel");
        poisonModifier = config.getInt("Properties.Poison.modifierPerLevel");
        poisonDuration = config.getInt("Properties.Poison.duration");
        poisonChance = config.getInt("Properties.Poison.chancePerLevel");
        fireTime = config.getInt("Properties.Fire.timePerLevel");
        freezeeChance = config.getInt("Properties.Freezee.chancePerLevel");
        freezeeDamage = config.getDouble("Properties.Freezee.damage");
        instabilityChance = config.getInt("Properties.Instability.chancePerLevel");
        instabilityModifier = config.getInt("Properties.Instability.effectLevel");
        instabilityDuration = config.getInt("Properties.Instability.durationPerLevel");
        explosionChance = config.getInt("Properties.Explosion.chancePerLevel");
        explosionDamage = config.getDouble("Properties.Explosion.damage");
        shrinkChance = config.getInt("Properties.Shrink.chancePerLevel");
        shrinkDuration = config.getInt("Properties.Shrink.durationPerLevel");
        shrinkSound = config.getString("Properties.Shrink.sound");
        enlargeChance = config.getInt("Properties.Enlarge.chancePerLevel");
        enlargeDuration = config.getInt("Properties.Enlarge.durationPerLevel");
        enlargeSound = config.getString("Properties.Enlarge.sound");
    }
}
