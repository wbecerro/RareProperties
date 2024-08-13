package wbe.rareproperties.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Messages {

    private FileConfiguration config;

    public String listMessage;
    public String notEnoughArgs;
    public String alreadyHasProperty;
    public String noPermission;
    public String propertyAdded;
    public String propertyAddedDisc;
    public String propertyNotPresent;
    public String propertyRemoved;
    public String propertyGiven;
    public String cannotRemoveProperty;
    public String addParams;
    public String removeParams;
    public String getParams;
    public String giveParams;
    public String giveRandomParams;
    public String cannotTeleport;
    public String captureSuccess;
    public String notEnoughHealth;
    public List<String> help;
    public String reload;
    public String socketGiven;
    public String tomeGiven;
    public String limited;
    public String itemGiven;
    public String itemIdentified;
    public String noItemsToIdentify;
    public String socketNoColor;
    public String socketApplied;
    public String socketSlotAdded;

    public Messages(FileConfiguration config) {
        this.config = config;

        listMessage = config.getString("Messages.listMessage").replace("&", "§");
        notEnoughArgs = config.getString("Messages.notEnoughArgs").replace("&", "§");
        alreadyHasProperty = config.getString("Messages.alreadyHasProperty").replace("&", "§");
        noPermission = config.getString("Messages.noPermission").replace("&", "§");
        propertyAdded = config.getString("Messages.propertyAdded").replace("&", "§");
        propertyAddedDisc = config.getString("Messages.propertyAddedDisc").replace("&", "§");
        propertyNotPresent = config.getString("Messages.propertyNotPresent").replace("&", "§");
        propertyRemoved = config.getString("Messages.propertyRemoved").replace("&", "§");
        propertyGiven = config.getString("Messages.propertyGiven").replace("&", "§");
        cannotRemoveProperty = config.getString("Messages.cannotRemoveProperty").replace("&", "§");
        addParams = config.getString("Messages.addParams").replace("&", "§");
        removeParams = config.getString("Messages.removeParams").replace("&", "§");
        getParams = config.getString("Messages.getParams").replace("&", "§");
        giveParams = config.getString("Messages.giveParams").replace("&", "§");
        giveRandomParams = config.getString("Messages.giveRandomParams").replace("&", "§");
        cannotTeleport = config.getString("Messages.cannotTeleport").replace("&", "§");
        captureSuccess = config.getString("Messages.captureSuccess").replace("&", "§");
        help = config.getStringList("Messages.help");
        reload = config.getString("Messages.reload").replace("&", "§");
        socketGiven = config.getString("Messages.socketGiven").replace("&", "§");
        tomeGiven = config.getString("Messages.tomeGiven").replace("&", "§");
        limited = config.getString("Messages.limited").replace("&", "§");
        itemGiven = config.getString("Messages.itemGiven").replace("&", "§");
        itemIdentified = config.getString("Messages.itemIdentified").replace("&", "§");
        noItemsToIdentify = config.getString("Messages.noItemsToIdentify").replace("&", "§");
        socketNoColor = config.getString("Messages.socketNoColor").replace("&", "§");
        socketApplied = config.getString("Messages.socketApplied").replace("&", "§");
        socketSlotAdded = config.getString("Messages.socketSlotAdded").replace("&", "§");
    }
}
