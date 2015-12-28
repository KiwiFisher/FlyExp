package com.kiwifisher.flyexp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private FlyExp plugin;

    /*
    Instantiates the plugin this is for, giving us access to all methods and private fields.
     */
    public CommandHandler(FlyExp plugin) {

        this.plugin = plugin;
    }

    /*
     * Check when a player sends a command if it's for this plugin. If it is, then turn on fly mode for the player.
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        /*
        If a player types the command /expfly, then toggle on their fly mode.
         */
        if (command.getLabel().equalsIgnoreCase("flyexp") && commandSender instanceof Player) {

            Player player = (Player) commandSender;

            if (player.hasPermission("flyexp.use")) {

                player.setAllowFlight(!player.getAllowFlight());
                player.sendMessage(player.getAllowFlight() ? ChatColor.GREEN + "You are now able to fly!" : ChatColor.RED + "You disabled flight mode");

                /*
                Keep track of who is flying because of this plugin so it doesn't interfere with essentials and such.
                 */
                if (!player.getAllowFlight() && this.plugin.getFlightEnabledPlayers().contains(player)) {
                    this.plugin.getFlightEnabledPlayers().remove(player);

                } else {
                    this.plugin.getFlightEnabledPlayers().add(player);
                }

            }

        }

        return false;
    }
}
