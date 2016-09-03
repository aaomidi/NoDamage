package com.aaomidi.nodamage;

import com.aaomidi.nodamage.utils.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class NoDamage extends JavaPlugin implements Listener, CommandExecutor {
    private final static HashMap<Player, HashSet<EntityDamageEvent.DamageCause>> map = new HashMap<>();
    private final static EntityDamageEvent.DamageCause[] causes =
            {
                    EntityDamageEvent.DamageCause.DROWNING,
                    EntityDamageEvent.DamageCause.FALL,
                    EntityDamageEvent.DamageCause.FIRE,
                    EntityDamageEvent.DamageCause.FIRE_TICK,
                    EntityDamageEvent.DamageCause.LAVA,
                    EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
                    EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
            };

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("nd").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        map.putIfAbsent(player, new HashSet<>());
        HashSet<EntityDamageEvent.DamageCause> damages = map.get(player);
        if (!command.getName().equals("nd")) return false;
        switch (args.length) {
            case 0: {
                // some info
                break;
            }
            case 1: {
                if (!args[0].equalsIgnoreCase("toggle")) return false;
                if (damages.isEmpty()) {
                    damages.addAll(Arrays.asList(causes));
                    Lang.sendMessage(player, "Damage protection activated.");
                } else {
                    damages.clear();
                    Lang.sendMessage(player, "Damage protection deactivated.");
                }
                break;
            }
            case 2: {
                if (!args[0].equalsIgnoreCase("toggle")) return false;
                String damageType = args[1].toLowerCase();
                switch (damageType) {
                    case "drowning": {
                        if (damages.contains(EntityDamageEvent.DamageCause.DROWNING)) {
                            Lang.sendMessage(player, "Drowning protection deactivated.");
                            damages.remove(EntityDamageEvent.DamageCause.DROWNING);
                        } else {
                            Lang.sendMessage(player, "Drowning protection activated.");
                            damages.add(EntityDamageEvent.DamageCause.DROWNING);
                        }
                        break;
                    }
                    case "explosion": {
                        if (damages.contains(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                            Lang.sendMessage(player, "Explosion protection deactivated.");
                            damages.remove(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
                            damages.remove(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION);
                        } else {
                            Lang.sendMessage(player, "Explosion protection activated.");
                            damages.add(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
                            damages.add(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION);
                        }
                        break;
                    }
                    case "fall": {
                        if (damages.contains(EntityDamageEvent.DamageCause.FALL)) {
                            Lang.sendMessage(player, "Fall protection deactivated.");
                            damages.remove(EntityDamageEvent.DamageCause.FALL);
                        } else {
                            Lang.sendMessage(player, "Fall protection activated.");
                            damages.add(EntityDamageEvent.DamageCause.FALL);
                        }
                        break;
                    }
                    case "fire": {
                        if (damages.contains(EntityDamageEvent.DamageCause.FIRE)) {
                            Lang.sendMessage(player, "Fire protection deactivated.");
                            damages.remove(EntityDamageEvent.DamageCause.FIRE);
                            damages.remove(EntityDamageEvent.DamageCause.FIRE_TICK);
                        } else {
                            Lang.sendMessage(player, "Fire protection activated.");
                            damages.add(EntityDamageEvent.DamageCause.FIRE);
                            damages.add(EntityDamageEvent.DamageCause.FIRE_TICK);
                        }
                        break;
                    }
                    case "lava": {
                        if (damages.contains(EntityDamageEvent.DamageCause.LAVA)) {
                            Lang.sendMessage(player, "Lava protection deactivated.");
                            damages.remove(EntityDamageEvent.DamageCause.LAVA);
                        } else {
                            Lang.sendMessage(player, "Lava protection activated.");
                            damages.add(EntityDamageEvent.DamageCause.LAVA);
                        }
                        break;
                    }
                }
                break;
            }
        }
        return true;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!entity.getType().equals(EntityType.PLAYER)) return;
        Player player = (Player) entity;
        // No you don't need to optimize the hashmap. Our smart compilers will take care of that.
        if (!map.containsKey(player)) return;

        EntityDamageEvent.DamageCause damageCause = event.getCause();

        if (!map.get(player).contains(damageCause)) return;

        event.setDamage(0);
        event.setCancelled(true);
    }
}
