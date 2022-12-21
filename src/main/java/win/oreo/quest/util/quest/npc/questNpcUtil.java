package win.oreo.quest.util.quest.npc;

import org.bukkit.plugin.java.JavaPlugin;
import win.oreo.quest.Main;
import win.oreo.quest.util.npc.NPCPlayer;
import win.oreo.quest.util.quest.Quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class questNpcUtil {
    public static List<QuestNpc> questNpcList = new ArrayList<>();

    private Main plugin;

    public questNpcUtil() {
        this.plugin = JavaPlugin.getPlugin(Main.class);
    }

    public void saveAllQuestNpc() {
        for (QuestNpc npc : questNpcList) {
            for (int i = 0; i < npc.getQuestMap().size(); i++) {
                plugin.questYml.getConfig().set("npc." + npc.getQuestName() + ".list." + i, npc.getQuestMap().get(i).getQuestID().toString());
            }
            plugin.questYml.getConfig().set("npc." + npc.getQuestName() + ".count", npc.getQuestMap().size());
            plugin.questYml.saveConfig();
        }
    }

    public void createQuest(String npcName, Quest quest) {
        HashMap<Integer, Quest> map = new HashMap<>();
        map.put(0, quest);
        QuestNpc npc = new QuestNpc(npcName, map);
        questNpcList.add(npc);
    }

    public void addQuest(String npcName, Quest quest) {
        getQuestNpc(npcName).getQuestMap().put(getQuestNpc(npcName).getQuestMap().size(), quest);
    }

    public void setQuest(String npcName, int index, Quest quest) {
        if (!getQuestNpc(npcName).getQuestMap().containsKey(index)) return;
        getQuestNpc(npcName).getQuestMap().put(index, quest);
    }

    public void removeQuest(String npcName, Quest quest) {
        if (!getQuestNpc(npcName).getQuestMap().containsValue(quest)) return;
        for (int i : getQuestNpc(npcName).getQuestMap().keySet()) {
            if (getQuestNpc(npcName).getQuestMap().get(i).equals(quest)) {
                int size = getQuestNpc(npcName).getQuestMap().size();
                getQuestNpc(npcName).getQuestMap().remove(i);
                for (int j = i + 1; j < size; j++) {
                    getQuestNpc(npcName).getQuestMap().put(j-1, getQuestNpc(npcName).getQuestMap().get(j));
                }
                getQuestNpc(npcName).getQuestMap().remove(size-1);
                break;
            }
        }
    }

    public void removeQuest(String npcName, int index) {
        if (!getQuestNpc(npcName).getQuestMap().containsKey(index)) return;
        int size = getQuestNpc(npcName).getQuestMap().size();
        getQuestNpc(npcName).getQuestMap().remove(index);
        for (int j = index + 1; j < size; j++) {
            getQuestNpc(npcName).getQuestMap().put(j-1, getQuestNpc(npcName).getQuestMap().get(j));
        }
        getQuestNpc(npcName).getQuestMap().remove(size-1);
    }

    public QuestNpc getQuestNpc(String npcName) {
        for (QuestNpc questNpc : questNpcList) {
            if (questNpc.getQuestName().equals(npcName)) {
                return questNpc;
            }
        }
        return null;
    }
}
