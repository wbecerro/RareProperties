package wbe.rareproperties.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapelessRecipe;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.items.IdentifierTome;

public class RecipeLoader {

    RareProperties plugin;

    NamespacedKey identifierTome;

    public RecipeLoader(RareProperties plugin) {
        this.plugin = plugin;
        identifierTome = new NamespacedKey(plugin, "IdentifierTome");
    }

    public void loadRecipes() {
        loadTomeRecipe();
    }

    public void unloadRecipes() {
        plugin.getServer().removeRecipe(identifierTome);
    }

    private void loadTomeRecipe() {
        ShapelessRecipe recipe = new ShapelessRecipe(identifierTome, new IdentifierTome(plugin));
        recipe.addIngredient(3, Material.BOOK);
        recipe.addIngredient(1, Material.ENDER_EYE);
        plugin.getServer().addRecipe(recipe);
    }
}
