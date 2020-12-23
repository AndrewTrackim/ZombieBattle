package zombieBattle.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import zombieBattle.Main;

public class ZombieStopCommand implements CommandExecutor {
	private Main plugin;

	public ZombieStopCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			boolean success = plugin.setZombieSpawning(false, p);
			if (success) {
				p.sendMessage("Zombie spawner terminated");
			} else {
				p.sendMessage("The zombie spawner wasn't running");
			}
			
			return success;
		}

		return false;
	}
}
