package autosave

import lunalib.lunaSettings.LunaSettings

object AutosaveCooldownManager {

    private var lastAutosaveTimeMs: Long = System.currentTimeMillis()

    fun canAutosave(): Boolean {
        val minutes = LunaSettings.getFloat("autosave_extended", "autosave_min_interval")
        val minIntervalMs = (minutes * 60_000).toLong()

        val now = System.currentTimeMillis()
        return now - lastAutosaveTimeMs >= minIntervalMs
    }

    fun markSaved() {
        lastAutosaveTimeMs = System.currentTimeMillis()
    }
}
