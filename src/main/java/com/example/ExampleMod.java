package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandManager;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
    public static final String MOD_ID = "modid";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");

        CommandManager.registerCommand(new GameModeCommand());
    }

    public static class GameModeCommand implements CommandManager.Registration {
        @Override
        public void registerCommands(CommandManager.RegistrationEnvironment cme) {
            cme.register(CommandManager.literal("secgm")
                    .then(CommandManager.argument("gamemode", StringArgumentType.string())
                            .executes(context -> {
                                ServerCommandSource source = context.getSource();
                                ServerPlayerEntity player = source.getPlayer();

                                if (player != null) {
                                    int gameMode = 0;
                                    try {
                                        gameMode = Integer.parseInt(context.getArgument("gamemode", String.class));
                                    } catch (NumberFormatException e) {
                                        source.sendError(new TranslatableText("commands.secgm.invalid"));
                                        return 1;
                                    }

                                    if (gameMode < 0 || gameMode > 3) {
                                        source.sendError(new TranslatableText("commands.secgm.outofrange"));
                                        return 1;
                                    }

                                    player.setGameMode(gameMode);
                                    source.sendFeedback(new LiteralText("Game mode set to " + gameMode), true);
                                }

                                return 1;
                            })
                    )
            );
        }
    }
}