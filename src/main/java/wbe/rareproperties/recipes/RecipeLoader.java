package wbe.rareproperties.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.items.IdentifierTome;
import wbe.rareproperties.items.SindrisFavour;
import wbe.rareproperties.items.SpecialCraftingItem;
import wbe.rareproperties.properties.RareProperty;
import wbe.rareproperties.util.Utilities;

import java.text.Normalizer;
import java.util.*;

public class RecipeLoader {

    private RareProperties plugin;

    private Utilities utilities;

    private FileConfiguration config;

    public static List<NamespacedKey> keys = new ArrayList<>();

    public RecipeLoader(RareProperties plugin, FileConfiguration config) {
        this.plugin = plugin;
        utilities = new Utilities(plugin);
        this.config = config;
    }

    public void loadRecipes() {
        loadTomeRecipe();
        loadConfigRecipes();
    }

    public void unloadRecipes() {
        for(NamespacedKey key : keys) {
            plugin.getServer().removeRecipe(key);
        }
    }

    private void loadTomeRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, "IdentifierTome");
        ShapelessRecipe recipe = new ShapelessRecipe(key, new IdentifierTome(plugin));
        recipe.addIngredient(3, Material.BOOK);
        recipe.addIngredient(1, Material.ENDER_EYE);
        plugin.getServer().addRecipe(recipe);
        keys.add(key);
    }

    public void loadShapedRecipe(String id, String[] shape, HashMap<Character, String> ingredients) {
        NamespacedKey recipeKey = new NamespacedKey(plugin, Normalizer.normalize(id + "recipe", Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, getResult(id));
        recipe.shape(shape);
        for(Map.Entry<Character, String> ingredient : ingredients.entrySet()) {
            ItemStack item;
            if(ingredient.getValue().equalsIgnoreCase("specialItem")) {
                item = new SpecialCraftingItem(plugin);
            } else {
                item = new ItemStack(Material.valueOf(ingredient.getValue()));
            }
            recipe.setIngredient(ingredient.getKey(), new RecipeChoice.ExactChoice(item));
        }

        plugin.getServer().addRecipe(recipe);
        keys.add(recipeKey);
    }

    private ItemStack getResult(String id) {
        SindrisFavour sindrisFavour = new SindrisFavour();
        RareProperty rareProperty = utilities.getProperty(id);
        sindrisFavour.setProperty(rareProperty, "I", plugin);
        return sindrisFavour;
    }

    private void loadConfigRecipes() {
        Set<String> configProperties = config.getConfigurationSection("Properties").getKeys(false);
        for(String property : configProperties) {
            if(!config.contains("Properties." + property + ".recipe")) {
                continue;
            }
            String id = config.getString("Properties." + property + ".name");
            String[] shape = config.getStringList("Properties." + property + ".recipe.shape").toArray(new String[3]);
            HashMap<Character, String> ingredients = new HashMap<>();
            Set<String> configIngredients = config.getConfigurationSection("Properties." + property + ".recipe.ingredients").getKeys(false);
            for(String ingredient : configIngredients) {
                String material = config.getString("Properties." + property + ".recipe.ingredients." + ingredient + ".material");
                ingredients.put(ingredient.charAt(0), material);
            }

            loadShapedRecipe(id, shape, ingredients);
        }
    }
}
