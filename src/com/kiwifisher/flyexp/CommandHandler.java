package com.kiwifisher.flyexp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

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

                boolean nowFlying;

                player.setAllowFlight(!player.getAllowFlight());
                player.sendMessage(player.getAllowFlight() ? ChatColor.GREEN + "You are now able to fly!" : ChatColor.RED + "You disabled flight mode");

                /*
                Keep track of who is flying because of this plugin so it doesn't interfere with essentials and such.
                 */
                if (!player.getAllowFlight()) {
                    try{
                        FlyExp.getFlightEnabledPlayers().remove(player);
                    } catch (Exception ignored){}

                } else {
                    FlyExp.getFlightEnabledPlayers().add(player);
                }

            }

        }

        return false;
    }
}
