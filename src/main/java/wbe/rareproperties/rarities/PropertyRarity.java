package wbe.rareproperties.rarities;

import java.util.List;

public class PropertyRarity {

    private String name;

    private String color;

    private int weight;

    private List<String> properties;

    public PropertyRarity(String name, String color, int weight, List<String> properties) {
        this.name = name;
        this.color = color;
        this.weight = weight;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }
}
