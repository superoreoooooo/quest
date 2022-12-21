package win.oreo.quest.util.quest.npc;

import win.oreo.quest.util.quest.Quest;

import java.util.HashMap;

public class QuestNpc {
    private String npcName;
    private HashMap<Integer, Quest> questMap;

    public QuestNpc(String npcName, HashMap<Integer, Quest> questMap) {
        this.npcName = npcName;
        this.questMap = questMap;
    }

    public String getNpcName() {
        return npcName;
    }

    public HashMap<Integer, Quest> getQuestMap() {
        return questMap;
    }
}
