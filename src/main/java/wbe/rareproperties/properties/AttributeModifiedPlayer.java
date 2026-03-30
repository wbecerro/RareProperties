package wbe.rareproperties.properties;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

public class AttributeModifiedPlayer {

    private Player player;

    private Attribute attribute;

    private AttributeModifier modifier;

    public AttributeModifiedPlayer(Player player, Attribute attribute, AttributeModifier modifier) {
        this.player = player;
        this.attribute = attribute;
        this.modifier = modifier;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public AttributeModifier getModifier() {
        return modifier;
    }

    public void setModifier(AttributeModifier modifier) {
        this.modifier = modifier;
    }
}
