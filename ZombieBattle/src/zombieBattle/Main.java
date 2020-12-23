package zombieBattle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import zombieBattle.commands.ZombieStartCommand;
import zombieBattle.commands.ZombieStopCommand;

public class Main extends JavaPlugin {

	private Thread zombieSpawn;
	private Boolean running;

	/**
	 * <p>
	 * When enabled, initialze commands and events and print message "Zombie Battle
	 * plugin enabled"
	 * </p>
	 */
	@Override
	public void onEnable() {
		registerCommands();
		registerEvents();
		registerConfig();
		// ig this just says that the plugin was enabled
		getLogger().info("Zombie Battle plugin enabled");
		running = false;
	}

	/**
	 * <p>
	 * When disabled, print message "Literal toxic plugin disabled (phew)"
	 * </p>
	 */
	@Override
	public void onDisable() {
		getLogger().info("Literal toxic plugin disabled (phew)");
	}

	private void registerConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	private void registerCommands() {
		// indicates that the onCommand method in this class should be used if the
		// command is "help" or one of its aliases
		getCommand("zombieBattleStart").setExecutor(new ZombieStartCommand(this));
		getCommand("zombieBattleStop").setExecutor(new ZombieStopCommand(this));
	}

	private void registerEvents() {

	}

	public boolean setZombieSpawning(boolean turnOn, Player player) {
		if (turnOn && running) {
			return false;
		} else if (turnOn) {
			World world = player.getWorld();
			ArrayList<Location> spawnLocations = new ArrayList<Location>();
			try {
				// get list of coordinates from config
				List<String> stringCoords = getConfig().getStringList("coordinates");
				for (String l : stringCoords) {
					// add coordinate with values from config, if any exception happens at any
					// point, use default coordinates
					l = l.replaceAll(" ", "");
					int x = Integer.parseInt(l.substring(0, l.indexOf(",")));
					l = l.substring(l.indexOf(",") + 1, l.length());
					int y = Integer.parseInt(l.substring(0, l.indexOf(",")));
					l = l.substring(l.indexOf(",") + 1, l.length());
					int z = Integer.parseInt(l);
					spawnLocations.add(new Location(world, x, y, z));
				}

				// if no coordinates added, use default coordinates
				if (spawnLocations.isEmpty()) {
					throw new Exception("No coordinates added :(");
				}
			} catch (Exception e) {
				// clear it to get rid of any leftover coordinates
				spawnLocations.clear();
				spawnLocations.add(new Location(world, -211, 73, -59));
				spawnLocations.add(new Location(world, -211, 73, -98));
				spawnLocations.add(new Location(world, -139, 73, -98));
				spawnLocations.add(new Location(world, -140, 73, -59));
				getLogger().info(e.getMessage());
			}

			// starts thread and returns true
			zombieSpawn = new ZombieSpawningThread(spawnLocations, world, this);
			zombieSpawn.start();
			running = true;
			return true;
		} else if (!turnOn && !running) {
			return false;
		} else {
			zombieSpawn.interrupt();
			running = false;
			return true;
		}
	}
}
