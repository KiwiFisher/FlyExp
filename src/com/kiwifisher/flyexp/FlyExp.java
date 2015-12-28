package com.kiwifisher.flyexp;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FlyExp extends JavaPlugin {
    private int decreaseAmount;
    private ArrayList<Player> flightEnabledPlayers = new ArrayList<>();

    @Override
    public void onEnable() {

        /*
        Copy in the default config
         */
        getConfig().options().copyDefaults(true);
        saveConfig();

        /*
        Register command
         */
        getCommand("flyexp").setExecutor(new CommandHandler(this));

        /*
        Get config values and assign them to instance variables
         */
        this.decreaseAmount = getConfig().getInt("decrease-amount-per-second");

        /*
        Start a repeating task to check players
         */
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {

                /*
                Uses iterator as else removing the player causes a ConcurrentModificationException as you can't remove an index of
                and array you are looking through.
                 */
                for (Iterator<Player> iterator = getFlightEnabledPlayers().iterator(); iterator.hasNext();) {

                    Player player = iterator.next();

                        /*
                        If the player is flying and doesn't have the bypass permission, then decrease their EXP.
                         */
                    if (getFlightEnabledPlayers().contains(player) && !player.hasPermission("flyexp.bypass") && player.getGameMode() != GameMode.CREATIVE) {

                        if (player.isFlying() && (player.getLevel() > 0 || player.getExp() > 0 || player.getTotalExperience() > 0)) {

                                /*
                                Decrease amount is half because this runable does checks twice per second.
                                 */
                            decreaseExp(player, decreaseAmount);

                        } else if (player.getLevel() <= 0 || player.getExp() <= 0 || player.getTotalExperience() <= 0){

                                /*
                                If the player has 0 exp left, stop them flying.
                                 */
                            player.setAllowFlight(false);
                            player.sendMessage(ChatColor.RED + "You ran out of exp! Your flight stopped working");
                            if (getFlightEnabledPlayers().contains(player)) {
                                iterator.remove();
                            }

                        }

                    }

                }

            }
        }, 0L, 5L);

    }

    public void decreaseExp(Player player, int amount) {

        int newAmount = player.getTotalExperience() - amount;

        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);
        player.giveExp(newAmount);

    }

    public List<Player> getFlightEnabledPlayers() {
        return flightEnabledPlayers;
    }

}
