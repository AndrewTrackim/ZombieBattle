package zombieBattle;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public class ZombieSpawningThread extends Thread {
	private ArrayList<Location> spawnLocations;
	private World world;
	private Main plugin;

	public ZombieSpawningThread(ArrayList<Location> locations, World world, Main plugin) {
		spawnLocations = locations;
		this.world = world;
		this.plugin = plugin;
	}

	@Override
	public void run() {
		while (true) {
			// see if thread needs to be stopped
			if (Thread.interrupted()) {
				break;
			}

			// set wait time
			int waitTime = 3;
			try {
				waitTime = plugin.getConfig().getInt("timeBetweenZombies") * 1000;
			} catch (Exception e) {
			}
			// wait # seconds between spawns
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				break;
			}

			// spawn the zombies
			for (Location l : spawnLocations) {
				Bukkit.getScheduler().runTask(plugin, new Runnable() {
					@Override
					public void run() {
						world.spawnEntity(l, EntityType.ZOMBIE);
					}
				});
			}

			Bukkit.getScheduler().runTask(plugin, new Runnable() {
				@Override
				public void run() {
					boolean setNight = true;
					try {
						setNight = plugin.getConfig().getBoolean("setNight");
					} catch (Exception e) {
					}
					if (setNight) {
						world.setTime(17000);
					}
				}
			});
		}
	}

}
