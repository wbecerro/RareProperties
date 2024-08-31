package wbe.rareproperties.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapelessRecipe;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.items.IdentifierTome;

import java.util.ArrayList;
import java.util.List;

public class RecipeLoader {

    private RareProperties plugin;

    private List<NamespacedKey> keys = new ArrayList<>();

    public RecipeLoader(RareProperties plugin) {
        this.plugin = plugin;
    }

    public void loadRecipes() {
        loadTomeRecipe();
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
}
