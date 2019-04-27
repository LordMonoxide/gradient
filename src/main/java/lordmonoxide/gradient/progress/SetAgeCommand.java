package lordmonoxide.gradient.progress;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lordmonoxide.gradient.advancements.AdvancementTriggers;
import lordmonoxide.gradient.network.PacketUpdatePlayerProgress;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Collection;

public final class SetAgeCommand {
  private SetAgeCommand() { }

  public static ArgumentBuilder<CommandSource, ?> register() {
    return Commands.literal("setage")
      .requires(ctx -> ctx.hasPermissionLevel(4))
      .then(Commands.argument("age", IntegerArgumentType.integer(1, Age.highest().value()))
        .then(Commands.argument("targets", EntityArgument.players())
          .executes(ctx -> execute(ctx, Age.get(IntegerArgumentType.getInteger(ctx, "age")), EntityArgument.getPlayers(ctx, "targets")))
        )
      );
  }

  private static int execute(final CommandContext<CommandSource> ctx, final Age age, final Collection<EntityPlayerMP> players) throws CommandSyntaxException {
    final EntityPlayerMP sender = ctx.getSource().asPlayer();

    for(final EntityPlayerMP target : players) {
      target
        .getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY)
        .ifPresent(progress -> {
          progress.setAge(age);

          AdvancementTriggers.CHANGE_AGE.trigger(target);
          PacketUpdatePlayerProgress.send(target);

          target.sendMessage(new TextComponentTranslation("commands.gradient.setage.set", age.getDisplayName()));

          if(sender != target) {
            sender.sendMessage(new TextComponentTranslation("commands.gradient.setage.set_other", target.getDisplayName(), age.getDisplayName()));
          }
        });
    }

    return Command.SINGLE_SUCCESS;
  }
}
