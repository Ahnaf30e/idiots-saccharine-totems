package dev.ahnaf30eidiot.tok.command;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.ahnaf30eidiot.tok.api.TOKPersistentValues;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class TOKCommands {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("tokdev")
                        .requires(srs -> srs.hasPermissionLevel(2))
                        .then(CommandManager.literal("cleanup")
                                .executes(ctx -> runCleanUp(ctx.getSource(), "noarg"))
                                .then(CommandManager.argument("mode", StringArgumentType.word())
                                        .suggests((c, b) -> {
                                            return CommandSource
                                                    .suggestMatching(List.of("all", "empty", "orphan", "noarg"), b);
                                        }).executes(ctx -> {
                                            return runCleanUp(ctx.getSource(),
                                                    StringArgumentType.getString(ctx, "mode"));
                                        }))));
    }

    private static int runCleanUp(ServerCommandSource src, String mode) {
        ServerWorld world = src.getWorld();
        TOKPersistentValues state = TOKPersistentValues.get(world);

        int before = state.getHeldOn().size();
        switch (mode) {
            case "all" -> state.getHeldOn().clear();

            case "empty" -> state.getHeldOn().entrySet()
                    .removeIf(e -> e.getValue() == null || e.getValue().isEmpty());

            case "orphan", "noarg" -> {
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

        src.sendFeedback(() -> Text.literal("§aTOK cleanup [" + mode + "] complete. Removed " + removed + " entries."),
                true);

        return removed;
    }
}
