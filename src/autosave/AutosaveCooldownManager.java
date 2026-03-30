package autosave;

import com.fs.starfarer.api.Global;
import lunalib.lunaSettings.LunaSettings;

public class AutosaveCooldownManager {

    private static long lastAutosaveTimeMs = System.currentTimeMillis();

    public static boolean canAutosave() {
        float minutes = LunaSettings.getFloat("autosave_extended", "autosave_min_interval");
        long minIntervalMs = (long)(minutes * 60_000);

        long now = System.currentTimeMillis();
        return now - lastAutosaveTimeMs >= minIntervalMs;
    }

    public static void markSaved() {
        lastAutosaveTimeMs = System.currentTimeMillis();
    }
}
