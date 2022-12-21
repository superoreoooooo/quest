package win.oreo.quest.command.npc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import win.oreo.quest.util.npc.NPCPlayer;

import java.util.ArrayList;
import java.util.List;

public class npcCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("list");
            completions.add("size");
            completions.add("skin");
            completions.add("add");
            completions.add("open");
            completions.add("edit");
            completions.add("tp");
            completions.add("remove");
            return completions;
        }
        else if (args.length == 2) {
            switch (args[0]) {
                case "size" :
                case "skin" :
                case "add" :
                case "open" :
                case "edit" :
                case "tp" :
                    for (NPCPlayer player : NPCPlayer.getNPCPlayerList()) {
                        completions.add(player.getName());
                    }
                    break;
                case "remove" :
                    completions.add("all");
                    for (NPCPlayer player : NPCPlayer.getNPCPlayerList()) {
                        completions.add(player.getName());
                    }
                    break;
            }
            return completions;
        }

        return null;
    }
}
