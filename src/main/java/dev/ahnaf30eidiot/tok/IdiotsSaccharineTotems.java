package dev.ahnaf30eidiot.tok;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.ahnaf30eidiot.tok.block.TOKBlocks;
import dev.ahnaf30eidiot.tok.component.TOKComponents;
import dev.ahnaf30eidiot.tok.effect.TOKEffects;
import dev.ahnaf30eidiot.tok.event.TOKEvents;
import dev.ahnaf30eidiot.tok.item.TOKItemGroups;
import dev.ahnaf30eidiot.tok.item.TOKItems;
import dev.ahnaf30eidiot.tok.potion.TOKPotions;
import dev.ahnaf30eidiot.tok.recipe.TOKRecipes;

public class IdiotsSaccharineTotems implements ModInitializer {
	public static final String MOD_ID = "saccharine_totems";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("IdiotsSaccharineTotems");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Idiot started with TOK (Totem of Keeping)");
		TOKItems.registerModItems();
		TOKBlocks.regsiterBlocks();
		TOKPotions.registerPotions();
		TOKItemGroups.registerItemGroups();
		TOKEvents.registerItemEvents();
		TOKRecipes.registerSpecialRecipes();
		TOKEffects.registerEffects();
		TOKEvents.registerEvents();
		TOKEvents.registerLootTables();
		TOKEvents.registerIngredientSerializers();
		TOKComponents.registerComponentTypes();
	}
}