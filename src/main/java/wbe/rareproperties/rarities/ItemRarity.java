package wbe.rareproperties.rarities;

public class ItemRarity {

    private String id;

    private int maxEnchants;

    private int minEnchants;

    private int maxEnchantLevel;

    private int maxProperties;

    private String color;

    private int weight;

    public ItemRarity(String id, int maxEnchants, int minEnchants, int maxEnchantLevel, int maxProperties, String color, int weight) {
        this.id = id;
        this.maxEnchants = maxEnchants;
        this.minEnchants = minEnchants;
        this.maxEnchantLevel = maxEnchantLevel;
        this.maxProperties = maxProperties;
        this.color = color;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMaxEnchants() {
        return maxEnchants;
    }

    public void setMaxEnchants(int maxEnchants) {
        this.maxEnchants = maxEnchants;
    }

    public int getMinEnchants() {
        return minEnchants;
    }

    public void setMinEnchants(int minEnchants) {
        this.minEnchants = minEnchants;
    }

    public int getMaxEnchantLevel() {
        return maxEnchantLevel;
    }

    public void setMaxEnchantLevel(int maxEnchantLevel) {
        this.maxEnchantLevel = maxEnchantLevel;
    }

    public int getMaxProperties() {
        return maxProperties;
    }

    public void setMaxProperties(int maxProperties) {
        this.maxProperties = maxProperties;
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
}
