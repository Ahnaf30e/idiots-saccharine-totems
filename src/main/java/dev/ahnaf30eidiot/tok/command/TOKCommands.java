package dev.ahnaf30eidiot.tok.command;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.ahnaf30eidiot.tok.api.TOKDevValues;
import dev.ahnaf30eidiot.tok.api.TOKPersistentValues;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class TOKCommands {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("tokdev")
                        .requires(srs -> srs.hasPermissionLevel(2))
                        .then(CommandManager.literal("ignoreNext")
                                .executes(ctx -> ignoreNext(ctx.getSource())))
                        .then(CommandManager.literal("entries")
                                .executes(ctx -> getEntries(ctx.getSource())))
                        .then(CommandManager.literal("cleanup")
                                .executes(ctx -> runCleanUp(ctx.getSource(), "noarg"))
                                .then(CommandManager.argument("mode", StringArgumentType.word())
                                        .suggests((c, b) -> {
                                            return CommandSource
                                                    .suggestMatching(List.of("all", "empty", "orphans", "noarg"), b);
                                        }).executes(ctx -> {
                                            return runCleanUp(ctx.getSource(),
                                                    StringArgumentType.getString(ctx, "mode"));
                                        }))));
        dispatcher.register(
                CommandManager.literal("isttok")
                        .requires(srs -> srs.hasPermissionLevel(0))
                        .then(CommandManager.literal("restoreFromDimensions")
                                .executes(ctx -> restoreAcrossDimensions(ctx.getSource()))));
    }

    private static int runCleanUp(ServerCommandSource src, String mode) {
        ServerWorld world = src.getWorld();
        TOKPersistentValues state = TOKPersistentValues.get(world);

        int before = state.getHeldOn().size();
        switch (mode) {
            case "all" -> state.getHeldOn().clear();

            case "empty" -> state.getHeldOn().entrySet()
                    .removeIf(e -> e.getValue() == null || e.getValue().isEmpty());

            case "orphans", "noarg" -> {
                state.getHeldOn().entrySet().removeIf(e -> (e.getValue() == null || e.getValue().isEmpty()) ||
                        src.getServer().getPlayerManager().getPlayer(e.getKey()) == null);
            }

            default -> {
                src.sendError(Text.literal("§cInvalid mode: " + mode));
                return 0;
            }
        }

        int after = state.getHeldOn().size();
        int removed = before - after;

        state.markDirty();

        src.sendFeedback(() -> Text.literal("§aTOK cleanup [ " + mode + " ] complete. Removed " + removed + " entries."),
                true);

        return removed;
    }

    
    private static int ignoreNext (ServerCommandSource src) {
        TOKDevValues.ignoreThisTime(src.getPlayer().getUuid());

        src.sendFeedback(() -> Text.literal("§9TOK ignoring next TOK popped..."),
                false);
        return 1;
    }

    private static int getEntries(ServerCommandSource src) {
        ServerWorld world = src.getWorld();
        TOKPersistentValues state = TOKPersistentValues.get(world);
        java.util.Map<?, ?> held = state.getHeldOn();

        src.sendFeedback(() -> Text.literal("§aTOK persistant data [ " + held.size() + " entries ]: \n" + held.toString()),
                false);
        return held.size();
    }
    
    private static int restoreAcrossDimensions(ServerCommandSource src) {
        ServerPlayerEntity player = src.getPlayer();
        Iterable<ServerWorld> worlds = src.getServer().getWorlds();
        int totalHeld = 0;
        for (ServerWorld world : worlds) {
                        
                TOKPersistentValues state = TOKPersistentValues.get(world);
                java.util.Map<?, ?> held = state.getHeldOn();

                int heldSize = held.size();
                ItemStack pending = state.getHeldOn().remove(player.getUuid());
                state.markDirty();
                if (pending != null && !pending.isEmpty() && !player.getWorld().isClient()) {
                        player.getInventory().insertStack(pending);
                        // newPlayer.playerScreenHandler.sendContentUpdates();
                        src.sendFeedback(() -> Text.literal("§aTOK [ "+ world.getDimensionEntry().getIdAsString() + " ] persistant data [ " + heldSize + " entries ]: §3§o~Successfully restored " +  player.getUuid()),
                        false);

                        totalHeld += heldSize;
                } else {
                        src.sendFeedback(() -> Text.literal("§aTOK [ "+ world.getDimensionEntry().getIdAsString() + " ] persistant data [ " + heldSize + " entries ]: §7§o~Nothing..."),
                        false);
                }
        }

        final int totalHeldFinal = totalHeld; // What

        if (totalHeld > 0) {
                src.sendFeedback(() -> Text.literal("§aTotal restored [ " + totalHeldFinal + " entries ]"),
                false);
        } else {
                src.sendFeedback(() -> Text.literal("§aTotal restored [ " + totalHeldFinal + " entries ]: §r\n§6§o~Nothing to restore here..."),
                false);
        }

        return totalHeld;
    }
}
