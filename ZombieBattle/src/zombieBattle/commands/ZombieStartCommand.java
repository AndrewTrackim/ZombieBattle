package zombieBattle.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import zombieBattle.Main;

public class ZombieStartCommand implements CommandExecutor {
	private Main plugin;

	public ZombieStartCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			boolean success = plugin.setZombieSpawning(true, (Player) sender);
			Player player = (Player) sender;
			if (success) {

				// give caller items

				giveItems(player);

				// give nearby players items
				int playerRadius;
				try {
					playerRadius = plugin.getConfig().getInt("playerRadius");
					if (playerRadius < 0) {
						throw new Exception();
					}
				} catch (Exception e) {
					playerRadius = 25;
				}
				List<Player> nearbyPlayers = (List<Player>) player.getLocation().getNearbyPlayers(playerRadius);
				for (Player p : nearbyPlayers) {
					giveItems(p);
				}
			} else {
				player.sendMessage("The zombie spawner is already running.");
			}

			return success;
		}

		return false;
	}

	private void giveItems(Player player) {
		ItemMeta currMeta;
		ArrayList<String> lore;

		// clear inventory
		boolean clear = true;
		try {
			clear = plugin.getConfig().getBoolean("clearInventory");
		} catch (Exception e) {
		}

		if (clear) {
			player.getInventory().clear();
		}

		// change gamemode
		if (plugin.getConfig().getString("gamemode").equals("adventure")) {
			player.setGameMode(GameMode.ADVENTURE);
		} else {
			// survival is the default
			player.setGameMode(GameMode.SURVIVAL);
		}

		// generate and give items
		// create helmet
		ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
		currMeta = helmet.getItemMeta();
		currMeta.setUnbreakable(true);
		currMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, false);
		currMeta.setDisplayName("Helmet of the Protector");
		lore = new ArrayList<>();
		lore.add("Used to defend the house against zombies!");
		currMeta.setLore(lore);
		helmet.setItemMeta(currMeta);

		// create chestplate
		ItemStack chestPlate = new ItemStack(Material.DIAMOND_CHESTPLATE);
		currMeta = chestPlate.getItemMeta();
		currMeta.setUnbreakable(true);
		currMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, false);
		currMeta.setDisplayName("Chestplate of the Protector");
		lore = new ArrayList<>();
		lore.add("Used to defend the house against zombies!");
		currMeta.setLore(lore);
		chestPlate.setItemMeta(currMeta);

		// create pants
		ItemStack pants = new ItemStack(Material.DIAMOND_LEGGINGS);
		currMeta = pants.getItemMeta();
		currMeta.setUnbreakable(true);
		currMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, false);
		currMeta.setDisplayName("Pants of the Protector");
		lore = new ArrayList<>();
		lore.add("Used to defend the house against zombies!");
		currMeta.setLore(lore);
		pants.setItemMeta(currMeta);

		// create shoes
		ItemStack shoes = new ItemStack(Material.DIAMOND_BOOTS);
		currMeta = shoes.getItemMeta();
		currMeta.setUnbreakable(true);
		currMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, false);
		currMeta.setDisplayName("Shoes of the Protector");
		lore = new ArrayList<>();
		lore.add("Used to defend the house against zombies!");
		currMeta.setLore(lore);
		shoes.setItemMeta(currMeta);

		// create sword
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		currMeta = sword.getItemMeta();
		currMeta.setUnbreakable(true);
		currMeta.addEnchant(Enchantment.DAMAGE_ALL, 3, false);
		currMeta.setDisplayName("Sword of the Avenger");
		lore = new ArrayList<>();
		lore.add("Used to protect the user from zombies!");
		currMeta.setLore(lore);
		sword.setItemMeta(currMeta);

		// create axe
		ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
		currMeta = axe.getItemMeta();
		currMeta.setUnbreakable(true);
		currMeta.addEnchant(Enchantment.DAMAGE_ALL, 10, true);
		currMeta.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
		AttributeModifier slowAttribute = new AttributeModifier("generic_movement_speed", -25, Operation.ADD_NUMBER);
		currMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, slowAttribute);
		AttributeModifier attackAttribute = new AttributeModifier("generic_attack_damage", 25, Operation.ADD_NUMBER);
		currMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, attackAttribute);
		currMeta.setDisplayName("Heavy Axe of the Warrior");
		lore = new ArrayList<>();
		lore.add("While the axe may slow the user down,");
		lore.add(" it has the power to dish out large damage");
		currMeta.setLore(lore);
		axe.setItemMeta(currMeta);

		// create bow
		ItemStack bow = new ItemStack(Material.BOW);
		currMeta = bow.getItemMeta();
		currMeta.setUnbreakable(true);
		currMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
		currMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
		currMeta.setDisplayName("Bow of the Rogue Archer");
		lore = new ArrayList<>();
		lore.add("Used by archers from many villages.");
		lore.add("Known for the flaming arrows and knockback.");
		currMeta.setLore(lore);
		bow.setItemMeta(currMeta);

		// create arrow
		ItemStack arrows = new ItemStack(Material.ARROW);

		// create steak
		ItemStack beef = new ItemStack(Material.COOKED_BEEF);
		beef.setAmount(64);

		// create golden apple
		ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE);
		goldenApple.setAmount(10);

		PlayerInventory playerInventory = player.getInventory();

		// give them armor
		ItemStack[] armor = { shoes, pants, chestPlate, helmet };
		playerInventory.setArmorContents(armor);

		// give them everything else
		playerInventory.addItem(sword);
		playerInventory.addItem(axe);
		playerInventory.addItem(bow);
		playerInventory.addItem(goldenApple);
		playerInventory.addItem(beef);
		playerInventory.addItem(beef);
		playerInventory.addItem(beef);
		playerInventory.addItem(arrows);
	}
}
